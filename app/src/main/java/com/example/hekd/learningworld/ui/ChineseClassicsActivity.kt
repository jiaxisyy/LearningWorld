package com.example.hekd.learningworld.ui

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.hekd.learningworld.R
import com.example.hekd.learningworld.adapter.RvAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException


class ChineseClassicsActivity : AppCompatActivity() {
    private var currentParent: File? = null    //记录当前文件的父文件夹
    private var currentFiles: Array<File>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //替换国学经典图片
        iv_lw_main_topName.setImageResource(R.drawable.btn_more)
        init()

        btn_lw_back.setOnClickListener {
            try {
                if (!currentParent!!.getCanonicalFile().equals(Environment.getExternalStorageDirectory().path)) {
                    if (currentParent!!.path.equals(Environment.getExternalStorageDirectory().path)) {
                        finish()
                    }else{
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
    }

    private fun init() {
        val root = File(Environment.getExternalStorageDirectory().path)
        if (root.exists()) {
            currentParent = root
            currentFiles = root.listFiles()
            inflateListView(currentFiles as Array<out File>?)
        } else {//没有这个目录
            root.mkdir()
        }


    }

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
                //如果点击的是文件，不做任何处理
                if (currentFiles!![position].isFile) {


                    return
                }
                val temp = currentFiles!![position].listFiles()
                if (temp == null || temp.size == 0) {
                    Toast.makeText(applicationContext, "此文件夹不可用或者文件夹为空", Toast.LENGTH_SHORT).show()
                    return
                }
                currentParent = currentFiles!![position]
                currentFiles = temp
                inflateListView(currentFiles)


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
