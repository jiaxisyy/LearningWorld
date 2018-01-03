package com.example.hekd.learningworld.update2

import android.os.Bundle
import android.view.SurfaceHolder
import com.example.hekd.learningworld.R
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.activity_ijk.*

import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.IOException

/**
 * Created by hekd on 2017/11/28.
 */
class MyijkplayerActivity : AutoLayoutActivity(), SurfaceHolder.Callback, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnBufferingUpdateListener {

    var ijkPlayer: IjkMediaPlayer? = null


    override fun onPrepared(p0: IMediaPlayer?) {
        ijkPlayer!!.start()
    }

    override fun onCompletion(p0: IMediaPlayer?) {
        ijkPlayer!!.seekTo(0)
        ijkPlayer!!.start()
    }

    override fun onBufferingUpdate(p0: IMediaPlayer?, p1: Int) {
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {

        ijkPlayer!!.setDisplay(p0)
        ijkPlayer!!.prepareAsync()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ijk)
        val path = intent.getStringExtra("ijk_video_path")

        sv_ijk.holder.addCallback(this)
        ijkPlayer = IjkMediaPlayer()
        //开启硬解码
        ijkPlayer!!.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)

        try {
            ijkPlayer!!.dataSource = path
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //mediaPlayer准备工作
        ijkPlayer!!.setOnPreparedListener(this)
        //MediaPlayer完成
        ijkPlayer!!.setOnCompletionListener(this)
        //当前加载进度的监听
        ijkPlayer!!.setOnBufferingUpdateListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ijkPlayer!!.stop()
    }


}