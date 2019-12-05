package com.tans.guidescrim.guidescrims

import android.graphics.Point
import android.widget.RelativeLayout
import com.tans.guidescrim.ScrimView
import com.tans.guidescrim.ViewGetter
import com.tans.guidescrim.dialogs.GuideScrimDialog

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-05
 */

class SimpleChainGuideScrim(override val guideScrimsAndChainEvent: Map<GuideScrim, ChainGuideEvents>) : ChainGuideScrim {


    override var chainClickEvent: ClickEventGuideScrimChain = guideScrimsAndChainEvent.toClickEventGuideScrimChain()

    override var scrimView: ScrimView? = null

    override var container: RelativeLayout? = null
    override var dialog: GuideScrimDialog? = null


    /**
     *  below params not work for ChainGuideScrim.
     */
    override val highLightViewIds: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>> = emptyArray()
    override val highLightData: Array<Pair<Int, ScrimView.Companion.HighLightDrawerData>> = emptyArray()
    override val scrimColor: Int = -1
    override val viewGetter: ViewGetter = { null }
    override val childrenLayoutIdsAndPosition: () -> Map<Int, Point> = { emptyMap() }


}