package com.clg.viewtest.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import android.util.Log
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
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
    private val marginSpace = 60 // View 左右边距
    private val barToLinSpace = 15 // 柱状条到竖线的间距
    var lineSpace = 0
    //绘制柱形图的坐标起点
    private var startX: Int = 0
    private var startY: Int = 0
    private val mTextSize = 25
    private var mMaxTextHeight: Int = 0
    private var mXMaxTextRect: Rect? = null
    private var mYMaxTextRect: Rect? = null
    //是否要展示柱状条对应的值
    private val isShowValueText = true
    //数据值
    private var mData: MutableList<Int> = ArrayList()
    private var yAxisList: MutableList<Int> = ArrayList()
    private var xAxisList: MutableList<String> = ArrayList()
    var lastTouchX = 0f
    var selectIndex = -1

    private var itemClickListener: onItemClickListener? = null


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

        var lastTouchX = 0f

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

        mMaxTextHeight =
            if (mYMaxTextRect!!.height() > mXMaxTextRect!!.height()) mYMaxTextRect!!.height() else mXMaxTextRect!!.height()


        //坐标原点 y轴起点
        startY = 120 + mMaxTextHeight + keduTextSpace + 8

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        Log.d("TAG", "heightSize=" + heightSize + "widthSize=" + widthSize)
        //保存测量结果
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//      画X轴及上方横向的刻度线

        if (xAxisList.size > 0) {

            canvas.drawLine(
                (startX).toFloat(),
                (startY).toFloat(),
                (startX + width).toFloat(),
                (startY).toFloat(),
                mPaintLline
            )

            lineSpace = (width - marginSpace * 2) / xAxisList.size
            canvas.drawLine(
                (startX + marginSpace + xAxisList.size * lineSpace).toFloat(),
                (startY).toFloat(),
                (startX + marginSpace + xAxisList.size * lineSpace).toFloat(),
                (startY - 20).toFloat(),
                mPaintLline
            )
        }
        for (j in xAxisList.indices) {
            //绘制X轴的文字
            val rect = Rect()
            mPaintText!!.getTextBounds(xAxisList[j], 0, xAxisList[j].length, rect)
            canvas.drawText(
                xAxisList[j],
                (startX + marginSpace + j * lineSpace - rect.width() / 2).toFloat(),
                (startY + rect.height() + keduTextSpace).toFloat(),
                mPaintText
            )

            canvas.drawLine(
                (startX + marginSpace + j * lineSpace).toFloat(),
                (startY).toFloat(),
                (startX + marginSpace + j * lineSpace).toFloat(),
                (startY - 20).toFloat(),
                mPaintLline
            )

            //绘制柱状条
            val initx = startX + marginSpace + j * lineSpace + barToLinSpace
            val inity = startX + marginSpace + (j + 1) * lineSpace - barToLinSpace
            if (mData[j] == 0) {
                val rectText = Rect()
                mPaintText!!.getTextBounds("无航班", 0, 3, rectText)
                //绘制柱状条上的值
                canvas.drawText(
                    "无航班",
                    (startX + initx + (inity - initx) / 2 - rectText.width() / 2).toFloat(),
                    (startY - keduTextSpace).toFloat(),
                    mPaintText
                )

            } else {
                val rectText = Rect()
                mPaintText!!.getTextBounds(mData[j].toString() + "", 0, (mData[j].toString() + "").length, rectText)
                //绘制柱状条上的值
                canvas.drawText(
                    mData[j].toString() + "",
                    (startX + initx + (inity - initx) / 2 - rectText.width() / 2).toFloat(),
                    (startY - keduTextSpace - (mData[j] / 100.0) * 120 - 4).toFloat(),
                    mPaintText
                )

                val r3 = Rect()
                r3.top = (startY - (mData[j] / 100.0) * 120 - keduTextSpace).toInt()
                Log.i("sssss", ((mData[j] / 100.0) * 120).toString())
                r3.bottom = startY
                r3.left = initx
                r3.right = inity
                var drawable: Drawable
                if (selectIndex == j) {
                    drawable = resources.getDrawable(R.drawable.shape1)
                } else {
                    drawable = resources.getDrawable(R.drawable.shape)
                }
                drawable.setBounds(r3)
                drawable.draw(canvas)
            }

        }


    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.getAction()) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                var isNoFlight = false
                if (mData.size > 0) {
                    for (j in mData.indices) {
                        val initx = startX + marginSpace + j * lineSpace + barToLinSpace
                        val inity = startX + marginSpace + (j + 1) * lineSpace - barToLinSpace
                        if (lastTouchX <= inity && lastTouchX >= initx) {
                            selectIndex = j
                            isNoFlight = mData[j] == 0
                            itemClickListener?.onItemClickListener(selectIndex)
                            break
                        }
                    }
                }
                if (!isNoFlight){
                    this.invalidate()
                }
                return true
            }
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

    // 提供set方法
    fun setOnItemClickListener(itemClickListener: onItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    //自定义接口
    interface onItemClickListener {
        fun onItemClickListener(position: Int)
    }

}