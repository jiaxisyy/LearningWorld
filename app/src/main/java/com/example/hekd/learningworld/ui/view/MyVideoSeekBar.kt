package com.example.hekd.learningworld.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.SeekBar
import com.example.hekd.learningworld.R


/**
 * Created by hekd on 2017/10/16.
 */
class MyVideoSeekBar : SeekBar {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.MSeekBar, defStyleAttr, 0)
        val count: Int = ta.indexCount

//        for (i in 0..count) {
//            val index = ta.getIndex(i)
//            when (index) {
//                R.styleable.MSeekBar_textColor -> mTextColor = ta.getColor(index, Color.WHITE)
//            }
//
//        }
        ta.recycle()
        //设置画笔
        val mPaint = Paint()
        mPaint.isAntiAlias = true

    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//
//
//        invalidate()
//
//        return super.onTouchEvent(event)
//    }

}