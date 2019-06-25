package com.clg.viewtest.stringutils

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.*
import android.util.Log
import android.view.View


class SpannableUtils {
    companion object {
        //字体颜色
        fun textColor(content: String): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#FF4040"))
            stringBuilder.setSpan(foregroundColorSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return stringBuilder
        }

        //背景颜色
        fun backgroundColor(content: String): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            val backgroundColorSpan = BackgroundColorSpan(Color.parseColor("#FF4040"))
            stringBuilder.setSpan(backgroundColorSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return stringBuilder
        }

        //文本可点击，有点击事件
        fun clickable(content: String): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Log.i("clickable", content)
                }

                override fun updateDrawState(ds: TextPaint) {
                    //去掉可点击文字的下划线
                    ds.isUnderlineText = false
                }
            }
            //文本可点击，有点击事件
            stringBuilder.setSpan(clickableSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            // 设置此方法后，点击事件才能生效
//            textView.setMovementMethod(LinkMovementMethod.getInstance())
            return stringBuilder
        }

        //模糊效果
        fun blurMaskFilter(content: String): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            val filter = BlurMaskFilter(4.0f, BlurMaskFilter.Blur.OUTER)
            val maskFilterSpan = MaskFilterSpan(filter)

            stringBuilder.setSpan(maskFilterSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return stringBuilder
        }

        //弃用线效果
        fun strikethrough(content: String): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            stringBuilder.setSpan(StrikethroughSpan(), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

//            textView.setMovementMethod(LinkMovementMethod.getInstance())

            return stringBuilder

        }

        //下划线效果
        fun underline(content: String): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            val underlineSpan = UnderlineSpan()
            //下划线效果
            stringBuilder.setSpan(underlineSpan, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return stringBuilder

        }

        //字体绝对大小效果
        fun absoluteSize(content: String): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            val ab = AbsoluteSizeSpan(30, true)
            //文本字体绝对的大小
            stringBuilder.setSpan(ab, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return stringBuilder
        }

        //设置图片基于底部
        fun dynamicDrawable(content: String, drawable: Drawable): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            val ab = object : DynamicDrawableSpan() {
                override fun getDrawable(): Drawable {
//                    val drawable = getResources().getDrawable(R.drawable.close)
                    drawable.setBounds(0, 0, 50, 50)
                    return drawable
                }
            }
            stringBuilder.setSpan(ab, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return stringBuilder

        }

        //设置图片
        fun imageSpan(content: String, drawable: Drawable): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
//            val drawable = getResources().getDrawable(R.drawable.close)
            drawable.setBounds(0, 0, 100, 100)
            val ab = ImageSpan(drawable)
            stringBuilder.setSpan(ab, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            return stringBuilder
        }

        //相对字体大小
        fun relativeSize(content: String): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            val ab = RelativeSizeSpan(3.0f)
            stringBuilder.setSpan(ab, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            return stringBuilder
        }

        //基于X的缩放
        fun scaleXSpan(content: String): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            val scaleX = ScaleXSpan(3.0f)
            stringBuilder.setSpan(scaleX,0,5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            return stringBuilder
        }

        //字体样式：粗体、斜体等
        fun styleSpan(content: String): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            val style = StyleSpan(Typeface.BOLD_ITALIC)
            stringBuilder.setSpan(style,0,5,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return stringBuilder
        }
        //上标
        fun subscript (content: String): SpannableStringBuilder {

            val stringBuilder = SpannableStringBuilder(content)
            val ab = SuperscriptSpan()
            stringBuilder.setSpan(ab, 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            return stringBuilder
        }
        //字体、大小、样式和颜色
        fun textAppearance(content: String, context: Context,style: Int): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            //自定义文本样式
            val ab = TextAppearanceSpan(context, style)
            stringBuilder.setSpan(ab, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return stringBuilder

        }
        //文本字体
        fun typeface(content: String,typeface: TypefaceSpan): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            //文字字体
//            val typeface = TypefaceSpan("serif")
            //val typeface = Typeface.createFromAsset(this.getAssets(),
            //            "fonts/char.ttf")
            stringBuilder.setSpan(typeface, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return stringBuilder

        }
        //文本超链接
        fun uRLSpan(content: String): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            val urlSpan = URLSpan("http://www.baidu.com")
            stringBuilder.setSpan(urlSpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            textView.setMovementMethod(LinkMovementMethod())
            return stringBuilder
        }
        //下标
        fun subscriptSpan(content: String): SpannableStringBuilder{
            val stringBuilder = SpannableStringBuilder(content)
            val subscriptSpan = SubscriptSpan()
            stringBuilder.setSpan(subscriptSpan, 3, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            textView.setMovementMethod(LinkMovementMethod())
            return stringBuilder
        }


    }

}
