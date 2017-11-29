package com.example.hekd.learningworld.update2

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.hekd.learningworld.R
import com.example.hekd.learningworld.adapter.RvAdapter2
import com.example.hekd.learningworld.util.SdCardUtil
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.activity_t_allvideo.*
import java.io.File
import java.util.*

/**
 * Created by hekd on 2017/11/28.
 */
class TAllVideoActivity : AutoLayoutActivity() {
    /**内置存储*/
    private val ROOT_PATH = Environment.getExternalStorageDirectory().path + "/test"

    val names = ArrayList<File>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_t_allvideo)
        getAllVideo()
    }


    /**
     *获取所有的视频文件,除去babacity文件夹里的
     */
    private fun getAllVideo() {
        val storagePath = SdCardUtil.getStoragePath(this, true)//外置sd卡
        inflate(ROOT_PATH)
        rv_t_allVideo.layoutManager = LinearLayoutManager(this)
        Collections.sort(names)
        rv_t_allVideo.adapter = RvAdapter2(names as List<File>, object : RvAdapter2.ItemClickListener {
            override fun itemClick(view: View, position: Int) {
                val intent = Intent(this@TAllVideoActivity,MyijkplayerActivity::class.java)
                intent.putExtra("ijk_video_path", names[position].path)
                startActivity(intent)
            }
        })
    }

    /**
     *
     * 遍历出所有文件
     */
    private fun inflate(path: String?) {
        val root = File(path)
        if (root.exists()) {//存在此路径
            val listFiles = root.listFiles()
            listFiles.forEach {
                if (it.isFile) {
                    names.add(it)
                } else {//文件夹
                    inflate(it.path)
                }
            }
        } else {//路径不存在

        }
    }
}