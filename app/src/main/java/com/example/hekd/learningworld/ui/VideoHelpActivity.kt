package com.example.hekd.learningworld.ui

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.hekd.learningworld.R
import com.example.hekd.learningworld.adapter.RvHelpAdapter
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by hekd on 2017/12/7.
 */
class VideoHelpActivity : AutoLayoutActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arrayListOf = arrayListOf("视频文件格式支持：MP4、3GP、AVI；", "视频编码格式支持：H.264、MPEG-4；", "视频分辨率：800（宽）*480（高）；", "视频帧率：30帧；")

        rv_lw_main.adapter = RvHelpAdapter(arrayListOf)
        rv_lw_main.layoutManager = LinearLayoutManager(this)
        rl_lw_main_nine.visibility = View.GONE
        btn_video_help.visibility = View.GONE
        //修改字体
        val typeface = Typeface.createFromAsset(assets, "fonts/mini.TTF")
        tv_lw_main_topName.typeface = typeface
        tv_lw_main_topName.text = getString(R.string.video_help)
        btn_lw_back.setOnClickListener {
            finish()
        }
    }
}