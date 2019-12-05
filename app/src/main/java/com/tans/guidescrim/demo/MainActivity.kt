package com.tans.guidescrim.demo

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.tans.guidescrim.R
import com.tans.guidescrim.ScrimView
import kotlinx.android.synthetic.main.activity_main.*
import com.tans.guidescrim.ScrimView.Companion.HighLightDrawerData
import com.tans.guidescrim.bottomOfViewPoint
import com.tans.guidescrim.dialogs.toSimpleContainerGuideScrimDialog
import com.tans.guidescrim.dialogs.toSimpleGuideDialog
import com.tans.guidescrim.getViewScreenLocationRect
import com.tans.guidescrim.guidescrims.*

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
            .highLightViewIds(arrayOf(R.id.combine_dialog_bt to HighLightDrawerData.RectDrawerData()))
            .build()

        val simpleScrim3 = SimpleGuideScrim.Companion.Builder()
            .highLightViewIds(arrayOf(R.id.scrim_event_dialog_bt to HighLightDrawerData.RectDrawerData()))
            .build()

        val containerGuideScrim = SimpleContainerGuideScrim.Companion.Builder()
            .childrenLayoutIdsAndPosition {
                mapOf(R.layout.layout_test_container_child to bottomOfViewPoint(combine_dialog_bt))
            }
            .build()

        val guideScrim = simpleScrim1 + simpleScrim2 + simpleScrim3 + containerGuideScrim

        val combineDialog = guideScrim
            .withClickEvent(containerClickData = mapOf(
                R.id.container_rl to { id ->
                    Toast.makeText(this, "Hello,World", Toast.LENGTH_SHORT).show()
                    true })
            )
            .toSimpleContainerGuideScrimDialog(this)

        val scrimDialog = simpleScrim1.withClickEvent(
            scrimClickData = { mapOf(1 to Pair(getViewScreenLocationRect(hello_tv), { id -> Toast.makeText(this, "Hello,World", Toast.LENGTH_SHORT).show(); true })) }
        ).toSimpleGuideDialog(this)

        scrim_event_dialog_bt.setOnClickListener {
            scrimDialog.show()
        }

        combine_dialog_bt.setOnClickListener {
            combineDialog.show()
        }

        step_activity_bt.setOnClickListener {
            startActivity(Intent(this, StepActivity::class.java))
        }

    }
}
