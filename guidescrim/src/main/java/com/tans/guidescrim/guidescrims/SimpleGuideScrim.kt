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

        class Builder() {

            private var highLightData: Array<Pair<Int, HighLightDrawerData>> = emptyArray()
            private var highLightViewIds: Array<Pair<Int, HighLightDrawerData>> = emptyArray()
            private var viewGetter: ViewGetter = { null }
            private var scrimColor: Int = Color.TRANSPARENT

            fun highLightData(highLightData: Array<Pair<Int, HighLightDrawerData>>): Builder {
                this.highLightData = highLightData
                return this
            }

            fun highLightViewIds(highLightViewIds: Array<Pair<Int, HighLightDrawerData>>): Builder {
                this.highLightViewIds = highLightViewIds
                return this
            }

            fun viewGetter(viewGetter: ViewGetter): Builder {
                this.viewGetter = viewGetter
                return this
            }

            fun scrimColor(scrimColor: Int): Builder {
                this.scrimColor = scrimColor
                return this
            }

            fun build(): SimpleGuideScrim = SimpleGuideScrim(
                highLightData = this.highLightData,
                highLightViewIds = this.highLightViewIds,
                viewGetter = this.viewGetter,
                scrimColor = this.scrimColor
            )

        }

    }

}