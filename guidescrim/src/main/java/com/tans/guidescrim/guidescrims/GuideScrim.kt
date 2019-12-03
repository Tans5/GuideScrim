package com.tans.guidescrim.guidescrims


import androidx.fragment.app.FragmentActivity
import com.tans.guidescrim.R
import com.tans.guidescrim.ScrimView
import com.tans.guidescrim.ViewGetter
import com.tans.guidescrim.dialogs.SimpleGuideScrimDialog

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-03
 */

interface GuideScrim{

    val highLightViewIds: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>>

    val highLightData: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>>

    val scrimColor: Int

    val viewGetter: ViewGetter


    fun onCreateView(scrimView: ScrimView) {
        scrimView.addHighLightViewIds(data = *highLightViewIds, invalidate = false)
        scrimView.addHighLightAreas(data = *highLightData, invalidate = false)
        scrimView.updateScrimColor(color = scrimColor, invalidate = false)
        scrimView.viewGetter = viewGetter
    }

    fun onDestroyView(scrimView: ScrimView?) {
        scrimView?.removeHighLightByIds(*(highLightData + highLightViewIds).map { it.first }.toIntArray())
    }

}

fun GuideScrim.toSimpleGuideDialog(activity: FragmentActivity, theme: Int = R.style.ScrimGuideDialogTheme)
        : SimpleGuideScrimDialog = SimpleGuideScrimDialog(ownerActivity = activity, dialogTheme = theme, guideScrim = this)