package com.example.hekd.learningworld.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Display
import android.view.SurfaceHolder
import android.view.View
import android.widget.RelativeLayout
import android.widget.SeekBar
import com.example.hekd.learningworld.MainActivity
import com.example.hekd.learningworld.R
import kotlinx.android.synthetic.main.activity_video.*
import java.io.File


class VideoActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, SeekBar.OnSeekBarChangeListener, SurfaceHolder.Callback, MediaPlayer.OnVideoSizeChangedListener, MainActivity.ISendFile {
    override fun sendFile(array: Array<File>) {
        //获取当前position
        files=array

        println("测试${files!![0].name}")

    }


    override fun onVideoSizeChanged(p0: MediaPlayer?, p1: Int, p2: Int) {
        println("onVideoSizeChanged->p1=$p1" + "p2=$p2")
    }

    /**
     * 完成播放
     */
    override fun onCompletion(p0: MediaPlayer?) {
        isPlaying = false
        finish()
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        when (p1) {
            MediaPlayer.MEDIA_ERROR_SERVER_DIED -> Log.v("Play Error:::", "MEDIA_ERROR_SERVER_DIED")
            MediaPlayer.MEDIA_ERROR_UNKNOWN -> Log.v("Play Error:::", "MEDIA_ERROR_UNKNOWN")
            else -> {
            }
        }
        return false

    }


    private val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            println("msg======" + msg?.obj)
            sb_video.progress = msg?.obj as Int
        }

    }


    override fun onInfo(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {

        // 当一些特定信息出现或者警告时触发
        when (p1) {
            MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING -> {
            }
            MediaPlayer.MEDIA_INFO_METADATA_UPDATE -> {
            }
            MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING -> {
            }
            MediaPlayer.MEDIA_INFO_NOT_SEEKABLE -> {
            }
        }
        return false
    }

    var isPlaying = true
    var vWidth: Int = 0
    var vHeight: Int = 0

    override fun onPrepared(player: MediaPlayer?) {
        // 当prepare完成后，该方法触发，在这里我们播放视频

        //首先取得video的宽和高
        vWidth = player!!.videoWidth
        vHeight = player.videoHeight


        if (vWidth > currDisplay!!.width || vHeight > currDisplay!!.height) {
            //如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
            val wRatio = vWidth / currDisplay!!.width
            val hRatio = vHeight / currDisplay!!.height

            //选择大的一个进行缩放
            val ratio = Math.max(wRatio, hRatio)

            vWidth = Math.ceil((vWidth / ratio).toDouble()).toInt()
            vHeight = Math.ceil((vHeight / ratio).toDouble()).toInt()

            //设置surfaceView的布局参数
            sv_video.layoutParams = RelativeLayout.LayoutParams(vWidth, vHeight)
            //然后开始播放视频
            player.start()
        }
        val max: Int = player.duration
        tv_video_allTime.text = max.toString()
        object : Thread() {
            override fun run() {
                sb_video.max = max

                while (isPlaying) {
                    val currentPosition1 = player.currentPosition
                    val message = Message()
                    message.obj = currentPosition1
                    handler.sendMessage(message)
                    try {
                        Thread.sleep(500)
                    } catch (e: Exception) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                }
            }
        }.start()


    }

    override fun onSeekComplete(p0: MediaPlayer?) {
        println("onSeekComplete")
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {


    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar) {

        println("onStopTrackingTouch======" + p0.progress)
        player?.seekTo(p0.progress)
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {

        println("p1=$p1,p2=$p2,p3=$p3")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {

    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        player?.setDisplay(p0)
        player?.prepareAsync()

    }

    var currentPosition: Int = 0
    var holder: SurfaceHolder? = null
    var player: MediaPlayer? = null
    var currDisplay: Display? = null
    var isShowControl: Boolean = false
    var files: Array<File>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        //默认隐藏视频外围控制键
//        rl_videoTop.visibility = View.GONE
//        rl_videoBottom.visibility = View.GONE
        holder = sv_video.holder
        holder?.addCallback(this)
        holder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        player = MediaPlayer()
        player?.setOnCompletionListener(this)
        player?.setOnErrorListener(this)
        player?.setOnInfoListener(this)
        player?.setOnPreparedListener(this)
        player?.setOnSeekCompleteListener(this)
        player?.setOnVideoSizeChangedListener(this)
        sb_video.setOnSeekBarChangeListener(this)


        //视频当前文件路径
        val path = intent.getStringExtra("VIDEO_PATH")
        player?.setDataSource(path)
        //获取当前position
        val position = intent.getIntExtra("VIDEO_POSITION",0)
        println("position=$position")
        //设置视频名字
        println("测试tv_video_name")
        MainActivity().etISendFile(this)

//
//        val extra = intent.getSerializableExtra("VIDEO_FILES")


        //视频上一个文件路径
//        if (position - 1 < 0) {
//            Toast.makeText(this@VideoActivity, "已经没有文件了", Toast.LENGTH_SHORT).show()
//        } else {
//            val path_previous = files!![position - 1].path
//            player?.setDataSource(path_previous)
//        }
//        //视频下一个文件路径
//        if (position + 1 > files!!.indices as Int) {
//            Toast.makeText(this@VideoActivity, "已经没有文件了", Toast.LENGTH_SHORT).show()
//        } else {
//            val path_next = files!![position + 1].path
//            player?.setDataSource(path_next)
//        }


        currDisplay = windowManager.defaultDisplay

        sv_video.setOnClickListener {
            //当点击视频就显示控制按钮

//            if (isShowControl) {//显示控制按钮
//                rl_videoTop.visibility = View.VISIBLE
//                rl_videoBottom.visibility = View.VISIBLE
//                isShowControl = false
//            } else { //隐藏控制按钮
//                rl_videoTop.visibility = View.INVISIBLE
//                rl_videoBottom.visibility = View.INVISIBLE
//                isShowControl = true
//            }
        }
        //上一个视频
        ll_video_play_previous.setOnClickListener {


        }
        //下一个视频
        ll_video_play_next.setOnClickListener {


        }

        //暂停和播放
        ll_video_play.setOnClickListener {
            if (player!!.isPlaying) {
                player!!.pause()
                iv_playAndPause.setImageResource(R.drawable.btn_play)
            } else {
                player!!.start()
                iv_playAndPause.setImageResource(R.drawable.btn_play_pause)
            }
        }
        //全屏
        ll_video_fullScreen.setOnClickListener {
            rl_videoTop.visibility = View.INVISIBLE
            rl_videoBottom.visibility = View.INVISIBLE
            isShowControl = true
        }
        //退出
        btn_video_back.setOnClickListener {
            isPlaying = false
            finish()
        }
    }


}
