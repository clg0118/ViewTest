package com.clg.viewtest.view

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.clg.viewtest.R

class Book1ChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    //底线画笔
    lateinit var mPaintLine: Paint
    //时间文字画笔
    lateinit var mPaintTime: Paint
    //柱状图画笔
    lateinit var mPaintBar: Paint
    //竖线画笔
    lateinit var mPaintVerticalLine: Paint
    //时间文字字体大小
    private val mTextSizeTime = 20f
    //价格文字字体大小
    private val mTextSizePrice = 24f
    //时间文字 最大宽高
    lateinit var mTimeMaxRect: Rect
    //价格文字 最大宽高
    lateinit var mPriceMaxRect: Rect

    private var mTimeList: MutableList<String> = ArrayList()
    private var mPiceList: MutableList<String> = ArrayList()

    init {
        
    }

    private fun init(context: Context, isUpdate: Boolean) {
        when(isUpdate){
            true -> {

            }

        }

        //设置边缘特殊效果
        val paintBGBlur = BlurMaskFilter(
            1f, BlurMaskFilter.Blur.INNER
        )
        //绘制柱状图的画笔
        mPaintBar = Paint()
        mPaintBar.style = Paint.Style.FILL
        mPaintBar.strokeWidth = 4F
        mPaintBar.maskFilter = paintBGBlur

        //绘制底线画笔
        mPaintLine = Paint()
        mPaintLine.color = ContextCompat.getColor(context, R.color.color_274782)
        mPaintLine.isAntiAlias = true
        mPaintLine.strokeWidth = 2F

        //绘制时间文字的画笔
        mPaintTime = Paint()
        mPaintTime.textSize = mTextSizeTime
        mPaintTime.color = ContextCompat.getColor(context, R.color.color_a9c6d6)
        mPaintTime.isAntiAlias = true
        mPaintTime.strokeWidth = 1F

        mTimeMaxRect = Rect()
        mPriceMaxRect = Rect()



    }

}