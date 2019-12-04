package com.tans.guidescrim.guidescrims

import android.graphics.Point
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.tans.guidescrim.getViewPositionPoint

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-03
 */
interface GuideScrimContainer {

    /**
     * Point is relate to screen position.
     */
    val childrenLayoutIdsAndPosition: () -> Map<Int, Point>

    fun onCreateContainerView(container: RelativeLayout) {
        val context = container.context
        childrenLayoutIdsAndPosition().forEach { (id, screenPoint) ->
            val child = LayoutInflater.from(context).inflate(id, container, false)
            val lp = RelativeLayout.LayoutParams(child.layoutParams)
            val containerPoint = getViewPositionPoint(screenPoint, container)
            lp.addRule(RelativeLayout.ALIGN_PARENT_START)
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            lp.marginStart = containerPoint.x
            lp.topMargin = containerPoint.y
            child.id = id
            child.layoutParams = lp
            container.addView(child, lp)
        }
    }

    fun onDestroyContainerView(container: RelativeLayout?) {
        childrenLayoutIdsAndPosition().forEach { (id, _) ->
            container?.removeView(container.findViewById(id))
        }
    }
}