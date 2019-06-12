package com.clg.viewtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.clg.viewtest.view.BarChartView
import com.clg.viewtest.view.BarChartViewJava
import com.clg.viewtest.view.BarChartViewKT
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var barchart: BarChartView
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        barchart = findViewById(R.id.barchart)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            var datas = mutableListOf<Int>(40, 76, 90, 50, 100,23)
            var xList = mutableListOf<String>("1月份", "2月份", "3月份", "4月份", "5月份","6月份")

            //根据数据的最大值生成上下对应的Y轴坐标范围
            var ylist = mutableListOf<Int>()
            var maxYAxis: Int? = Collections.max(datas)
            if (maxYAxis!! % 2 == 0) {
                maxYAxis = maxYAxis + 2
            } else {
                maxYAxis = maxYAxis + 1
            }
            var keduSpace = (maxYAxis / datas.size) + 1
            for (i in 0..datas.size) {
                ylist.add(keduSpace * i)
            }
            barchart.updateValueData(datas, xList, ylist)
        }

    }
}
