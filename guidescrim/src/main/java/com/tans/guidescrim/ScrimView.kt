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

typealias ViewGetter = (id: Int) -> View?

// Boolean: has calculate this area or drawable.
typealias IdsHighLightDrawerData = Pair<Boolean, ScrimView.Companion.HighLightDrawerData>

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

    var viewGetter: ViewGetter = object : ViewGetter {

        override fun invoke(id: Int): View? {
            return (context as? Activity)?.findViewById(id)
        }

    }
        set(value) {
            field = value
            invalidate()
        }

    private val viewSize = Rect()

    private var highLightData: Map<Int, HighLightDrawerData> = emptyMap()

    /** Need to calculate the area and drawable
     * @see calculateAllIdsDrawerData
     */
    private var viewIdsHighLightData: Map<Int, IdsHighLightDrawerData> = emptyMap()

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
        viewIdsHighLightData =
            typedArray.getString(R.styleable.ScrimView_high_light_ids)?.split(",")
                ?.mapNotNull {
                    convertToIntId(it)
                }?.map {
                    it to (false to (HighLightDrawerData.RectDrawerData(area = Rect()) as HighLightDrawerData))
                }?.toMap() ?: emptyMap()
        typedArray.recycle()
    }

    fun updateScrimColor(@ColorInt color: Int, invalidate: Boolean = true) {
        scrimPaint.color = color
        if (invalidate) {
            invalidate()
        }
    }

    /**
     *  @param data's area is the screen location.
     *
     */
    fun setHighLightDrawerData(vararg data: HighLightDrawerData, invalidate: Boolean = true) {
        setHighLightDrawerData(data = *data.withIndex().map { it.index to it.value }.toTypedArray(), invalidate = invalidate)
    }

    fun setHighLightDrawerData(vararg data: Pair<Int, HighLightDrawerData>, invalidate: Boolean = true) {
        this.highLightData = data.map { (itemId, itemData) ->
            itemId to convertHighLightRectToViewRect(itemData)
        }.toMap()
        if (invalidate) {
            invalidate()
        }
    }

    /**
     * @param data's area is the screen location.
     */
    fun addHighLightAreas(vararg data: HighLightDrawerData, invalidate: Boolean = true) {
        val highLightCount = highLightData.count()
        addHighLightAreas(data = *data.withIndex().map { it.index + highLightCount to it.value }.toTypedArray(), invalidate = invalidate)
    }

    fun addHighLightAreas(vararg data: Pair<Int, HighLightDrawerData>, invalidate: Boolean = true) {
        this.highLightData = (this.highLightData + data.map { (itemId, itemData) ->
            itemId to convertHighLightRectToViewRect(itemData)
        })
        if (invalidate) {
            invalidate()
        }
    }

    /**
     * HighLightDrawerData area is not work, area is influenced by view location.
     */
    fun setHighLightViewIds(vararg data: Pair<Int, HighLightDrawerData>, invalidate: Boolean = true) {
        this.viewIdsHighLightData = data.map { (id, data) -> id to (false to data)  }.toMap()
        if (invalidate) {
            invalidate()
        }
    }

    /**
     * HighLightDrawerData area is not work, area is influenced by view location.
     */
    fun addHighLightViewIds(vararg data: Pair<Int, HighLightDrawerData>, invalidate: Boolean = true) {
        this.viewIdsHighLightData = this.viewIdsHighLightData + data
            .map { (id, data) -> id to (false to data) }.toMap()
        if (invalidate) {
            invalidate()
        }
    }

    fun removeHighLightByIds(vararg ids: Int, invalidate: Boolean = true) {
        this.viewIdsHighLightData = this.viewIdsHighLightData
            .filter { (id, _) -> !ids.contains(id) }
            .toMap()
        this.highLightData = this.highLightData
            .filter { (id, _) -> !ids.contains(id) }
            .toMap()
        if (invalidate) {
            invalidate()
        }
    }

    fun getHighLightDataById(id: Int): HighLightDrawerData? {
        val viewIdsHighLightData = this.viewIdsHighLightData
        val highLightData = this.highLightData
        return when {
            viewIdsHighLightData.containsKey(id) -> {
                viewIdsHighLightData[id]?.let {
                    calculateIdsDrawerData(id, it).second
                }
            }

            highLightData.containsKey(id) -> {
                highLightData[id]
            }

            else -> {
                null
            }
        }
    }

    fun clearHightLight(invalidate: Boolean = true) {
        this.viewIdsHighLightData = emptyMap()
        this.highLightData = emptyMap()
        if (invalidate) {
            invalidate()
        }
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

            val highLightDataList = getHighLightDataList()
            val idsHighLightDataList = calculateAllIdsDrawerData()
            this.viewIdsHighLightData = idsHighLightDataList
            val highLightDrawer = this.highLightDrawer
            for ((_, data) in highLightDataList + idsHighLightDataList.map { it.key to it.value.second }) {
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

    fun calculateAllIdsDrawerData(forceCalculate: Boolean = false): Map<Int, IdsHighLightDrawerData> {
        return this.viewIdsHighLightData.mapNotNull { (id, data) ->
            id to calculateIdsDrawerData(id, data, forceCalculate)
        }.toMap()
    }

    fun calculateIdsDrawerData(id: Int, idData: IdsHighLightDrawerData, forceCalculate: Boolean = false): IdsHighLightDrawerData {
        val (hasCalculate, data) = idData
        return if (hasCalculate && !forceCalculate) {
            idData
        } else {
            val view = viewGetter(id)
            if (view == null) {
                idData
            } else {
                when (data) {
                    is HighLightDrawerData.DrawableDrawerData -> {
                        true to data.copy(
                            area = getViewLocationRect(
                                getViewScreenLocationRect(view),
                                this
                            ),
                            drawable = drawViewContent(view)
                        )
                    }

                    is HighLightDrawerData.RectDrawerData -> {
                        true to data.copy(
                            area = getViewLocationRect(
                                getViewScreenLocationRect(view),
                                this
                            )
                        )
                    }
                }
            }
        }
    }

    fun getHighLightDataList(): Map<Int, HighLightDrawerData> {
        return this.highLightData
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
                style = Paint.Style.FILL_AND_STROKE
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
                rectHighLightPaint.pathEffect = CornerPathEffect(data.radius)
                canvas.drawRect(
                    fixedArea.left.toFloat(),
                    fixedArea.top.toFloat(),
                    fixedArea.right.toFloat(),
                    fixedArea.bottom.toFloat(),
                    rectHighLightPaint
                )

                if (data.borderData != null) {
                    highLightBorderPaint.color = data.borderData.color
                    highLightBorderPaint.strokeWidth = data.borderData.width.toFloat()
                    highLightBorderPaint.pathEffect = CornerPathEffect(data.radius)
                    canvas.drawRect(
                        fixedArea.left.toFloat(),
                        fixedArea.top.toFloat(),
                        fixedArea.right.toFloat(),
                        fixedArea.bottom.toFloat(),
                        highLightBorderPaint
                    )
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

