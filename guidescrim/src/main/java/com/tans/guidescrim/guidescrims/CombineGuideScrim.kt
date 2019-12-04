package com.tans.guidescrim.guidescrims

import android.graphics.Point
import android.view.View
import android.widget.RelativeLayout
import com.tans.guidescrim.ScrimView
import com.tans.guidescrim.ViewGetter

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-03
 */
class CombineGuideScrim(
    val left: GuideScrim,
    val right: GuideScrim
) : GuideScrim, GuideScrimContainer {

    override val childrenLayoutIdsAndPosition: () -> Map<Int, Point> = {
        ((left as? GuideScrimContainer)?.childrenLayoutIdsAndPosition?.invoke()
            ?: emptyMap()) + ((right as? GuideScrimContainer)?.childrenLayoutIdsAndPosition?.invoke()
            ?: emptyMap())
    }

    override val highLightViewIds: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>> =
        left.highLightViewIds + right.highLightViewIds

    override val highLightData: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>> =
        left.highLightData + right.highLightData

    override val scrimColor: Int = left.scrimColor

    override val viewGetter: ViewGetter = object : ViewGetter {
        override fun invoke(id: Int): View? {
            return left.viewGetter(id) ?: right.viewGetter(id)
        }

    }

    override fun onCreateView(scrimView: ScrimView) {
        left.onCreateView(scrimView)
        right.onCreateView(scrimView)
        scrimView.updateScrimColor(scrimColor, false)
        scrimView.viewGetter = viewGetter
    }

    override fun onDestroyView(scrimView: ScrimView?) {
        left.onDestroyView(scrimView)
        right.onDestroyView(scrimView)
    }

    override fun onCreateContainerView(container: RelativeLayout) {

        if (left is GuideScrimContainer) {
            left.onCreateContainerView(container)
        }

        if (right is GuideScrimContainer) {
            right.onCreateContainerView(container)
        }

    }

    override fun onDestroyContainerView(container: RelativeLayout?) {
        if (left is GuideScrimContainer) {
            left.onDestroyContainerView(container)
        }

        if (right is GuideScrimContainer) {
            right.onDestroyContainerView(container)
        }
    }

}

infix operator fun GuideScrim.plus(right: GuideScrim): CombineGuideScrim = CombineGuideScrim(this, right)

