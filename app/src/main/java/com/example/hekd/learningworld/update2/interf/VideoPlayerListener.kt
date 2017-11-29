package com.example.hekd.learningworld.update2.interf

import tv.danmaku.ijk.media.player.IMediaPlayer

/**
 * Created by hekd on 2017/11/29.
 */
abstract class VideoPlayerListener:IMediaPlayer.OnBufferingUpdateListener,IMediaPlayer.OnCompletionListener,IMediaPlayer.OnPreparedListener,IMediaPlayer.OnInfoListener,IMediaPlayer.OnVideoSizeChangedListener,IMediaPlayer.OnErrorListener,IMediaPlayer.OnSeekCompleteListener {
}