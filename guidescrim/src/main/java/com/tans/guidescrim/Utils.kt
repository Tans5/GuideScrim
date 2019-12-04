package com.tans.guidescrim

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
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

fun getViewPositionPoint(point: Point, view: View): Point {
    val viewCoordinates = IntArray(2)
    view.getLocationOnScreen(viewCoordinates)
    return Point(point.x - viewCoordinates[0], point.y - viewCoordinates[1])
}

fun bottomOfViewPoint(
    view: View,
    offsetTop: Int = 0,
    centerHorizontal: Boolean = false,
    selfSize: Rect = Rect()
): Point {
    val viewRect = getViewScreenLocationRect(view)
    return if (centerHorizontal) {
        val offsetX = (viewRect.width() - selfSize.width()) / 2
        Point(viewRect.left + offsetX, viewRect.bottom + offsetTop)
    } else {
        Point(viewRect.left, viewRect.bottom + offsetTop)
    }
}

fun rightOfViewPoint(
    view: View,
    offsetLeft: Int = 0,
    centerVertical: Boolean = false,
    selfSize: Rect = Rect()
): Point {
    val viewRect = getViewScreenLocationRect(view)
    return if (centerVertical) {
        val offsetY = (viewRect.height() - selfSize.height()) / 2
        Point(viewRect.left + offsetLeft, viewRect.right + offsetY)
    } else {
        Point(viewRect.left + offsetLeft, viewRect.right)
    }
}

fun topOfViewPoint(
    view: View,
    offsetBottom: Int = 0,
    centerHorizontal: Boolean = false,
    selfSize: Rect = Rect()
): Point {
    val viewRect = getViewScreenLocationRect(view)
    return if (centerHorizontal) {
        val offsetX = (viewRect.width() - selfSize.width()) / 2
        Point(viewRect.left + offsetX, viewRect.top - selfSize.height() - offsetBottom)
    } else {
        Point(viewRect.left, viewRect.top - selfSize.height() - offsetBottom)
    }
}

fun leftOfViewPoint(
    view: View,
    offsetRight: Int = 0,
    centerVertical: Boolean = false,
    selfSize: Rect = Rect()
): Point {
    val viewRect = getViewScreenLocationRect(view)
    return if (centerVertical) {
        val offsetX = (viewRect.width() - selfSize.width()) / 2
        Point(viewRect.left - selfSize.width() - offsetRight, viewRect.top + offsetX)
    } else {
        Point(viewRect.left - selfSize.width() - offsetRight, viewRect.top)
    }
}

fun drawViewContent(view: View): BitmapDrawable {
    val context: Context = view.context
    val bitmap: Bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight,
        Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return BitmapDrawable(context.resources, bitmap)
}