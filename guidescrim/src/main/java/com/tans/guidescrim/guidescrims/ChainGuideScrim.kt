package com.tans.guidescrim.guidescrims

import android.graphics.Rect
import android.widget.RelativeLayout
import com.tans.guidescrim.ScrimView
import com.tans.guidescrim.dialogs.GuideScrimDialog

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-04
 */

interface ChainGuideScrim : GuideScrim, GuideScrimContainer {

    val guideScrimsAndChainEvent: Map<GuideScrim, ChainGuideEvents>

    var chainClickEvent: ClickEventGuideScrimChain

    var scrimView: ScrimView?

    var container: RelativeLayout?

    var dialog: GuideScrimDialog?

    fun previous() {
        val chainClickEvent = this.chainClickEvent
        val previous = chainClickEvent.previous
        if (previous == null) {
            finish()
        } else {
            with(chainClickEvent.guideScrim) {
                onDestroyView(scrimView)
                if (container != null) {
                    onDestroyContainerView(container)
                }
                onDialogDestroy(dialog!!)
            }
            with(previous.guideScrim) {
                onDialogCreate(dialog!!)
                onCreateView(scrimView!!)
                val container = this@ChainGuideScrim.container
                if (container != null) {
                    onCreateContainerView(container)
                }
            }
            this.chainClickEvent = previous
        }
    }

    fun next() {

        val chainClickEvent = this.chainClickEvent
        val next = chainClickEvent.next
        if (next == null) {
            finish()
        } else {
            with(chainClickEvent.guideScrim) {
                onDestroyView(scrimView)
                if (container != null) {
                    onDestroyContainerView(container)
                }
                onDialogDestroy(dialog!!)
            }
            with(next.guideScrim) {
                onDialogCreate(dialog!!)
                onCreateView(scrimView!!)
                val container = this@ChainGuideScrim.container
                if (container != null) {
                    onCreateContainerView(container)
                }
            }
            this.chainClickEvent = next
        }

    }

    fun finish() {
        dialog?.dismiss()
    }

    override fun onDialogCreate(dialog: GuideScrimDialog) {
        super.onDialogCreate(dialog)
        this.dialog = dialog
        this.chainClickEvent = this.chainClickEvent.moveToHead()
        val chainClickEvent = this.chainClickEvent
        chainClickEvent.guideScrim.onDialogCreate(dialog)
    }

    override fun onDialogDestroy(dialog: GuideScrimDialog) {
        super.onDialogDestroy(dialog)
        this.dialog = null
        val chainClickEvent = this.chainClickEvent
        chainClickEvent.guideScrim.onDialogDestroy(dialog)
    }

    override fun onCreateView(scrimView: ScrimView) {
        super.onCreateView(scrimView)
        val chainClickEvent = this.chainClickEvent
        chainClickEvent.guideScrim.onCreateView(scrimView)
        this.scrimView = scrimView
    }

    override fun onDestroyView(scrimView: ScrimView?) {
        super.onDestroyView(scrimView)
        val chainClickEvent = this.chainClickEvent
        chainClickEvent.guideScrim.onDestroyView(scrimView)
        this.scrimView = null
    }

    override fun onCreateContainerView(container: RelativeLayout) {
        super.onCreateContainerView(container)
        val chainClickEvent = this.chainClickEvent
        chainClickEvent.guideScrim.onCreateContainerView(container)
        this.container = container
    }

    override fun onDestroyContainerView(container: RelativeLayout?) {
        super.onDestroyContainerView(container)
        val chainClickEvent = this.chainClickEvent
        chainClickEvent.guideScrim.onDestroyContainerView(container)
        this.container = null
    }

    fun Map<GuideScrim, ChainGuideEvents>.toClickEventGuideScrimChain()
            : ClickEventGuideScrimChain {
        val list = this.toList()
        return list.foldRight(
            ClickEventGuideScrimChain(guideScrim = list[list.lastIndex]
                .let { (guideScrim, events) ->
                    guideScrim.toChainClickEventGuideScrim(events)
                })
        ) { (guideScrim, events), next ->
            if (list[list.lastIndex].first != guideScrim) {
                val guideScrimEvent = guideScrim.toChainClickEventGuideScrim(events)
                val chain =
                    ClickEventGuideScrimChain(next = next, guideScrim = guideScrimEvent)
                next.previous = chain
                chain
            } else {
                next
            }
        }
    }

    fun GuideScrim.toChainClickEventGuideScrim(events: ChainGuideEvents): SimpleClickEventGuideScrim {
        val previousClickHandler: ClickHandler = {
            previous()
            true
        }
        val nextClickHandler: ClickHandler = {
            next()
            true
        }
        val finishHandler: ClickHandler = {
            finish()
            true
        }
        val scrimClickData =
            events.scrimPreEvent.map { (id, rect) ->
                id to (rect to previousClickHandler)
            } + events.scrimNextEvent.map { (id, rect) ->
                id to (rect to nextClickHandler)
            } + events.scrimFinishEvent.map { (id, rect) ->
                id to (rect to finishHandler)
            }

        val containerClickData = events.containerPreEvent.map { id ->
            id to previousClickHandler
        } + events.containerNextEvent.map { id ->
            id to nextClickHandler
        } + events.containerFinishEvent.map { id ->
            id to finishHandler
        }

        return this.withClickEvent(
            scrimClickData = { scrimClickData.toMap() },
            containerClickData = containerClickData.toMap()
        )
    }

}

data class ClickEventGuideScrimChain(
    var previous: ClickEventGuideScrimChain? = null,
    var next: ClickEventGuideScrimChain? = null,
    val guideScrim: SimpleClickEventGuideScrim
) {

    fun moveToHead(): ClickEventGuideScrimChain {
        var i: Int = 0
        var result = this
        var previous = result.previous
        while (previous != null) {
            result = previous
            previous = result.previous
            i++
            if (i > MAX_COUNT) {
                throw error("Can't find head.")
            }
        }
        return result
    }

    fun moveToTail(): ClickEventGuideScrimChain {
        var i: Int = 0
        var result = this
        var next = result.next
        while (next != null) {
            result = next
            next = result.next
            i++
            if (i > MAX_COUNT) {
                throw error("Can't find head.")
            }
        }
        return result
    }

    companion object {
        const val MAX_COUNT = 1000
    }

}

data class ChainGuideEvents(
    val scrimPreEvent: Map<Int, Rect> = emptyMap(),
    val scrimNextEvent: Map<Int, Rect> = emptyMap(),
    val scrimFinishEvent: Map<Int, Rect> = emptyMap(),
    val containerPreEvent: List<Int> = emptyList(),
    val containerNextEvent: List<Int> = emptyList(),
    val containerFinishEvent: List<Int> = emptyList()
)