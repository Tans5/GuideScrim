package com.tans.guidescrim.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tans.guidescrim.R
import com.tans.guidescrim.guidescrims.SimpleContainerGuideScrim
import com.tans.guidescrim.ScrimView.Companion.HighLightDrawerData
import com.tans.guidescrim.dialogs.toSimpleContainerGuideScrimDialog
import com.tans.guidescrim.guidescrims.ChainGuideEvents
import com.tans.guidescrim.guidescrims.SimpleChainGuideScrim
import kotlinx.android.synthetic.main.activity_step.*

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-05
 */
class StepActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step)
        val stepScrim1 = SimpleContainerGuideScrim.Companion.Builder()
            .highLightViewIds(arrayOf(R.id.step1_tv to HighLightDrawerData.RectDrawerData()))
            .scrimColor(ContextCompat.getColor(this, R.color.color_scrim))
            .viewGetter { findViewById(it) }
            .build()

        val stepScrim2 = SimpleContainerGuideScrim.Companion.Builder()
            .highLightViewIds(arrayOf(R.id.step2_tv to HighLightDrawerData.RectDrawerData()))
            .scrimColor(ContextCompat.getColor(this, R.color.color_scrim))
            .viewGetter { findViewById(it) }
            .build()

        val stepScrim3 = SimpleContainerGuideScrim.Companion.Builder()
            .highLightViewIds(arrayOf(R.id.step3_tv to HighLightDrawerData.RectDrawerData()))
            .scrimColor(ContextCompat.getColor(this, R.color.color_scrim))
            .viewGetter { findViewById(it) }
            .build()

        val dialog = SimpleChainGuideScrim(
            mapOf(
                stepScrim1 to ChainGuideEvents(containerNextEvent = listOf(R.id.container_rl)),
                stepScrim2 to ChainGuideEvents(containerNextEvent = listOf(R.id.container_rl)),
                stepScrim3 to ChainGuideEvents(containerNextEvent = listOf(R.id.container_rl))
            )
        ).toSimpleContainerGuideScrimDialog(this)

        show_step_bt.setOnClickListener {
            dialog.show()
        }

    }

}