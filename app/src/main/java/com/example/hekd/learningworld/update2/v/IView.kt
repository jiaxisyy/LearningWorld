package com.example.hekd.learningworld.update2.v

/**
 * Created by hekd on 2017/11/28.
 */
interface IView {

    fun videoStart()
    fun videoStop()
    fun videoPause()
    /**
     * 隐藏控制按钮
     */
    fun hideControl()

    /**
     * 显示控制按钮
     */
    fun showControl()

    /**
     *全屏
     */
    fun fullScreen()


    /**
     * 下一个
     */
    fun nextVideo()

    /**
     * 上一个
     */
    fun previousVideo()
}