package com.tans.guidescrim.guidescrims

import android.graphics.Rect
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.widget.RelativeLayout
import com.tans.guidescrim.ScrimView

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-04
 */

typealias ClickHandler = (id: Int) -> Boolean

interface GuideScrimClickEvent {

    /**
     * Rect is screen position.
     */
    val scrimClickData: () -> Map<Int, Pair<Rect, ClickHandler>>

    val containerClickData: Map<Int, ClickHandler>

    fun scrimClickEventInit(scrimView: ScrimView) {
        val scrimClickData = scrimClickData.invoke()
        scrimView.isClickable = true
        scrimView.setOnTouchListener { v, event ->
            val action = event.action
            if (action == ACTION_UP) {
                for ((id, data) in scrimClickData) {
                    val clickX = event.rawX.toInt()
                    val clickY  = event.rawY.toInt()
                    if (data.first.contains(clickX, clickY)) {
                        if (data.second(id)) {
                            break
                        }
                    }
                }
            }
            true
        }
    }

    fun containerChildrenEventInit(container: RelativeLayout) {
        containerClickData.forEach { (id, handler) ->
            container.findViewById<View>(id).setOnClickListener {
                handler(id)
            }
        }
    }

}