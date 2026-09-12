package com.example.ui.shapes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.example.model.SlotShapeSpec
import android.graphics.Path as AndroidPath

/**
 * Creates a smooth, beautifully proportioned Compose Path for a heart.
 */
fun buildComposeHeartPath(size: Size): Path {
    val path = Path()
    val width = size.width
    val height = size.height

    val topCurveHeight = height * 0.35f

    path.moveTo(width / 2f, height * 0.25f)
    // Left lobe
    path.cubicTo(
        width * 0.15f, -height * 0.05f,
        -width * 0.05f, topCurveHeight,
        width * 0.18f, height * 0.60f
    )
    // Down to bottom tip
    path.cubicTo(
        width * 0.30f, height * 0.78f,
        width * 0.44f, height * 0.92f,
        width / 2f, height * 0.98f
    )
    // Right bottom back up
    path.cubicTo(
        width * 0.56f, height * 0.92f,
        width * 0.70f, height * 0.78f,
        width * 0.82f, height * 0.60f
    )
    // Right lobe
    path.cubicTo(
        width * 1.05f, topCurveHeight,
        width * 0.85f, -height * 0.05f,
        width / 2f, height * 0.25f
    )
    path.close()
    return path
}

/**
 * Creates the exact same heart path for Android Graphics (Bitmap export).
 */
fun buildAndroidHeartPath(width: Float, height: Float): AndroidPath {
    val path = AndroidPath()
    val topCurveHeight = height * 0.35f

    path.moveTo(width / 2f, height * 0.25f)
    path.cubicTo(
        width * 0.15f, -height * 0.05f,
        -width * 0.05f, topCurveHeight,
        width * 0.18f, height * 0.60f
    )
    path.cubicTo(
        width * 0.30f, height * 0.78f,
        width * 0.44f, height * 0.92f,
        width / 2f, height * 0.98f
    )
    path.cubicTo(
        width * 0.56f, height * 0.92f,
        width * 0.70f, height * 0.78f,
        width * 0.82f, height * 0.60f
    )
    path.cubicTo(
        width * 1.05f, topCurveHeight,
        width * 0.85f, -height * 0.05f,
        width / 2f, height * 0.25f
    )
    path.close()
    return path
}

/**
 * Compose Heart Shape
 */
class HeartShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = buildComposeHeartPath(size)
        return Outline.Generic(path)
    }
}

/**
 * Compose Triangle Shape
 */
class TriangleShape(private val type: SlotShapeSpec.TriangleType) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val w = size.width
        val h = size.height

        when (type) {
            SlotShapeSpec.TriangleType.UP -> {
                path.moveTo(w * 0.5f, 0f)
                path.lineTo(w, h)
                path.lineTo(0f, h)
            }
            SlotShapeSpec.TriangleType.DOWN -> {
                path.moveTo(0f, 0f)
                path.lineTo(w, 0f)
                path.lineTo(w * 0.5f, h)
            }
            SlotShapeSpec.TriangleType.LEFT -> {
                path.moveTo(0f, h * 0.5f)
                path.lineTo(w, 0f)
                path.lineTo(w, h)
            }
            SlotShapeSpec.TriangleType.RIGHT -> {
                path.moveTo(0f, 0f)
                path.lineTo(w, h * 0.5f)
                path.lineTo(0f, h)
            }
            SlotShapeSpec.TriangleType.DIAGONAL_TOP_LEFT -> {
                path.moveTo(0f, 0f)
                path.lineTo(w, 0f)
                path.lineTo(0f, h)
            }
            SlotShapeSpec.TriangleType.DIAGONAL_BOTTOM_RIGHT -> {
                path.moveTo(w, 0f)
                path.lineTo(w, h)
                path.lineTo(0f, h)
            }
            SlotShapeSpec.TriangleType.DIAGONAL_TOP_RIGHT -> {
                path.moveTo(0f, 0f)
                path.lineTo(w, 0f)
                path.lineTo(w, h)
            }
            SlotShapeSpec.TriangleType.DIAGONAL_BOTTOM_LEFT -> {
                path.moveTo(0f, 0f)
                path.lineTo(w, h)
                path.lineTo(0f, h)
            }
        }
        path.close()
        return Outline.Generic(path)
    }
}

/**
 * Compose Polygon Shape from normalized (0..1) relative coordinates
 */
class PolygonShape(private val points: List<Pair<Float, Float>>) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        if (points.isNotEmpty()) {
            path.moveTo(points[0].first * size.width, points[0].second * size.height)
            for (i in 1 until points.size) {
                path.lineTo(points[i].first * size.width, points[i].second * size.height)
            }
            path.close()
        }
        return Outline.Generic(path)
    }
}

/**
 * Android Graphics Polygon Path builder
 */
fun buildAndroidPolygonPath(points: List<Pair<Float, Float>>, width: Float, height: Float): AndroidPath {
    val path = AndroidPath()
    if (points.isNotEmpty()) {
        path.moveTo(points[0].first * width, points[0].second * height)
        for (i in 1 until points.size) {
            path.lineTo(points[i].first * width, points[i].second * height)
        }
        path.close()
    }
    return path
}

/**
 * Android Graphics Triangle Path builder
 */
fun buildAndroidTrianglePath(type: SlotShapeSpec.TriangleType, width: Float, height: Float): AndroidPath {
    val path = AndroidPath()
    when (type) {
        SlotShapeSpec.TriangleType.UP -> {
            path.moveTo(width * 0.5f, 0f)
            path.lineTo(width, height)
            path.lineTo(0f, height)
        }
        SlotShapeSpec.TriangleType.DOWN -> {
            path.moveTo(0f, 0f)
            path.lineTo(width, 0f)
            path.lineTo(width * 0.5f, height)
        }
        SlotShapeSpec.TriangleType.LEFT -> {
            path.moveTo(0f, height * 0.5f)
            path.lineTo(width, 0f)
            path.lineTo(width, height)
        }
        SlotShapeSpec.TriangleType.RIGHT -> {
            path.moveTo(0f, 0f)
            path.lineTo(width, height * 0.5f)
            path.lineTo(0f, height)
        }
        SlotShapeSpec.TriangleType.DIAGONAL_TOP_LEFT -> {
            path.moveTo(0f, 0f)
            path.lineTo(width, 0f)
            path.lineTo(0f, height)
        }
        SlotShapeSpec.TriangleType.DIAGONAL_BOTTOM_RIGHT -> {
            path.moveTo(width, 0f)
            path.lineTo(width, height)
            path.lineTo(0f, height)
        }
        SlotShapeSpec.TriangleType.DIAGONAL_TOP_RIGHT -> {
            path.moveTo(0f, 0f)
            path.lineTo(width, 0f)
            path.lineTo(width, height)
        }
        SlotShapeSpec.TriangleType.DIAGONAL_BOTTOM_LEFT -> {
            path.moveTo(0f, 0f)
            path.lineTo(width, height)
            path.lineTo(0f, height)
        }
    }
    path.close()
    return path
}
