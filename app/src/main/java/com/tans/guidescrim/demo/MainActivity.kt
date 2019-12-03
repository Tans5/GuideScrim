package com.tans.guidescrim.demo

import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.tans.guidescrim.R
import com.tans.guidescrim.ScrimView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scrim_view.addHighLightViewIds(R.id.hello_tv to ScrimView.Companion.HighLightDrawerData.RectDrawerData(
            offsets = Rect(10, 10, 10, 10),
            radius = 25f
            ,borderData = ScrimView.Companion.HighLightDrawerData.RectDrawerData.BorderData(color = Color.RED)
        ))
    }
}
