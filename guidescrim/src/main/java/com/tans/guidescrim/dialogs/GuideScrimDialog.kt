package com.tans.guidescrim.dialogs


import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.tans.guidescrim.R
import com.tans.guidescrim.ScrimView
import com.tans.guidescrim.guidescrims.GuideScrim

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-03
 */

abstract class GuideScrimDialog(private val ownerActivity: FragmentActivity,
                                private val guideScrim: GuideScrim,
                                private val dialogTheme: Int = R.style.ScrimGuideDialogTheme) : DialogFragment() {


    protected var scrimView: ScrimView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NORMAL, dialogTheme)
        super.onCreate(savedInstanceState)
        guideScrim.onDialogCreate(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshScrimView(view).apply {
            post { guideScrim.onCreateView(this) }
        }
    }

    abstract fun refreshScrimView(rootView: View): ScrimView

    override fun onDestroyView() {
        super.onDestroyView()
        guideScrim.onDestroyView(scrimView)
    }

    override fun onDestroy() {
        super.onDestroy()
        guideScrim.onDialogDestroy(this)
    }

    fun show() {
        show(ownerActivity.supportFragmentManager, GUIDE_SCRIM_DIALOG)
    }

    companion object {
        const val GUIDE_SCRIM_DIALOG = "guide_scrim_dialog"
    }

}