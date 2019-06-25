package com.clg.viewtest

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.clg.viewtest.stringutils.SpannableUtils
import com.clg.viewtest.view.Book1ChartView

class MainActivity : AppCompatActivity() {

    lateinit var barchart: Book1ChartView
    lateinit var button: Button
    lateinit var tv_02: TextView
    var mTimeList = mutableListOf<String>("00:00","06:00","09:00","12:00","15:00","18:00","21:00","24:00")
    var mPriceList = mutableListOf<String>("6800", "4100", "", "3600", "7800","12800","9100")
    var mBarMapList = mutableListOf<Map<String,Int>>()
    var mBarList = mutableListOf<Int>(30*2,40*2,0,10*2,40*2,60*2,50*2)
    val content = "abcdefghijklmnopqrstuvdxyz"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        barchart = findViewById(R.id.barchart)
        tv_02 = findViewById(R.id.tv_02)
        button = findViewById(R.id.button)
//        barchart.background = getDrawable(R.drawable.shape2)
        barchart.updateValueData(timeList = mTimeList, priceList = mPriceList, barHightList = mBarList)
        button.setOnClickListener {

        }
        tv_02.movementMethod = LinkMovementMethod.getInstance()
//        val drawable = getResources().getDrawable(R.drawable.close)
//        tv_02.text = SpannableUtils.imageSpan(content,drawable)
//        val charTypeface = Typeface.createFromAsset(this.assets,"fonts/char.ttf")
//        val typeface = TypefaceSpan("serif")
        tv_02.text = SpannableUtils.subscriptSpan(content)
//        tv_02.text = SpannableUtils.textAppearance(content,this,R.style.text)
    }
}
