package com.clg.viewtest.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.clg.viewtest.R
import org.jetbrains.annotations.NotNull

class Book1ChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    //底线画笔
    lateinit var mPaintLine: Paint
    //时间文字画笔
    lateinit var mPaintTime: Paint
    //柱状图画笔
    lateinit var mPaintBar: Paint
    //价格文字画笔
    lateinit var mPaintPrice: Paint
    //竖线画笔
    lateinit var mPaintVerticalLine: Paint
    // 无航班画笔
    lateinit var mPaintNoFlight: Paint
    //时间文字字体大小
    private val mTextSizeTime = 20f
    //价格文字字体大小
    private val mTextSizePrice = 24f
    //无航班字体大小
    private val mTextSizeNoFlight = 24f
    //时间文字 最大宽高
    lateinit var mTimeMaxRect: Rect
    //价格文字 最大宽高
    lateinit var mPriceMaxRect: Rect
    //时间文字底线之间的间距
    private val timeTextSpace = 10
    //柱状条之间的间距
    private var itemSpace: Int = 0
    //柱状图高度集合
    private var mBarHightList: MutableList<Int> = ArrayList()
    //时间集合
    private var mTimeList: MutableList<String> = ArrayList()
    //价格集合
    private var mPriceList: MutableList<String> = ArrayList()
    //柱状条点击回调
    private var itemClickListener: onItemClickListener? = null
    // View 左右边距
    private val marginSpace = 60
    // 柱状条到竖线的间距
    private val barToLinSpace = 15
    //竖线之间的间距
    private var lineSpace = 0
    //最后一次点击坐标点
    var lastTouchX = 0f
    //点击的item的角标
    var selectIndex = -1
    //time文字高度
    private var timeTextHeight: Int = 0
    //绘制柱形图的坐标起点
    private var startX: Int = 0
    private var startY: Int = 0
    //头部文字画笔
    lateinit var mPaintTitle: Paint
    //头部文字
    val titleStr = "点击筛选合适的航班起飞时间"
    //头部文字字体大小
    private val mTextSizeTitle = 12f

    //头部文字 最大宽高
    lateinit var mTitleMaxRect: Rect
    //title文字高度
    private var tiTextHeight: Int = 0

    init {
        init(context)
    }

    private fun init(context: Context) {

        //设置边缘特殊效果
        val paintBGBlur = BlurMaskFilter(
            1f, BlurMaskFilter.Blur.INNER
        )

        //初始化绘制头部文字的画笔
        mPaintTitle = Paint()
        mPaintTitle.textSize = mTextSizeTitle
        mPaintTitle.color = ContextCompat.getColor(context, R.color.iflight_chart_time)
        mPaintTitle.isAntiAlias = true
        mPaintTitle.strokeWidth = 1F

        //绘制柱状图的画笔
        mPaintBar = Paint()
        mPaintBar.style = Paint.Style.FILL
        mPaintBar.strokeWidth = 4F
        mPaintBar.maskFilter = paintBGBlur

        //绘制底线画笔
        mPaintLine = Paint()
        mPaintLine.color = ContextCompat.getColor(context, R.color.iflight_chart_line)
        mPaintLine.isAntiAlias = true
        mPaintLine.strokeWidth = 2F

        //绘制时间文字的画笔
        mPaintTime = Paint()
        mPaintTime.textSize = mTextSizeTime
        mPaintTime.color = ContextCompat.getColor(context, R.color.iflight_chart_time)
        mPaintTime.isAntiAlias = true
        mPaintTime.strokeWidth = 1F

        //绘制价格文字的画笔
        mPaintPrice = Paint()
        mPaintPrice.textSize = mTextSizePrice
        mPaintPrice.color = ContextCompat.getColor(context, R.color.iflight_chart_price)
        mPaintPrice.isAntiAlias = true
        mPaintPrice.strokeWidth = 1F

        //绘制竖线的画笔
        mPaintVerticalLine = Paint()
        mPaintVerticalLine.color = ContextCompat.getColor(context, R.color.iflight_chart_line)
        mPaintVerticalLine.isAntiAlias = true
        mPaintVerticalLine.strokeWidth = 2F

        //无航班画笔
        mPaintNoFlight = Paint()
        mPaintNoFlight.textSize = mTextSizePrice
        mPaintNoFlight.color = ContextCompat.getColor(context, R.color.iflight_chart_line)
        mPaintNoFlight.isAntiAlias = true
        mPaintNoFlight.strokeWidth = 1F

        mTimeMaxRect = Rect()
        mPriceMaxRect = Rect()
        mTitleMaxRect = Rect()
        mPaintTitle.getTextBounds(
            titleStr,
            0,
            titleStr.length,
            mTitleMaxRect
        )

        //绘制时间文字的最大值所占的宽高
        tiTextHeight = mTitleMaxRect.height()
        if (mTimeList.size > 0) {
            mPaintTime.getTextBounds(
                mTimeList[mTimeList.size - 1],
                0,
                mTimeList[mTimeList.size - 1].length,
                mTimeMaxRect
            )
        }
        if (mPriceList.size > 0) {
            mPaintPrice.getTextBounds(
                mPriceList[mPriceList.size - 1],
                0,
                mPriceList[mPriceList.size - 1].length,
                mPriceMaxRect
            )
        }
        //绘制的时间文字的最大值所占的宽高
        timeTextHeight = mTimeMaxRect.height()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        //保存测量结果
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
//坐标原点 y轴起点
        startY = height - (timeTextHeight + timeTextSpace + 8)
        if (mTimeList.size > 0 && mPriceList.size > 0) {
            //绘制背景
            val bgRect = Rect()
            bgRect.top = 0
            bgRect.bottom = height - (timeTextHeight + timeTextSpace + 8)
            bgRect.left = startX
            bgRect.right = startX + width
            val bg = resources.getDrawable(R.drawable.shape2)
            bg.bounds = bgRect
            bg.draw(canvas)




            canvas.drawLine(
                (startX).toFloat(),
                (startY).toFloat(),
                (startX + width).toFloat(),
                (startY).toFloat(),
                mPaintLine
            )
//            canvas.drawLine(
//                    (startX + marginSpace + mTimeList.size * lineSpace).toFloat(),
//                    (startY).toFloat(),
//                    (startX + marginSpace + mTimeList.size * lineSpace).toFloat(),
//                    (startY - 20).toFloat(),
//                    mPaintVerticalLine
//            )
            //获取分割线间距
            lineSpace = (width - marginSpace * 2) / (mTimeList.size - 1)
            for (j in mTimeList.indices) {
                //绘制时间段的文字
                val rect = Rect()
                mPaintTime.getTextBounds(mTimeList[j], 0, mTimeList[j].length, rect)
                canvas.drawText(
                    mTimeList[j],
                    (startX + marginSpace + j * lineSpace - rect.width() / 2).toFloat(),
                    (startY + rect.height() + timeTextSpace).toFloat(),
                    mPaintTime
                )
                //绘制竖线
                canvas.drawLine(
                    (startX + marginSpace + j * lineSpace).toFloat(),
                    (startY).toFloat(),
                    (startX + marginSpace + j * lineSpace).toFloat(),
                    (startY - 20).toFloat(),
                    mPaintVerticalLine
                )
            }

            //绘制柱状条
            for (j in mPriceList.indices) {
                val leftx = startX + marginSpace + j * lineSpace + barToLinSpace
                val rightx = startX + marginSpace + (j + 1) * lineSpace - barToLinSpace
                if (TextUtils.isEmpty(mPriceList[j])) {
                    val rectText = Rect()
                    mPaintNoFlight.getTextBounds("无航班", 0, 3, rectText)
                    //绘制柱状条上的值
                    canvas.drawText(
                        "无航班",
                        (startX + leftx + (rightx - leftx) / 2 - rectText.width() / 2).toFloat(),
                        (startY - timeTextSpace).toFloat(),
                        mPaintNoFlight
                    )

                } else {
                    val rectText = Rect()
                    mPaintPrice.getTextBounds(mPriceList[j] + "", 0, (mPriceList[j] + "").length, rectText)
                    //绘制柱状条上的值
                    canvas.drawText(
                        mPriceList[j],
                        (startX + leftx + (rightx - leftx) / 2 - rectText.width() / 2).toFloat(),
                        (startY - timeTextSpace - mBarHightList[j] - 4).toFloat(),
                        mPaintPrice
                    )
                    // 绘制渐变色柱状条
                    val barRect = Rect()
                    barRect.top = (startY - mBarHightList[j] - timeTextSpace).toInt()
                    barRect.bottom = startY
                    barRect.left = leftx
                    barRect.right = rightx
                    val drawable: Drawable = if (selectIndex == j) {
                        resources.getDrawable(R.drawable.shape1)
                    } else {
                        resources.getDrawable(R.drawable.shape)
                    }
                    drawable.bounds = barRect
                    drawable.draw(canvas)

                }
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                var isNoFlight = false
                if (mPriceList.size > 0) {
                    for (j in mPriceList.indices) {
                        val leftx = startX + marginSpace + j * lineSpace + barToLinSpace
                        val rightx = startX + marginSpace + (j + 1) * lineSpace - barToLinSpace
                        if (lastTouchX <= rightx && lastTouchX >= leftx) {
                            selectIndex = j
                            isNoFlight = TextUtils.isEmpty(mPriceList[j])
                            itemClickListener?.onItemClickListener(selectIndex)
                            break
                        }
                    }
                }
                if (!isNoFlight) {
                    this.invalidate()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 根据数据刷新界面
     * timeList = 时间段集合
     *
     *
     */
    fun updateValueData(@NotNull timeList: MutableList<String>, @NotNull priceList: MutableList<String>, @NotNull barHightList: MutableList<Int>) {
        this.mTimeList = timeList
        this.mPriceList = priceList
        this.mBarHightList = barHightList
        init(context)
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