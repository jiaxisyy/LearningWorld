package com.example.hekd.learningworld.update2.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.example.hekd.learningworld.update2.interf.VideoPlayerListener
import kotlinx.android.synthetic.main.avtivity_ijk.view.*
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.IOException


/**
 * Created by hekd on 2017/11/28.
 */
class IjkVideoView : FrameLayout, SurfaceHolder.Callback {
    val TAG = "syy log = "
    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
//        videoLoad()
        println(TAG + "surfaceChanged")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        println(TAG + "surfaceDestroyed")
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        //surfaceview创建成功后，加载视频
        videoLoad()
        println(TAG + "surfaceCreated")
    }

    var mPlayer: IMediaPlayer? = null

    /**视频文件地址*/
    var mPath: String = ""
    var mContext: Context? = null
    var listener: VideoPlayerListener? = null
    var surfaceView: SurfaceView? = null

    constructor(context: Context?) : super(context) {
        initVideoView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initVideoView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initVideoView(context)
    }


    fun initVideoView(context: Context?) {
        isFocusable = true
        mContext = context
    }

    /**
     * 设置视频地址。
     * 根据是否第一次播放视频，做不同的操作。
     */
    fun setVideoPath(path: String) {

        if (TextUtils.equals("", mPath)) {
            //如果是第一次播放视频，那就创建一个新的surfaceView
            mPath = path
            createSurfaceView()
        } else {
            mPath = path
            videoLoad()
        }
    }

    /**
     * 加载视频
     */
    private fun videoLoad() {
        createPlayer()
        try {
            mPlayer!!.dataSource = mPath
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //给mediaPlayer设置视图
        mPlayer!!.setDisplay(surfaceView!!.holder)
        mPlayer!!.prepareAsync()
    }

    /**
     * 创建播放器
     */
    private fun createPlayer() {
        if (mPlayer != null) {
            mPlayer!!.stop()
            mPlayer!!.setDisplay(null)
            mPlayer!!.release()
        }
        val ijkMediaPlayer = IjkMediaPlayer()

        //开启硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
        mPlayer = ijkMediaPlayer
        if (listener != null) {
            mPlayer!!.setOnPreparedListener(listener)
            mPlayer!!.setOnInfoListener(listener)
            mPlayer!!.setOnSeekCompleteListener(listener)
            mPlayer!!.setOnBufferingUpdateListener(listener)
            mPlayer!!.setOnErrorListener(listener)
        }


    }

    internal fun setListener(listener: VideoPlayerListener) {
        this.listener = listener
        if (mPlayer != null) {
            mPlayer!!.setOnPreparedListener(listener)
        }
    }

    private fun createSurfaceView() {

        surfaceView = SurfaceView(mContext)
        surfaceView!!.holder!!.addCallback(this)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER)
        surfaceView!!.layoutParams = layoutParams
//        surfaceView!!.holder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        this.addView(surfaceView)
    }

    fun start() {
        if (mPlayer != null) {
            mPlayer!!.start()
        }
    }

    fun release() {
        if (mPlayer != null) {
            mPlayer!!.reset()
            mPlayer!!.release()
            mPlayer = null
        }
    }

    fun pause() {
        if (mPlayer != null) {
            mPlayer!!.pause()
        }
    }

    fun stop() {
        if (mPlayer != null) {
            mPlayer!!.stop()
        }
    }


    fun reset() {
        if (mPlayer != null) {
            mPlayer!!.reset()
        }
    }


    fun getDuration(): Long {
        return if (mPlayer != null) {
            mPlayer!!.getDuration()
        } else {
            0
        }
    }


    fun getCurrentPosition(): Long {
        return if (mPlayer != null) {
            mPlayer!!.getCurrentPosition()
        } else {
            0
        }
    }


    fun seekTo(l: Long) {
        if (mPlayer != null) {
            mPlayer!!.seekTo(l)
        }
    }


}