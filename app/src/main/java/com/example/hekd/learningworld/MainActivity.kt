package com.example.hekd.learningworld

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.example.hekd.learningworld.adapter.RvAdapter
import com.example.hekd.learningworld.bean.greendao.GDVideoInfoDao
import com.example.hekd.learningworld.ui.VideoActivity
import com.example.hekd.learningworld.util.SdCardUtil
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.io.Serializable


class MainActivity : AutoLayoutActivity() {
    private var currentParent: File? = null    //记录当前文件的父文件夹
    private var currentFiles: Array<File>? = null
    /**外置存储*/
    private var ROOT_PATH: String? = null
    /**内置存储*/
//    private val ROOT_PATH = Environment.getExternalStorageDirectory().path + "/babacitvd"
    var dbList_paths: ArrayList<String>? = null
    var dbList_progress: ArrayList<Int>? = null
    var dbList_maxProgress: ArrayList<Int>? = null
    /**启动外部的app包名*/
    var OTHERAPP_PACKAGENAME = "cn.icoxedu.activity_nine_lessons"
    /**是否有外置SD卡,默认有*/
    var isExistSDCard = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //修改字体
        val typeface = Typeface.createFromAsset(assets, "fonts/mini.TTF")
        tv_lw_main_topName.typeface = typeface
        tv_lw_main_topName.text = getString(R.string.learning_world)
        initGreenDao()
        init()
        initNineOfCourses()
    }

    /**
     * 九门功课
     */
    private fun initNineOfCourses() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(OTHERAPP_PACKAGENAME, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        rl_lw_main_nine.setOnClickListener {
            if (packageInfo == null) {
                Toast.makeText(this, getString(R.string.app_no_install), Toast.LENGTH_SHORT).show()
            } else {
                System.out.println("已经安装")
                val intent = packageManager.getLaunchIntentForPackage(OTHERAPP_PACKAGENAME)
                if (intent != null) {
                    startActivity(intent)
                }
            }
        }
    }

    /**
     *初始化数据库
     */
    private fun initGreenDao() {
        val gdVideoInfoDao = MyApplication().getSession().gdVideoInfoDao
        val queryBuilder = gdVideoInfoDao.queryBuilder()
        val videoInfoList = queryBuilder.list()
        println("videoInfoList.size======${videoInfoList.size}")
        dbList_paths = ArrayList<String>()
        dbList_progress = ArrayList<Int>()
        dbList_maxProgress = ArrayList<Int>()
        for (i in videoInfoList.indices) {
            dbList_paths!!.add(videoInfoList[i].video_path)
            dbList_progress!!.add(videoInfoList[i].video_progress)
            dbList_maxProgress!!.add(videoInfoList[i].video_duration)
        }
    }


    /**
     * 初始化界面
     */
    private fun init() {
        val storagePath = SdCardUtil.getStoragePath(this, true)//外置sd卡
        if (!File(storagePath+ "/babacitvd").exists()) {
            println("我执行了1")
            isExistSDCard = false
        } else {
            println("我执行了2")
//            val storagePath = SdCardUtil.getStoragePath(this, true)//外置sd卡
            isExistSDCard = true
            ROOT_PATH = storagePath + "/babacitvd"
            val root = File(ROOT_PATH)
            if (root.exists()) {
                currentParent = root
                currentFiles = root.listFiles()
                if ((currentFiles as Array<out File>?)?.size != 0) {
                    inflateListView(currentFiles as Array<out File>?)
                }
                btn_lw_back.setOnClickListener {
                    try {
                        if (!currentParent!!.getCanonicalFile().equals(Environment.getExternalStorageDirectory().path)) {
                            if (currentParent!!.path.equals(ROOT_PATH)) {
                                finish()
                            } else {
                                currentParent = currentParent!!.getParentFile() //获取上级目录
                                currentFiles = currentParent!!.listFiles()             //取得当前层所有文件
                                inflateListView(currentFiles)   //更新列表
                            }
                        }
                    } catch (e: IOException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                }
            } else {//没有这个目录
//                root.mkdir()
//                init()
//            tv_lw_main_nothing.visibility = View.VISIBLE
            }
        }
        if (!isExistSDCard) {
            btn_lw_back.setOnClickListener {
                finish()
            }
        }
    }

    var test: Array<out File>? = null
    var countPlayed = 0
    var firstInto = true

    /*
    * 更新listview
    */
    private fun inflateListView(files: Array<out File>?) {
        val absolutePath = files!![0].absolutePath
        println("absolutePath==========$absolutePath")
        println("rootPath==========$ROOT_PATH")
        countPlayed = 0
        // TODO Auto-generated method stub
        val list = ArrayList<String>()
        val list_path = ArrayList<String>()
        val list_DirOrFile = ArrayList<Int>()
        val list_isPlayed = ArrayList<Int>()//是否观看过存储
        val list_Percent = ArrayList<Float>()
        val list_preogress = ArrayList<Int>()//进度值
        // 1表示文件,0表示文件夹
        // 1表示已观看,0表示未观看
        val gdVideoInfoDao = MyApplication().getSession().gdVideoInfoDao
        val substring = absolutePath.substring(0, absolutePath.lastIndexOf("/"))
        println("substring=================$substring")
        if (substring == ROOT_PATH) {
            /**显示九门功课*/
            rl_lw_main_nine.visibility = View.VISIBLE
            tv_lw_line.visibility = View.VISIBLE
        } else {
            rl_lw_main_nine.visibility = View.GONE
            tv_lw_line.visibility = View.GONE
        }

        for (i in files!!.indices) {

            if (files[i].isFile) {
                list_DirOrFile.add(1)
                if (dbList_paths!!.contains(files[i].path)) {//判断是否观看
                    //已观看
                    list_isPlayed.add(1)
                    val db_path = gdVideoInfoDao.queryBuilder().where(GDVideoInfoDao.Properties.Video_path.eq(files[i].path)).list()
                    val video_progress = db_path[0].video_progress.toFloat()
                    val video_duration = db_path[0].video_duration.toFloat()
                    list_preogress.add(db_path[0].video_progress)
                    list_Percent.add(video_progress / video_duration)
                } else {
                    //未观看
                    list_isPlayed.add(0)
                    list_Percent.add(0f)
                    list_preogress.add(0)
                }
            } else {
                list_DirOrFile.add(0)
                //文件夹默认未观看
                list_isPlayed.add(0)
                list_Percent.add(0f)
                list_preogress.add(0)
            }
            list.add(files[i].name)
            list_path.add(files[i].path)
        }

        rv_lw_main.adapter = RvAdapter(list as List<String>, list_DirOrFile, list_isPlayed, list_Percent, object : RvAdapter.ItemClickListener {
            override fun itemClick(view: View, position: Int) {

                val file = currentFiles!![position]
                //如果点击的是文件，不做任何处理
                if (file.isFile) {
                    val intent = Intent(this@MainActivity, VideoActivity::class.java)
                    intent.putExtra("VIDEO_PATH", file.path)
                    intent.putExtra("VIDEO_FILES_PATH", list_path as Serializable)
                    intent.putExtra("VIDEO_POSITION", position)
                    intent.putExtra("VIDEO_PROGRESS", list_preogress[position])
                    startActivity(intent)
                } else {

                    val temp = currentFiles!![position].listFiles()
                    if (temp == null || temp.size == 0) {
                        Toast.makeText(applicationContext, getString(R.string.dir_null), Toast.LENGTH_SHORT).show()
                        return
                    }
                    currentParent = currentFiles!![position]
                    currentFiles = temp
                    inflateListView(currentFiles)
                }
            }
        })
        rv_lw_main.layoutManager = LinearLayoutManager(this)
        try {
            val canonicalPath = currentParent?.getCanonicalPath()

            println(canonicalPath + "============")

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExistSDCard) {
                try {
                    if (!currentParent!!.getCanonicalFile().equals(Environment.getExternalStorageDirectory().path)) {
                        if (currentParent!!.path.equals(ROOT_PATH)) {
                            finish()
                        } else {
                            currentParent = currentParent!!.getParentFile() //获取上级目录
                            currentFiles = currentParent!!.listFiles()             //取得当前层所有文件
                            inflateListView(currentFiles)   //更新列表
                        }
                    }
                } catch (e: Exception) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            } else {
                finish()
            }
            return false
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }


}
