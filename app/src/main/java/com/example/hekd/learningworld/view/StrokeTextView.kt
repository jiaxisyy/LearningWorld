package com.example.hekd.learningworld.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import com.example.hekd.learningworld.util.SdCardUtil
import com.google.android.exoplayer.util.Util

/**
 * Created by hekd on 2017/12/4.
 */
class StrokeTextView : TextView {
    var mTextView: TextView? = null
    var mContext: Context? = null
    val paint = Paint()
    /**字体大小*/
    val mTextSize = 22
    /**描边大小*/
    val mStrokeWidth = 4

    constructor(context: Context?) : super(context) {
        mContext = context
        mTextView = TextView(context)
        init()
    }


    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        mTextView = TextView(context, attrs)
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mContext = context
        mTextView = TextView(context, attrs, defStyleAttr)
        init()
    }


    private fun init() {
        val textPaint = mTextView!!.paint
        textPaint.strokeWidth = mStrokeWidth.toFloat()
        textPaint.style = Paint.Style.STROKE
        //描边颜色
        mTextView!!.setTextColor(Color.WHITE)
        val fl = SdCardUtil.px2sp(mContext, textPaint.textSize).toFloat()
        mTextView!!.textSize=mTextSize.toFloat()
        mTextView!!.typeface = Typeface.createFromAsset(mContext!!.assets, "fonts/mini.TTF")
        mTextView!!.gravity = gravity

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        //设置轮廓文字
        val charSequence = mTextView!!.text
        if (charSequence != this.text || charSequence == null) {
            mTextView!!.text = text
            this.postInvalidate()
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mTextView!!.measure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mTextView!!.layout(left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        mTextView!!.draw(canvas)
        super.onDraw(canvas)
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        mTextView!!.layoutParams = params
    }

}