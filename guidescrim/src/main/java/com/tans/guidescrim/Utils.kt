package com.tans.guidescrim

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.view.View

/**
 *
 * author: pengcheng.tan
 * date: 2019-11-28
 */

/**
 * View must be measured before call this method.
 */
fun getViewScreenLocationRect(view: View): Rect {
    val viewCoordinates = IntArray(2)
    view.getLocationOnScreen(viewCoordinates)
    return Rect(viewCoordinates[0], viewCoordinates[1], viewCoordinates[0] + view.measuredWidth,
        viewCoordinates[1] + view.measuredHeight)
}

fun getViewLocationRect(rect: Rect, view: View): Rect {
    val viewCoordinates = IntArray(2)
    view.getLocationOnScreen(viewCoordinates)
    return Rect(rect.left - viewCoordinates[0], rect.top - viewCoordinates[1],
        rect.right - viewCoordinates[0], rect.bottom - viewCoordinates[1])
}

fun drawViewContent(view: View): BitmapDrawable {
    val context: Context = view.context
    val bitmap: Bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight,
        Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return BitmapDrawable(context.resources, bitmap)
}