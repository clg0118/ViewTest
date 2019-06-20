package com.clg.viewtest.stringutils

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan

class SpannableUtils {
    companion object{
        fun textColor(content: String): SpannableStringBuilder {
            val stringBuilder = SpannableStringBuilder(content)
            val foregroundColorSpan = ForegroundColorSpan(Color.parseColor("#FF4040"))
            stringBuilder.setSpan(foregroundColorSpan, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return stringBuilder
        }



    }

}
