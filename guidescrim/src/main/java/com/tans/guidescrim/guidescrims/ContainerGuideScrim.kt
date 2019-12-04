package com.tans.guidescrim.guidescrims

import android.graphics.Point
import com.tans.guidescrim.ViewGetter
import com.tans.guidescrim.ScrimView.Companion.HighLightDrawerData

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-04
 */

class ContainerGuideScrim(
    override val highLightViewIds: Array<Pair<Int, HighLightDrawerData>>,
    override val highLightData: Array<Pair<Int, HighLightDrawerData>>,
    override val scrimColor: Int,
    override val viewGetter: ViewGetter,
    override val childrenLayoutIdsAndPosition: () -> Map<Int, Point>
) : GuideScrim, GuideScrimContainer {


    companion object {
        class Builder : GuideScrim.Companion.Builder<ContainerGuideScrim>() {

            private var childrenLayoutIdsAndPosition = { emptyMap<Int, Point>() }

            fun childrenLayoutIdsAndPosition(childrenLayoutIdsAndPosition: () -> Map<Int, Point>): Builder {
                this.childrenLayoutIdsAndPosition = childrenLayoutIdsAndPosition
                return this
            }

            override fun build(): ContainerGuideScrim = ContainerGuideScrim(
                highLightViewIds = highLightViewIds,
                highLightData = highLightData,
                scrimColor = scrimColor,
                viewGetter = viewGetter,
                childrenLayoutIdsAndPosition = childrenLayoutIdsAndPosition
            )

        }
    }

}