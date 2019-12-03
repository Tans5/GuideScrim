package com.tans.guidescrim.demo

import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.tans.guidescrim.R
import com.tans.guidescrim.ScrimView
import com.tans.guidescrim.guidescrims.SimpleGuideScrim
import com.tans.guidescrim.guidescrims.toSimpleGuideDialog
import kotlinx.android.synthetic.main.activity_main.*
import com.tans.guidescrim.guidescrims.plus
import com.tans.guidescrim.ScrimView.Companion.HighLightDrawerData

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val simpleScrim1 = SimpleGuideScrim.Companion.Builder()
            .scrimColor(ContextCompat.getColor(this, R.color.common_scrim_color))
            .highLightViewIds(arrayOf(R.id.hello_tv to HighLightDrawerData.RectDrawerData(
                offsets = Rect(30, 30, 30, 30),
                radius = 20f,
                borderData = ScrimView.Companion.HighLightDrawerData.RectDrawerData.BorderData(Color.BLACK, width = 10)
            )))
            .viewGetter { id -> findViewById(id) }
            .build()

        val simpleScrim2 = SimpleGuideScrim.Companion.Builder()
            .highLightViewIds(arrayOf(R.id.app_name_tv2 to HighLightDrawerData.RectDrawerData()))
            .build()

        val simpleScrim3 = SimpleGuideScrim.Companion.Builder()
            .highLightViewIds(arrayOf(R.id.app_name_tv to HighLightDrawerData.RectDrawerData()))
            .build()

        val guideScrim = simpleScrim1 + simpleScrim2 + simpleScrim3

        val dialog = guideScrim.toSimpleGuideDialog(this)
        root_view.setOnClickListener {
            dialog.show()
        }
    }
}
