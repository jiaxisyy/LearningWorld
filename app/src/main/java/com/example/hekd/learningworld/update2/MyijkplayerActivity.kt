package com.example.hekd.learningworld.update2

import android.os.Bundle
import com.example.hekd.learningworld.R
import com.example.hekd.learningworld.update2.interf.VideoPlayerListener
import com.example.hekd.learningworld.update2.v.IView
import com.example.hekd.learningworld.update2.view.IjkVideoView
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.avtivity_ijk.*
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * Created by hekd on 2017/11/28.
 */
class MyijkplayerActivity : AutoLayoutActivity(), IView {
    override fun videoStart() {
    }

    override fun videoStop() {
    }

    override fun videoPause() {
    }

    override fun hideControl() {
    }

    override fun showControl() {
    }

    override fun fullScreen() {
    }

    override fun nextVideo() {
    }

    override fun previousVideo() {
    }

    var ijkPlayer: IjkVideoView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        try {
//            IjkMediaPlayer.loadLibrariesOnce(null)
//            IjkMediaPlayer.native_profileBegin("libijkplayer.so")
//        } catch (e: Exception) {
//            finish()
//        }
        val path = intent.getStringExtra("ijk_video_path")

        ijkPlayer = IjkVideoView(this)
        loadVideo(path)
        ijkPlayer!!.setListener(object :VideoPlayerListener(){
            override fun onBufferingUpdate(p0: IMediaPlayer?, p1: Int) {
            }

            override fun onCompletion(p0: IMediaPlayer?) {
                p0!!.seekTo(0)
                p0.start()
            }

            override fun onPrepared(p0: IMediaPlayer?) {
                p0!!.start()
            }

            override fun onInfo(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean = false

            override fun onVideoSizeChanged(p0: IMediaPlayer?, p1: Int, p2: Int, p3: Int, p4: Int) {

            }

            override fun onError(p0: IMediaPlayer?, p1: Int, p2: Int): Boolean = false

            override fun onSeekComplete(p0: IMediaPlayer?) {

            }
        })

//        ijkPlayer!!.start()
    }

    fun loadVideo(path: String) {
        ijkPlayer!!.setVideoPath(path)
    }

    override fun onStop() {
        super.onStop()
        IjkMediaPlayer.native_profileEnd()
    }


}