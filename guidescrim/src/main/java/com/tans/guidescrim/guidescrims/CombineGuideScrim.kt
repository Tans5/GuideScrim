package com.tans.guidescrim.guidescrims

import android.view.View
import com.tans.guidescrim.ScrimView
import com.tans.guidescrim.ViewGetter

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-03
 */
class CombineGuideScrim(val left: GuideScrim,
                        val right: GuideScrim) : GuideScrim {


    override val highLightViewIds: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>> = left.highLightViewIds + right.highLightViewIds

    override val highLightData: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>> = left.highLightData + right.highLightData

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

}

infix operator fun GuideScrim.plus(right: GuideScrim): GuideScrim = CombineGuideScrim(this, right)

