package com.tans.guidescrim.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity
import com.tans.guidescrim.R
import com.tans.guidescrim.ScrimView
import com.tans.guidescrim.guidescrims.GuideScrim
import com.tans.guidescrim.guidescrims.GuideScrimContainer

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-04
 */

class SimpleContainerGuideScrimDialog(
    ownerActivity: FragmentActivity,
    guideScrim: GuideScrim,
    guideScrimContainer: GuideScrimContainer,
    dialogTheme: Int
) : ContainerGuideScrimDialog(ownerActivity, guideScrim, guideScrimContainer, dialogTheme) {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_simple_container_scrim_guide, container, false)
    }

    override fun container(rootView: View): RelativeLayout {
        return rootView.findViewById(R.id.container_rl)
    }

    override fun refreshScrimView(rootView: View): ScrimView {
        return rootView.findViewById(R.id.scrim_view)
    }

}

fun <T> T.toSimpleContainerGuideScrimDialog(
    activity: FragmentActivity,
    theme: Int = R.style.ScrimGuideDialogTheme
)
        : SimpleContainerGuideScrimDialog where T : GuideScrim, T : GuideScrimContainer =
    SimpleContainerGuideScrimDialog(
        ownerActivity = activity,
        guideScrim = this,
        guideScrimContainer = this,
        dialogTheme = theme
    )

