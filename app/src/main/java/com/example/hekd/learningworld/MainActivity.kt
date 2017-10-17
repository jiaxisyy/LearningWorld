package com.example.hekd.learningworld

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.hekd.learningworld.adapter.RvAdapter
import com.example.hekd.learningworld.bean.Constant
import com.example.hekd.learningworld.ui.ChineseClassicsActivity
import com.example.hekd.learningworld.ui.VideoActivity
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.io.Serializable


class MainActivity : AutoLayoutActivity() {
    private var currentParent: File? = null    //记录当前文件的父文件夹
    private var currentFiles: Array<File>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val list = listOf<String>("国学经典", "童话故事", "记忆编码", "九门功课")
//        //往事如风
//
//
//        rv_lw_main.adapter = RvAdapter(list, object : RvAdapter.ItemClickListener {
//            override fun itemClick(view: View, position: Int) {
//                when (position) {
//                    0 ->   startActivity(Intent(this@MainActivity, ChineseClassicsActivity::class.java))
//                }
//            }
//        })
//        rv_lw_main.layoutManager = LinearLayoutManager(this)

        //修改字体
        val typeface = Typeface.createFromAsset(assets, "fonts/mini.TTF")
        tv_lw_main_topName.typeface = typeface
        tv_lw_main_topName.text = getString(R.string.learning_world)
        init()
    }

    /**
     * 初始化界面
     */
    private fun init() {
        val root = File(Environment.getExternalStorageDirectory().path + "/babacitvd")
        if (root.exists()) {
            currentParent = root
            currentFiles = root.listFiles()
            inflateListView(currentFiles as Array<out File>?)
            btn_lw_back.setOnClickListener {
                try {
                    if (!currentParent!!.getCanonicalFile().equals(Environment.getExternalStorageDirectory().path)) {
                        if (currentParent!!.path.equals(Environment.getExternalStorageDirectory().path)) {
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
        for (i in files!!.indices) {
            if (files[i].isDirectory) {

            } else {

            }

            list.add(files[i].name)
        }
        rv_lw_main.adapter = RvAdapter(list as List<String>, object : RvAdapter.ItemClickListener {
            override fun itemClick(view: View, position: Int) {
                val file = currentFiles!![position]
                //如果点击的是文件，不做任何处理
                if (file.isFile) {
                    if (file.name.endsWith(".mp4") || file.name.endsWith(".m4v")) {
                        val intent = Intent(this@MainActivity, VideoActivity::class.java)
                        intent.putExtra("VIDEO_PATH", file.path)
//                        intent.putExtra("VIDEO_FILES", files as Parcelable)
                        intent.putExtra("VIDEO_POSITION", position)
                        if(iSendFile!=null){
                            iSendFile?.sendFile(files as Array<File>)
                        }
                        startActivity(intent)
                    }
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


    var iSendFile: ISendFile? = null

    fun etISendFile(ise: ISendFile) {
        iSendFile = ise
    }

    interface ISendFile {
        fun sendFile(array: Array<File>)
    }
}
