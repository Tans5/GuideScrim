package com.tans.guidescrim

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

/**
 *
 * author: pengcheng.tan
 * date: 2019-11-28
 */

class ScrimView : View {


    val scrimPaint: Paint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    var highLightDrawer: HighLightDrawer = DefaultHighLightDrawer()
        set(value) {
            field = value
            invalidate()
        }

    private val viewSize = Rect()

    private var highLightDataArray: Array<out HighLightDrawerData> = emptyArray()

    private var highLightViewsIds: Map<Int, HighLightDrawerData> = emptyMap()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initAttrs(attrs)
    }


    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrimView)
        scrimPaint.color = typedArray.getColor(R.styleable.ScrimView_scrim_color, Color.TRANSPARENT)
        highLightViewsIds = typedArray.getString(R.styleable.ScrimView_high_light_ids)?.split(",")
            ?.mapNotNull {
                convertToIntId(it)
            }?.map {
                it to (HighLightDrawerData.RectDrawerData(area = Rect()) as HighLightDrawerData)
            }?.toMap() ?: emptyMap()
        typedArray.recycle()
    }

    /**
     *  @param data's area is the screen location.
     *
     */
    fun setHighLightDrawerData(vararg data: HighLightDrawerData) {
        this.highLightDataArray = data.map {
            convertHighLightRectToViewRect(it)
        }.toTypedArray()
        invalidate()
    }

    /**
     * @param data's area is the screen location.
     */
    fun addHighLightAreas(vararg data: HighLightDrawerData) {
        this.highLightDataArray = (this.highLightDataArray.asList() + data.map {
            convertHighLightRectToViewRect(it)
        }).toTypedArray()
        invalidate()
    }

    /**
     * HighLightDrawerData area is not work, area is influenced by view location.
     */
    fun setHighLightViewIds(vararg data: Pair<Int, HighLightDrawerData>) {
        this.highLightViewsIds = data.toMap()
        invalidate()
    }

    /**
     * HighLightDrawerData area is not work, area is influenced by view location.
     */
    fun addHighLightViewIds(vararg data: Pair<Int, HighLightDrawerData>) {
        this.highLightViewsIds = this.highLightViewsIds + data.toMap()
        invalidate()
    }

    fun removeHighLightByIds(vararg ids: Int) {
        this.highLightViewsIds = this.highLightViewsIds
            .filter { (id, _) -> !ids.contains(id) }
            .toMap()
        invalidate()
    }

    fun clearHightLight() {
        this.highLightViewsIds = emptyMap()
        this.highLightDataArray = emptyArray()
        invalidate()
    }

    private fun convertHighLightRectToViewRect(data: HighLightDrawerData): HighLightDrawerData {
        return when (data) {
            is HighLightDrawerData.DrawableDrawerData -> {
                data.copy(area = getViewLocationRect(data.area, this))
            }

            is HighLightDrawerData.RectDrawerData -> {
                data.copy(area = getViewLocationRect(data.area, this))
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            val width = measuredWidth
            val height = measuredHeight
            val sc = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)

            viewSize.set(0, 0, width, height)
            canvas.drawRect(viewSize, scrimPaint)

            val highLightDataList = this.highLightDataArray.toList()
            val idsHighLightDataList: List<HighLightDrawerData> =
                this.highLightViewsIds.mapNotNull { (id, data) ->
                    val view = (context as? Activity)?.findViewById<View>(id)
                    if (view == null) {
                        null
                    } else {
                        when (data) {
                            is HighLightDrawerData.DrawableDrawerData -> {
                                data.copy(
                                    area = getViewLocationRect(
                                        getViewScreenLocationRect(view),
                                        this
                                    ),
                                    drawable = drawViewContent(view)
                                )
                            }

                            is HighLightDrawerData.RectDrawerData -> {
                                data.copy(
                                    area = getViewLocationRect(
                                        getViewScreenLocationRect(view),
                                        this
                                    )
                                )
                            }
                        }
                    }
                }
            val highLightDrawer = this.highLightDrawer
            for (data in highLightDataList + idsHighLightDataList) {
                when (data) {
                    is HighLightDrawerData.DrawableDrawerData -> {
                        highLightDrawer.drawDrawable(data, canvas)
                    }

                    is HighLightDrawerData.RectDrawerData -> {
                        highLightDrawer.drawRect(data, canvas)
                    }
                }
            }
            canvas.restoreToCount(sc)
        }
    }

    private fun convertToIntId(idString: String?): Int? {
        return if (idString != null) {

            val trimString = idString.trim { it <= ' ' }

            try {
                val res = R.id::class.java
                val field = res.getField(trimString)
                field.getInt(null as Any?)
            } catch (var5: Exception) {
                null
            }

        } else {
            null
        }
    }

    companion object {

        sealed class HighLightDrawerData {

            abstract val area: Rect

            data class RectDrawerData(
                override val area: Rect = Rect(),
                val offsets: Rect = Rect(),
                val radius: Float = 0f,
                val borderData: BorderData? = null
            ) : HighLightDrawerData() {

                fun getAreaWithOffsets(): Rect = Rect(
                    area.left - offsets.left,
                    area.top - offsets.top,
                    area.right + offsets.right,
                    area.bottom + offsets.bottom
                )

                data class BorderData(
                    @ColorInt val color: Int = Color.WHITE,
                    val width: Int = 5
                )

            }

            data class DrawableDrawerData(
                override val area: Rect = Rect(),
                val drawable: Drawable? = null
            ) : HighLightDrawerData()
        }

        interface HighLightDrawer {

            fun drawRect(data: HighLightDrawerData.RectDrawerData, canvas: Canvas)

            fun drawDrawable(data: HighLightDrawerData.DrawableDrawerData, canvas: Canvas)

        }

        class DefaultHighLightDrawer(
            val rectHighLightPaint: Paint = Paint().apply {
                color = Color.TRANSPARENT
                style = Paint.Style.FILL
                isAntiAlias = true
                xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            },
            val highLightBorderPaint: Paint = Paint().apply {
                color = Color.TRANSPARENT
                style = Paint.Style.STROKE
                isAntiAlias = true
                strokeWidth = 5f
            }
        ) : HighLightDrawer {

            override fun drawRect(data: HighLightDrawerData.RectDrawerData, canvas: Canvas) {
                val fixedArea = data.getAreaWithOffsets()
                canvas.drawRoundRect(
                    fixedArea.left.toFloat(),
                    fixedArea.top.toFloat(),
                    fixedArea.right.toFloat(),
                    fixedArea.bottom.toFloat(),
                    data.radius, data.radius, rectHighLightPaint)

                if (data.borderData != null) {
                    highLightBorderPaint.color = data.borderData.color
                    highLightBorderPaint.strokeWidth = data.borderData.width.toFloat()
                    canvas.drawRoundRect(
                        fixedArea.left.toFloat(),
                        fixedArea.top.toFloat(),
                        fixedArea.right.toFloat(),
                        fixedArea.bottom.toFloat(),
                        data.radius, data.radius, highLightBorderPaint)
                }
            }

            override fun drawDrawable(
                data: HighLightDrawerData.DrawableDrawerData,
                canvas: Canvas
            ) {
                data.drawable?.bounds = data.area
                data.drawable?.draw(canvas)
            }

        }

    }

}

