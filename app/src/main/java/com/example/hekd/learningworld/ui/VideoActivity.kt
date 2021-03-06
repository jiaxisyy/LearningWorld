package com.example.hekd.learningworld.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Toast
import com.example.hekd.learningworld.MyApplication
import com.example.hekd.learningworld.R
import com.example.hekd.learningworld.bean.GDVideoInfo
import com.example.hekd.learningworld.bean.greendao.GDVideoInfoDao
import kotlinx.android.synthetic.main.activity_video.*
import kotlinx.android.synthetic.main.dialog_reminder_continue.view.*
import kotlinx.android.synthetic.main.dialog_reminder_error.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class VideoActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, SeekBar.OnSeekBarChangeListener, SurfaceHolder.Callback, MediaPlayer.OnVideoSizeChangedListener{



    override fun onVideoSizeChanged(p0: MediaPlayer?, p1: Int, p2: Int) {
        println("onVideoSizeChanged->p1=$p1" + "p2=$p2")
    }

    /**
     * 完成播放
     */
    override fun onCompletion(p0: MediaPlayer?) {
//        finish()
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

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            when (msg!!.what) {
                WHAT_PROGRESS -> {
                    val i = msg?.obj as Int
                    nowProgress = i
                    sb_video.progress = i
                    val hms = formatter!!.format(i)
                    tv_video_nowTime.text = hms
                }
                WHAT_NOTOUTH -> {


                }

            }

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


    override fun onPrepared(player: MediaPlayer?) {
        // 当prepare完成后，该方法触发，在这里我们播放视频
        isPlaying = true

//        if(position>0){
////            player?.start()
//            player?.seekTo(position)
//        }
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

        }
        if (progresss == 0) {
            //然后开始播放视频
            player.start()
            if (player!!.isPlaying) {
                iv_playAndPause.setImageResource(R.drawable.btn_play_pause)
            } else {
                iv_playAndPause.setImageResource(R.drawable.btn_play)
            }
        }

        val max: Int = player.duration
        val hms = formatter!!.format(max)
        tv_video_allTime.text = hms
        nowMaxProgress = max
        sb_video.max = max
        if (player.isPlaying) {
            iv_playAndPause.setImageResource(R.drawable.btn_play_pause)
        } else {
            iv_playAndPause.setImageResource(R.drawable.btn_play)
        }
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
        println("surfaceDestroyed   is  execute")
        if (player!!.isPlaying) {
            position = player!!.currentPosition
            player!!.stop()
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        player?.setDisplay(p0)
        if (firstInto) {
            try {
                player?.prepareAsync()
                firstInto = false
            } catch (e: Exception) {
                println("=========ERROR==========")
                if (!(dialog_error!!.isShowing)) {
                    dialog_error!!.show()
                }
            }
        }
    }

    var position = 0
    var currentPosition: Int = 0
    var holder: SurfaceHolder? = null
    var player: MediaPlayer? = null
    var currDisplay: Display? = null
    /**视频控制按钮是否显示*/
    var isShowControl: Boolean = false
    var files: Array<File>? = null
    var nowPosition: Int = 0
    var firstInto = true

    var extra: ArrayList<String>? = null
    var gdVideoInfoDao: GDVideoInfoDao? = null
    var isPlaying = true
    var vWidth: Int = 0
    var vHeight: Int = 0
    var formatter: SimpleDateFormat? = null

    var dialog_error: Dialog? = null

    var progresss: Int = 0

    /**数据库当前进度*/
    var dbProgress: Int = 0
    /**数据库总进度*/
    var dbMaxProgress: Int = 0
    /**数据库存储路径*/
    var dbPath: String? = null
    /**当前播放视频路径*/
    var nowPath: String? = null
    /**当前视频播放进度*/
    var nowProgress: Int = 0
    /**当前视频总进度*/
    var nowMaxProgress: Int = 0
    /**无触摸后视频隐藏*/
    val WHAT_NOTOUTH = 10001
    /**开启进度条*/
    val WHAT_PROGRESS = 10002


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        //获取点击视频进度
        progresss = intent.getIntExtra("VIDEO_PROGRESS", 0)
        if (progresss != 0) {
            initDialog()
        }
//        默认隐藏视频外围控制键
        rl_videoTop.visibility = View.GONE
        rl_videoBottom.visibility = View.GONE
        sv_video.setOnClickListener {
            //            当点击视频就显示控制按钮
            if (!isShowControl) { //显示控制按钮
                rl_videoTop.visibility = View.VISIBLE
                rl_videoBottom.visibility = View.VISIBLE
                isShowControl = !isShowControl
            }else{
                rl_videoTop.visibility = View.INVISIBLE
                rl_videoBottom.visibility = View.INVISIBLE
                isShowControl = !isShowControl
            }
        }

        formatter = SimpleDateFormat("HH:mm:ss")
        formatter!!.timeZone = TimeZone.getTimeZone("GMT+00:00")
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
        initErrorDialog()
        dialog_error!!.dismiss()
        try {
            player?.setDataSource(path)
        } catch (e: Exception) {
            println("ERROR")
            dialog_error!!.show()
        }
        nowPath = path
        //获取当前position
        val position = intent.getIntExtra("VIDEO_POSITION", 0)
        nowPosition = position
        println("position=$position")
        var extra = intent.getSerializableExtra("VIDEO_FILES_PATH") as ArrayList<String>
        //设置视频名字
        val indexOf = extra!![position].lastIndexOf("/")
        tv_video_name.text = extra!![position].substring(indexOf + 1)
        currDisplay = windowManager.defaultDisplay
        prepareSeekBar()
        initDB()

        //上一个视频
        ll_video_play_previous.setOnClickListener {
            //视频上一个文件路径
            if (nowPosition == 0) {
                Toast.makeText(this@VideoActivity, getString(R.string.no_file), Toast.LENGTH_SHORT).show()
            } else {

                val path_previous = extra!![nowPosition - 1]
                if (File(path_previous).isDirectory) {
                    Toast.makeText(this@VideoActivity, getString(R.string.no_file), Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        progresss = 0
                        player?.reset()
                        player?.setDataSource(path_previous)
                        player?.prepare()
                        val indexOf = extra!![nowPosition - 1].lastIndexOf("/")
                        tv_video_name.text = extra!![nowPosition - 1].substring(indexOf + 1)
                        nowPosition -= 1
                        //把当前正在播放视频信息存储
                        val query_list = gdVideoInfoDao?.queryBuilder()?.where(GDVideoInfoDao.Properties.Video_path.eq(nowPath))!!.list()
                        if (query_list!!.isEmpty()) {
                            gdVideoInfoDao?.insert(GDVideoInfo(null, nowPath, nowProgress, nowMaxProgress))
                            println("insert data" + nowPath)
                            println("insert data" + nowProgress / nowMaxProgress + "%")
                        } else {
                            val id = query_list[0].id
                            println("id=====================$id")
                            gdVideoInfoDao?.update(GDVideoInfo(id, nowPath, nowProgress, nowMaxProgress))
                            println("update data" + nowPath)
                            println("update data" + nowProgress / nowMaxProgress + "%")
                        }
                        nowPath = path_previous
                    } catch (e: Exception) {
                        println("=========ERROR==========")
                        if (!(dialog_error!!.isShowing)) {
                            dialog_error!!.show()
                        }
                    }
                }
            }
        }
        //下一个视频
        ll_video_play_next.setOnClickListener {
            //视频下一个文件路径
            if (nowPosition == extra.size - 1) {
                Toast.makeText(this@VideoActivity, getString(R.string.no_file), Toast.LENGTH_SHORT).show()
            } else {
                val path_next = extra!![nowPosition + 1]
                if (File(path_next).isDirectory) {
                    Toast.makeText(this@VideoActivity, getString(R.string.no_file), Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        progresss = 0
                        player?.reset()
                        player?.setDataSource(path_next)
                        player?.prepare()
                        val indexOf = extra!![nowPosition + 1].lastIndexOf("/")
                        tv_video_name.text = extra!![nowPosition + 1].substring(indexOf + 1)
                        nowPosition += 1
                        //把当前正在播放视频信息存储
                        val query_list = gdVideoInfoDao?.queryBuilder()?.where(GDVideoInfoDao.Properties.Video_path.eq(nowPath))!!.list()
                        if (query_list!!.isEmpty()) {
                            gdVideoInfoDao?.insert(GDVideoInfo(null, nowPath, nowProgress, nowMaxProgress))
                            println("insert data" + nowPath)
                            println("insert data" + "nowProgress=$nowProgress" + "nowMaxProgress=$nowMaxProgress" + "%")
                        } else {
                            val id = query_list[0].id
                            println("id=====================$id")
                            gdVideoInfoDao?.update(GDVideoInfo(id, nowPath, nowProgress, nowMaxProgress))
                            println("update data" + nowPath)
                            println("update data" + "nowProgress=$nowProgress" + "nowMaxProgress=$nowMaxProgress" + "%")
                        }
                        nowPath = path_next

                    } catch (e: Exception) {
                        println("=========ERROR==========")
                        if (!(dialog_error!!.isShowing)) {
                            dialog_error!!.show()
                        }
                    }

                }


            }
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
            isShowControl = false
        }
        //退出
        btn_video_back.setOnClickListener {
            isPlaying = false

            try {
                //把当前正在播放视频信息存储
                val query_list = gdVideoInfoDao?.queryBuilder()?.where(GDVideoInfoDao.Properties.Video_path.eq(nowPath))!!.list()
                if (query_list!!.isEmpty()) {
                    gdVideoInfoDao?.insert(GDVideoInfo(null, nowPath, nowProgress, nowMaxProgress))
                    println("insert data" + nowPath)
                    println("insert data" + nowProgress / nowMaxProgress + "%")
                } else {
                    val id = query_list[0].id
                    println("id=====================$id")
                    gdVideoInfoDao?.update(GDVideoInfo(id, nowPath, nowProgress, nowMaxProgress))
                    println("update data" + nowPath)
                    println("update data" + nowProgress / nowMaxProgress + "%")
                }
            } catch (e: Exception) {
                println("==========clickBack error===========")
            }
            finish()

        }
    }

    /**
     * 弹窗初始化
     */
    private fun initDialog() {
        Log.d("TAG", "showDialogReminder")
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_reminder_continue, null, false)
        val dialog_Reminder = Dialog(this, R.style.input_dialog)
        dialog_Reminder.setCancelable(false)
        dialog_Reminder.setCanceledOnTouchOutside(false)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        dialog_Reminder.setContentView(view, params)
        if (!isFinishing) {
            dialog_Reminder.show()
        }
        //重新播放
        view.btn_dialog_return.setOnClickListener {
            dialog_Reminder.dismiss()
            player?.seekTo(0)
            iv_playAndPause.setImageResource(R.drawable.btn_play_pause)
            player?.start()

        }
        //继续播放
        view.btn_dialog_continue.setOnClickListener {
            dialog_Reminder.dismiss()
            iv_playAndPause.setImageResource(R.drawable.btn_play_pause)
            player?.seekTo(progresss)
            player?.start()
        }
    }

    /**
     * 初始化数据库
     */
    private fun initDB() {
        gdVideoInfoDao = MyApplication().getSession().gdVideoInfoDao
    }

    /**
     * 弹窗错误弹窗
     */
    private fun initErrorDialog() {
        Log.d("TAG", "showDialogReminder")
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_reminder_error, null, false)
        dialog_error = Dialog(this, R.style.input_dialog)
        dialog_error!!.setCancelable(false)
        dialog_error!!.setCanceledOnTouchOutside(false)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        dialog_error!!.setContentView(view, params)
        if (!isFinishing) {
            dialog_error!!.show()
        }
        dialog_error!!.btn_dialog_sure.setOnClickListener {
            if (dialog_error!!.isShowing) {
                dialog_error!!.dismiss()
            }
            finish()
        }

    }

    /**
     * 进度条开启
     */
    private fun prepareSeekBar() {
        object : Thread() {
            override fun run() {
                while (isPlaying) {
                    val currentPosition1 = player?.currentPosition
                    val message = Message()
                    message.obj = currentPosition1
                    message.what = WHAT_PROGRESS
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

    override fun onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy()
        if (player!!.isPlaying) {
            player!!.stop()
        }
        isPlaying = false
        player!!.release()
        // Activity销毁时停止播放，释放资源。不做这个操作，即使退出还是能听到视频播放的声音
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onRestart() {
        super.onRestart()
//        player!!.reset()
//        player?.start()
////        player?.setDisplay(holder)
    }


}
