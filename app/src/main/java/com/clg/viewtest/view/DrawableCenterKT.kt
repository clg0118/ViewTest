package com.clg.viewtest.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class DrawableCenterKT @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        // getCompoundDrawables() : Returns drawables for the left, top, right, and bottom borders.
        val drawable = compoundDrawables
        //得到drawableLeft设置的drawable对象
        val leftDrawable = drawable[0]
        if (leftDrawable != null) {
            //得到leftdrawable 的宽度
            val leftDrawableWidth = leftDrawable.intrinsicWidth
            //得到drawable与text之间的间距
            val drawablePadding = compoundDrawablePadding
            //得到文本的宽度
            val textWidth = paint.measureText(text.toString().trim { it <= ' ' }).toInt()

            val bodyWidth = leftDrawableWidth + drawablePadding + textWidth
            canvas.save()
            //将内容在X轴方向平移
            canvas.translate(((width - bodyWidth) / 2).toFloat(), 0f)
        }

        super.onDraw(canvas)
    }

}
