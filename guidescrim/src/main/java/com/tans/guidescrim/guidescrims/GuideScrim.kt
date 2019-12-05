package com.tans.guidescrim.guidescrims


import android.graphics.Color
import androidx.fragment.app.FragmentActivity
import com.tans.guidescrim.R
import com.tans.guidescrim.ScrimView
import com.tans.guidescrim.ViewGetter
import com.tans.guidescrim.dialogs.GuideScrimDialog
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


    fun onDialogCreate(dialog: GuideScrimDialog) {

    }

    fun onCreateView(scrimView: ScrimView) {
        scrimView.addHighLightViewIds(data = *highLightViewIds, invalidate = false)
        scrimView.addHighLightAreas(data = *highLightData, invalidate = false)
        scrimView.updateScrimColor(color = scrimColor, invalidate = false)
        scrimView.viewGetter = viewGetter
    }

    fun onDestroyView(scrimView: ScrimView?) {
        scrimView?.removeHighLightByIds(*(highLightData + highLightViewIds).map { it.first }.toIntArray())
    }

    fun onDialogDestroy(dialog: GuideScrimDialog) {

    }

    companion object {
        abstract class Builder<T: GuideScrim> {
            protected var highLightData: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>> = emptyArray()
            protected var highLightViewIds: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>> = emptyArray()
            protected var viewGetter: ViewGetter = { null }
            protected var scrimColor: Int = Color.TRANSPARENT

            fun highLightData(highLightData: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>>): Builder<T> {
                this.highLightData = highLightData
                return this
            }

            fun highLightViewIds(highLightViewIds: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>>): Builder<T> {
                this.highLightViewIds = highLightViewIds
                return this
            }

            fun viewGetter(viewGetter: ViewGetter): Builder<T> {
                this.viewGetter = viewGetter
                return this
            }

            fun scrimColor(scrimColor: Int): Builder<T> {
                this.scrimColor = scrimColor
                return this
            }

            abstract fun build(): T
        }
    }

}