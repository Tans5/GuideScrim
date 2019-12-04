package com.tans.guidescrim.guidescrims

import android.graphics.Color
import com.tans.guidescrim.ViewGetter
import com.tans.guidescrim.ScrimView.Companion.HighLightDrawerData

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-03
 */
class SimpleGuideScrim(override val highLightData: Array<Pair<Int, HighLightDrawerData>> = emptyArray(),
                       override val highLightViewIds: Array<Pair<Int, HighLightDrawerData>> = emptyArray(),
                       override val viewGetter: ViewGetter = { null },
                       override val scrimColor: Int = Color.TRANSPARENT) : GuideScrim {

    companion object {

        class Builder : GuideScrim.Companion.Builder<SimpleGuideScrim>() {

            override fun build(): SimpleGuideScrim = SimpleGuideScrim(
                highLightData = this.highLightData,
                highLightViewIds = this.highLightViewIds,
                viewGetter = this.viewGetter,
                scrimColor = this.scrimColor
            )

        }

    }

}