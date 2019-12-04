package com.tans.guidescrim.dialogs

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity
import com.tans.guidescrim.R
import com.tans.guidescrim.guidescrims.GuideScrim
import com.tans.guidescrim.guidescrims.GuideScrimContainer

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-04
 */

abstract class ContainerGuideScrimDialog(ownerActivity: FragmentActivity,
                                         guideScrim: GuideScrim,
                                         private val guideScrimContainer: GuideScrimContainer,
                                         dialogTheme: Int = R.style.ScrimGuideDialogTheme)
    : GuideScrimDialog(ownerActivity, guideScrim, dialogTheme) {

    protected var container: RelativeLayout? = null

    abstract fun container(rootView: View): RelativeLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val container = container(view)
        this.container = container
        container.post {
            guideScrimContainer.onCreateContainerView(container)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        guideScrimContainer.onDestroyContainerView(container)
    }

}