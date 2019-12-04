package com.tans.guidescrim.guidescrims

import android.graphics.Point
import android.graphics.Rect
import android.widget.RelativeLayout
import com.tans.guidescrim.ScrimView
import com.tans.guidescrim.ViewGetter

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-04
 */

class ClickEventGuideScrim(
    private val guideScrim: GuideScrim,
    override val scrimClickData: () -> Map<Int, Pair<Rect, ClickHandler>> = { emptyMap() },
    override val containerClickData: Map<Int, ClickHandler> = emptyMap()
) : GuideScrim, GuideScrimClickEvent, GuideScrimContainer {

    override val highLightViewIds: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>> =
        guideScrim.highLightViewIds

    override val highLightData: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>> =
        guideScrim.highLightData

    override val scrimColor: Int = guideScrim.scrimColor

    override val viewGetter: ViewGetter = guideScrim.viewGetter

    override val childrenLayoutIdsAndPosition: () -> Map<Int, Point> = { (guideScrim as? GuideScrimContainer)?.childrenLayoutIdsAndPosition?.invoke() ?: emptyMap() }

    override fun onCreateView(scrimView: ScrimView) {
        guideScrim.onCreateView(scrimView)
        scrimClickEventInit(scrimView)
    }

    override fun onCreateContainerView(container: RelativeLayout) {
        (guideScrim as? GuideScrimContainer)?.onCreateContainerView(container)
        containerChildrenEventInit(container)
    }

    override fun onDestroyView(scrimView: ScrimView?) {
        guideScrim.onDestroyView(scrimView)
    }

    override fun onDestroyContainerView(container: RelativeLayout?) {
        (guideScrim as? GuideScrimContainer)?.onDestroyContainerView(container)
    }

}

fun GuideScrim.withClickEvent(scrimClickData: () -> Map<Int, Pair<Rect, ClickHandler>> = { emptyMap() },
                              containerClickData: Map<Int, ClickHandler> = emptyMap()): ClickEventGuideScrim = ClickEventGuideScrim(
    guideScrim = this,
    scrimClickData = scrimClickData,
    containerClickData = containerClickData
)