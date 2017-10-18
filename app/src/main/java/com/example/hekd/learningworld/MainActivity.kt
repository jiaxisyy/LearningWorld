package com.example.hekd.learningworld

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.hekd.learningworld.adapter.RvAdapter
import com.example.hekd.learningworld.bean.greendao.DaoMaster
import com.example.hekd.learningworld.ui.VideoActivity
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.io.Serializable


class MainActivity : AutoLayoutActivity() {
    private var currentParent: File? = null    //记录当前文件的父文件夹
    private var currentFiles: Array<File>? = null
    private val ROOT_PATH = Environment.getExternalStorageDirectory().path + "/babacitvd"
    var dbList_paths: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //修改字体
        val typeface = Typeface.createFromAsset(assets, "fonts/mini.TTF")
        tv_lw_main_topName.typeface = typeface
        tv_lw_main_topName.text = getString(R.string.learning_world)
        initGreenDao()
        init()
    }

    /**
     *初始化数据库
     */
    private fun initGreenDao() {
        val devOpenHelper = DaoMaster.DevOpenHelper(this, getString(R.string.db_videos_name), null)
        val daoMaster = DaoMaster(devOpenHelper.writableDatabase)
        val session = daoMaster.newSession()
        val gdVideoInfoDao = session.gdVideoInfoDao
        val videoInfoList = gdVideoInfoDao.queryBuilder().list()
        dbList_paths=ArrayList<String>()
        for (i in videoInfoList.indices) {
            dbList_paths!!.add(videoInfoList[i].video_path)
        }
    }

    /**
     * 初始化界面
     */
    private fun init() {

        val root = File(ROOT_PATH)
        if (root.exists()) {
            currentParent = root
            currentFiles = root.listFiles()
            inflateListView(currentFiles as Array<out File>?)
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
            root.mkdir()
        }

    }

    var test: Array<out File>? = null

    /*
    * 更新listview
    */
    private fun inflateListView(files: Array<out File>?) {

        // TODO Auto-generated method stub
        val list = ArrayList<String>()
        val list_path = ArrayList<String>()
        val list_DirOrFile = ArrayList<Int>()
        val list_isPlayed = ArrayList<Int>()//是否观看过存储


        // 1表示文件,0表示文件夹
        // 1表示已观看,0表示未观看


        for (i in files!!.indices) {
            if (files[i].isFile) {
                list_DirOrFile.add(1)
                if (dbList_paths!!.contains(files[i].path)) {//判断是否观看
                    //已观看
                    list_isPlayed.add(1)
                } else {
                    //未观看
                    list_isPlayed.add(0)
                }
            } else {
                list_DirOrFile.add(0)
                //文件夹默认未观看
                list_isPlayed.add(0)
            }


            list.add(files[i].name)
            list_path.add(files[i].path)
        }
        rv_lw_main.adapter = RvAdapter(list as List<String>, list_DirOrFile, list_isPlayed, object : RvAdapter.ItemClickListener {
            override fun itemClick(view: View, position: Int) {
                val file = currentFiles!![position]
                //如果点击的是文件，不做任何处理
                if (file.isFile) {
                    val intent = Intent(this@MainActivity, VideoActivity::class.java)
                    intent.putExtra("VIDEO_PATH", file.path)
                    intent.putExtra("VIDEO_FILES_PATH", list_path as Serializable)
                    intent.putExtra("VIDEO_POSITION", position)
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


}
