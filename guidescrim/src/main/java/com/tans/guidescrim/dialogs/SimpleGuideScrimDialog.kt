package com.tans.guidescrim.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.tans.guidescrim.R
import com.tans.guidescrim.ScrimView
import com.tans.guidescrim.guidescrims.GuideScrim

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-03
 */
class SimpleGuideScrimDialog(ownerActivity: FragmentActivity,
                             guideScrim: GuideScrim,
                             dialogTheme: Int) : GuideScrimDialog(ownerActivity, guideScrim, dialogTheme) {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_simple_scrim_guide, container, false)
    }

    override fun refreshScrimView(rootView: View): ScrimView {
        return rootView.findViewById<ScrimView>(R.id.scrim_view)
    }

}

fun GuideScrim.toSimpleGuideDialog(activity: FragmentActivity, theme: Int = R.style.ScrimGuideDialogTheme)
        : SimpleGuideScrimDialog = SimpleGuideScrimDialog(ownerActivity = activity, dialogTheme = theme, guideScrim = this)