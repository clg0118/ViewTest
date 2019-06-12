package com.clg.viewtest.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import android.util.Log
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import android.graphics.Shader
import android.graphics.Color.parseColor
import android.graphics.LinearGradient
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import com.clg.viewtest.R


class BarChartView @JvmOverloads constructor(
    private val mContext: Context, @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(mContext, attrs, defStyleAttr) {

    private var mPaintBar: Paint? = null
    private var mPaintLline: Paint? = null
    private var mPaintText: Paint? = null
    //柱状条对应的颜色数组
    private val colors: IntArray
    private val keduTextSpace = 10//刻度与文字之间的间距
    private val keduWidth = 20 //坐标轴上横向标识线宽度
    private val keduSpace = 100 //每个刻度之间的间距 px
    private val itemSpace = 60//柱状条之间的间距
    private val itemWidth = 100//柱状条的宽度
    var lineSpace = 0
    //刻度递增的值
    private var valueSpace = 40
    //绘制柱形图的坐标起点
    private var startX: Int = 0
    private var startY: Int = 0
    private val mTextSize = 25
    private var mMaxTextWidth: Int = 0
    private var mMaxTextHeight: Int = 0
    private var mXMaxTextRect: Rect? = null
    private var mYMaxTextRect: Rect? = null
    //是否要展示柱状条对应的值
    private val isShowValueText = true
    //数据值
    private var mData: MutableList<Int> = ArrayList()
    private var yAxisList: MutableList<Int> = ArrayList()
    private var xAxisList: MutableList<String> = ArrayList()

    init {
        colors = intArrayOf(
            ContextCompat.getColor(mContext, R.color.color_07f2ab),
            ContextCompat.getColor(mContext, R.color.color_79d4d8),
            ContextCompat.getColor(mContext, R.color.color_4388bc),
            ContextCompat.getColor(mContext, R.color.color_07f2ab),
            ContextCompat.getColor(mContext, R.color.color_4388bc)
        )
        init(mContext, false)
    }

    private fun init(context: Context, isUpdate: Boolean) {
//        if (!isUpdate) {
//            initData()
//        }
        //设置边缘特殊效果
        val PaintBGBlur = BlurMaskFilter(
            1f, BlurMaskFilter.Blur.INNER
        )
        //绘制柱状图的画笔
        mPaintBar = Paint()
        mPaintBar!!.setStyle(Paint.Style.FILL)
        mPaintBar!!.setStrokeWidth(4F)
        mPaintBar!!.setMaskFilter(PaintBGBlur)
        //绘制直线的画笔
        mPaintLline = Paint()
        mPaintLline!!.setColor(ContextCompat.getColor(context, R.color.color_274782))
        mPaintLline!!.setAntiAlias(true)
        mPaintLline!!.setStrokeWidth(2F)

        //绘制文字的画笔
        mPaintText = Paint()
        mPaintText!!.setTextSize(mTextSize.toFloat())
        mPaintText!!.setColor(ContextCompat.getColor(context, R.color.color_a9c6d6))
        mPaintText!!.setAntiAlias(true)
        mPaintText!!.setStrokeWidth(1F)

        mYMaxTextRect = Rect()
        mXMaxTextRect = Rect()
        if (yAxisList.size > 0) {
            mPaintText!!.getTextBounds(
                Integer.toString(yAxisList[yAxisList.size - 1]),
                0,
                Integer.toString(yAxisList[yAxisList.size - 1]).length,
                mYMaxTextRect
            )
        }
        if (xAxisList.size > 0) {
            mPaintText!!.getTextBounds(
                xAxisList[xAxisList.size - 1],
                0,
                xAxisList[xAxisList.size - 1].length,
                mXMaxTextRect
            )
        }
        //绘制的刻度文字的最大值所占的宽高
        mMaxTextWidth =
            if (mYMaxTextRect!!.width() > mXMaxTextRect!!.width()) mYMaxTextRect!!.width() else mXMaxTextRect!!.width()
        mMaxTextHeight =
            if (mYMaxTextRect!!.height() > mXMaxTextRect!!.height()) mYMaxTextRect!!.height() else mXMaxTextRect!!.height()

        if (yAxisList.size >= 2) {
            valueSpace = yAxisList[1] - yAxisList[0]
        }

        //坐标原点 y轴起点
        startY = keduSpace * (yAxisList.size - 1) + mMaxTextHeight + if (isShowValueText) keduTextSpace else 0

    }

    /**
     * 初始化数据
     */
    private fun initData() {
        val data = intArrayOf(80, 160, 30, 40, 100)
        for (i in 0..4) {
            mData.add(data[i])
            yAxisList.add(0 + i * valueSpace)
        }
        val xAxis = arrayOf("1月", "2月", "3月", "4月", "5月")
        for (i in mData.indices) {
            xAxisList.add(xAxis[i])
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d("TAG", "onMeasure()")
//        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
//        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
//        if (heightMode == MeasureSpec.AT_MOST) {
//            if (keduWidth > mMaxTextHeight + keduTextSpace) {
//                heightSize = (yAxisList.size - 1) * keduSpace + keduWidth + mMaxTextHeight
//            } else {
//                heightSize = (yAxisList.size - 1) * keduSpace + (mMaxTextHeight + keduTextSpace) + mMaxTextHeight
//            }
//            heightSize =
//                heightSize + keduTextSpace + if (isShowValueText) keduTextSpace else 0//x轴刻度对应的文字距离底部的padding:keduTextSpace
//        }
//        if (widthMode == MeasureSpec.AT_MOST) {
//            widthSize = startX + mData.size * itemWidth + (mData.size + 1) * itemSpace
//        }
        Log.d("TAG", "heightSize=" + heightSize + "widthSize=" + widthSize)
        //保存测量结果
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("TAG", "onDraw()")

        //从下往上绘制Y 轴
//        canvas.drawLine(
//            startX.toFloat(),
//            (startY + keduWidth).toFloat(), startX.toFloat(),
//            (startY - (yAxisList.size - 1) * keduSpace).toFloat(), mPaintLline
//        )
        for (i in yAxisList.indices) {

            //绘制Y轴的文字
//            val textRect = Rect()
//            mPaintText!!.getTextBounds(
//                Integer.toString(yAxisList[i]),
//                0,
//                Integer.toString(yAxisList[i]).length,
//                textRect
//            )
//            canvas.drawText(
//                Integer.toString(yAxisList[i]),
//                (startX - keduWidth - textRect.width() - keduTextSpace).toFloat(),
//                (startY - (i + 1) * keduSpace + keduSpace).toFloat(),
//                mPaintText
//            )
//画X轴及上方横向的刻度线
//            canvas.drawLine(
//                (startX - keduWidth).toFloat(),
//                (startY - keduSpace * i).toFloat(),
//                (startX + mData.size * itemWidth + itemSpace * (mData.size + 1)).toFloat(),
//                (startY - keduSpace * i).toFloat(),
//                mPaintLline!!
//            )

        }
//画X轴及上方横向的刻度线
        canvas.drawLine(
            (startX).toFloat(),
            (startY).toFloat(),
            (startX + width).toFloat(),
            (startY).toFloat(),
            mPaintLline
        )
        if (xAxisList.size > 0){
            lineSpace = (width - 26) / xAxisList.size
        }
        for (j in xAxisList.indices) {
            //绘制X轴的文字
            val rect = Rect()
            mPaintText!!.getTextBounds(xAxisList[j], 0, xAxisList[j].length, rect)
            canvas.drawText(
                xAxisList[j],
                (startX + itemSpace / 2 + j * (itemSpace  + itemWidth) - rect.width() / 2).toFloat(),
                (startY + rect.height() + keduTextSpace).toFloat(),
                mPaintText
            )

            canvas.drawLine(
                (startX + lineSpace / 2 + j * lineSpace ).toFloat(),
                (startY - 2).toFloat(),
                (startX + lineSpace / 2 + j * lineSpace ).toFloat(),
                (startY - 20).toFloat(),
                mPaintLline
            )

            if (isShowValueText) {
                val rectText = Rect()
                mPaintText!!.getTextBounds(mData[j].toString() + "", 0, (mData[j].toString() + "").length, rectText)
                //绘制柱状条上的值
                canvas.drawText(
                    mData[j].toString() + "",
                    (startX + itemSpace * (j + 1) + itemWidth * j + itemWidth / 2 - rectText.width() / 2).toFloat(),
                    (startY - keduTextSpace - mData[j] * (keduSpace / valueSpace)).toFloat(),
                    mPaintText
                )
            }
            //绘制柱状条
//            mPaintBar!!.setColor(colors[j])
            //(mData.get(j) * (keduSpace * 1.0 / valueSpace))：为每个柱状条所占的高度值px

            val initx = startX + itemSpace * (j + 1) + j * itemWidth
            val shader = LinearGradient(
                initx.toFloat(),
                (startY - mData[j] * (keduSpace / valueSpace)).toFloat(),
                (initx + itemWidth).toFloat(),
                startY.toFloat(),
                parseColor("#87FAD9"),
                parseColor("#FF6633"),
                Shader.TileMode.CLAMP
            )



            mPaintBar!!.setShader(shader)
            val r2 = RectF()
            r2.top = (startY - keduTextSpace - mData[j] * (keduSpace / valueSpace)).toFloat()
            r2.bottom = startY.toFloat()
            r2.left = initx.toFloat()
            r2.right = (initx + itemWidth).toFloat()

            val r3 = Rect()
            r3.top = startY - mData[j] * (keduSpace / valueSpace) - keduTextSpace
            r3.bottom = startY
            r3.left = initx
            r3.right = (initx + itemWidth)

            val drawable = resources.getDrawable(R.drawable.shape)
            drawable.setBounds(r3)
//            drawable.draw(canvas)
//            canvas.drawRoundRect(
//                r2,
//                10f,
//                10f,
//                mPaintBar
//            )
        }

        canvas.drawLine(
            (startX + lineSpace / 2 + xAxisList.size * lineSpace ).toFloat(),
            (startY - 2).toFloat(),
            (startX + lineSpace / 2 + xAxisList.size * lineSpace ).toFloat(),
            (startY - 20).toFloat(),
            mPaintLline
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.getAction()) {
            MotionEvent.ACTION_DOWN ->

                return true
            MotionEvent.ACTION_MOVE -> {

                this.invalidate()
                return true
            }
            MotionEvent.ACTION_UP ->


                return true
        }
        return super.onTouchEvent(event)
    }

    /**
     * 根据真实的数据刷新界面
     *
     * @param datas
     * @param xList
     * @param yList
     */
    fun updateValueData(@NotNull datas: MutableList<Int>, @NotNull xList: MutableList<String>, @NotNull yList: MutableList<Int>) {
        this.mData = datas
        this.xAxisList = xList
        this.yAxisList = yList
        init(mContext, true)
        invalidate()
    }
}