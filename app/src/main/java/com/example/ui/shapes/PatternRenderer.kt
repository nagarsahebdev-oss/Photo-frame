package com.example.ui.shapes

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path as AndroidPath
import android.graphics.RectF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.model.CollageStyle
import com.example.model.PatternStyle
import kotlin.math.PI
import kotlin.math.sin

object PatternRenderer {

    fun drawComposePattern(
        drawScope: DrawScope,
        style: CollageStyle
    ) {
        val width = drawScope.size.width
        val height = drawScope.size.height
        val baseColor = Color(style.patternBaseColor)
        val elemColor = Color(style.patternElementColor)
        val step = (style.patternScaleDp * drawScope.density).coerceAtLeast(16f)

        // Draw Base
        drawScope.drawRect(
            color = baseColor,
            topLeft = Offset.Zero,
            size = Size(width, height)
        )

        when (style.patternStyle) {
            PatternStyle.POLKA_DOTS -> {
                val radius = step * 0.16f
                var row = 0
                var y = step / 2
                while (y < height + step) {
                    val xOffset = if (row % 2 == 1) step / 2 else 0f
                    var x = -step + xOffset
                    while (x < width + step) {
                        drawScope.drawCircle(
                            color = elemColor,
                            radius = radius,
                            center = Offset(x, y)
                        )
                        x += step
                    }
                    y += step * 0.866f // hexagonal/equilateral grid spacing
                    row++
                }
            }

            PatternStyle.DIAGONAL_STRIPES -> {
                val strokeW = step * 0.35f
                val totalDist = width + height + step * 2
                var d = -step
                while (d < totalDist) {
                    drawScope.drawLine(
                        color = elemColor,
                        start = Offset(d, -step),
                        end = Offset(d - height - step * 2, height + step),
                        strokeWidth = strokeW
                    )
                    d += step * 1.5f
                }
            }

            PatternStyle.GRID_MESH -> {
                val lineW = (1.5f * drawScope.density).coerceAtLeast(1f)
                var x = 0f
                while (x <= width) {
                    drawScope.drawLine(
                        color = elemColor,
                        start = Offset(x, 0f),
                        end = Offset(x, height),
                        strokeWidth = lineW
                    )
                    x += step
                }
                var y = 0f
                while (y <= height) {
                    drawScope.drawLine(
                        color = elemColor,
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = lineW
                    )
                    y += step
                }
            }

            PatternStyle.HEARTS -> {
                val heartSize = step * 0.45f
                var row = 0
                var y = step / 2
                while (y < height + step) {
                    val xOffset = if (row % 2 == 1) step / 2 else 0f
                    var x = -step + xOffset
                    while (x < width + step) {
                        drawHeart(drawScope, x, y, heartSize, elemColor)
                        x += step
                    }
                    y += step * 0.9f
                    row++
                }
            }

            PatternStyle.STARS -> {
                val starSize = step * 0.4f
                var row = 0
                var y = step / 2
                while (y < height + step) {
                    val xOffset = if (row % 2 == 1) step / 2 else 0f
                    var x = -step + xOffset
                    while (x < width + step) {
                        drawStar(drawScope, x, y, starSize, elemColor)
                        x += step
                    }
                    y += step
                    row++
                }
            }

            PatternStyle.TERRAZZO -> {
                var row = 0
                var y = step / 2
                while (y < height + step) {
                    var x = step / 2
                    while (x < width + step) {
                        val seed = ((x * 13 + y * 29) % 360).toInt()
                        if (seed % 3 == 0) {
                            // small pill/capsule
                            drawScope.drawCircle(
                                color = elemColor.copy(alpha = 0.85f),
                                radius = step * 0.12f,
                                center = Offset(x, y)
                            )
                        } else if (seed % 3 == 1) {
                            drawScope.drawRect(
                                color = elemColor.copy(alpha = 0.9f),
                                topLeft = Offset(x - step * 0.15f, y - step * 0.08f),
                                size = Size(step * 0.3f, step * 0.16f)
                            )
                        } else {
                            drawScope.drawCircle(
                                color = elemColor.copy(alpha = 0.6f),
                                radius = step * 0.08f,
                                center = Offset(x + step * 0.1f, y + step * 0.1f)
                            )
                        }
                        x += step
                    }
                    y += step
                    row++
                }
            }

            PatternStyle.WAVY -> {
                val strokeW = (2f * drawScope.density).coerceAtLeast(1f)
                var y = step / 2
                while (y < height + step) {
                    val path = Path()
                    var first = true
                    var x = 0f
                    while (x <= width + 20f) {
                        val waveY = y + sin((x / step) * 2 * PI.toFloat()) * (step * 0.18f)
                        if (first) {
                            path.moveTo(x, waveY)
                            first = false
                        } else {
                            path.lineTo(x, waveY)
                        }
                        x += 8f
                    }
                    drawScope.drawPath(path = path, color = elemColor, style = Stroke(width = strokeW))
                    y += step * 0.8f
                }
            }
        }
    }

    private fun drawHeart(drawScope: DrawScope, cx: Float, cy: Float, size: Float, color: Color) {
        val path = Path().apply {
            val topY = cy - size / 2
            val midY = cy - size / 6
            val bottomY = cy + size / 2
            moveTo(cx, midY)
            cubicTo(cx - size / 2, topY - size / 4, cx - size, midY, cx, bottomY)
            cubicTo(cx + size, midY, cx + size / 2, topY - size / 4, cx, midY)
            close()
        }
        drawScope.drawPath(path = path, color = color)
    }

    private fun drawStar(drawScope: DrawScope, cx: Float, cy: Float, size: Float, color: Color) {
        val half = size / 2
        val path = Path().apply {
            moveTo(cx, cy - half)
            quadraticBezierTo(cx, cy, cx + half, cy)
            quadraticBezierTo(cx, cy, cx, cy + half)
            quadraticBezierTo(cx, cy, cx - half, cy)
            quadraticBezierTo(cx, cy, cx, cy - half)
            close()
        }
        drawScope.drawPath(path = path, color = color)
    }

    // Android Graphics Canvas for Bitmap Export
    fun drawAndroidPattern(
        canvas: Canvas,
        width: Float,
        height: Float,
        style: CollageStyle,
        density: Float
    ) {
        val baseColor = style.patternBaseColor.toInt()
        val elemColor = style.patternElementColor.toInt()
        val step = (style.patternScaleDp * density).coerceAtLeast(16f)

        val basePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = baseColor }
        val elemPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = elemColor }

        canvas.drawRect(0f, 0f, width, height, basePaint)

        when (style.patternStyle) {
            PatternStyle.POLKA_DOTS -> {
                val radius = step * 0.16f
                var row = 0
                var y = step / 2
                while (y < height + step) {
                    val xOffset = if (row % 2 == 1) step / 2 else 0f
                    var x = -step + xOffset
                    while (x < width + step) {
                        canvas.drawCircle(x, y, radius, elemPaint)
                        x += step
                    }
                    y += step * 0.866f
                    row++
                }
            }

            PatternStyle.DIAGONAL_STRIPES -> {
                val strokeW = step * 0.35f
                elemPaint.style = Paint.Style.STROKE
                elemPaint.strokeWidth = strokeW
                val totalDist = width + height + step * 2
                var d = -step
                while (d < totalDist) {
                    canvas.drawLine(d, -step, d - height - step * 2, height + step, elemPaint)
                    d += step * 1.5f
                }
                elemPaint.style = Paint.Style.FILL
            }

            PatternStyle.GRID_MESH -> {
                val lineW = (1.5f * density).coerceAtLeast(1f)
                elemPaint.style = Paint.Style.STROKE
                elemPaint.strokeWidth = lineW
                var x = 0f
                while (x <= width) {
                    canvas.drawLine(x, 0f, x, height, elemPaint)
                    x += step
                }
                var y = 0f
                while (y <= height) {
                    canvas.drawLine(0f, y, width, y, elemPaint)
                    y += step
                }
                elemPaint.style = Paint.Style.FILL
            }

            PatternStyle.HEARTS -> {
                val heartSize = step * 0.45f
                var row = 0
                var y = step / 2
                while (y < height + step) {
                    val xOffset = if (row % 2 == 1) step / 2 else 0f
                    var x = -step + xOffset
                    while (x < width + step) {
                        drawAndroidHeart(canvas, x, y, heartSize, elemPaint)
                        x += step
                    }
                    y += step * 0.9f
                    row++
                }
            }

            PatternStyle.STARS -> {
                val starSize = step * 0.4f
                var row = 0
                var y = step / 2
                while (y < height + step) {
                    val xOffset = if (row % 2 == 1) step / 2 else 0f
                    var x = -step + xOffset
                    while (x < width + step) {
                        drawAndroidStar(canvas, x, y, starSize, elemPaint)
                        x += step
                    }
                    y += step
                    row++
                }
            }

            PatternStyle.TERRAZZO -> {
                var y = step / 2
                while (y < height + step) {
                    var x = step / 2
                    while (x < width + step) {
                        val seed = ((x * 13 + y * 29) % 360).toInt()
                        if (seed % 3 == 0) {
                            canvas.drawCircle(x, y, step * 0.12f, elemPaint)
                        } else if (seed % 3 == 1) {
                            canvas.drawRect(
                                RectF(x - step * 0.15f, y - step * 0.08f, x + step * 0.15f, y + step * 0.08f),
                                elemPaint
                            )
                        } else {
                            canvas.drawCircle(x + step * 0.1f, y + step * 0.1f, step * 0.08f, elemPaint)
                        }
                        x += step
                    }
                    y += step
                }
            }

            PatternStyle.WAVY -> {
                val strokeW = (2f * density).coerceAtLeast(1f)
                elemPaint.style = Paint.Style.STROKE
                elemPaint.strokeWidth = strokeW
                var y = step / 2
                while (y < height + step) {
                    val path = AndroidPath()
                    var first = true
                    var x = 0f
                    while (x <= width + 20f) {
                        val waveY = y + sin((x / step) * 2 * PI.toFloat()) * (step * 0.18f)
                        if (first) {
                            path.moveTo(x, waveY)
                            first = false
                        } else {
                            path.lineTo(x, waveY)
                        }
                        x += 8f
                    }
                    canvas.drawPath(path, elemPaint)
                    y += step * 0.8f
                }
                elemPaint.style = Paint.Style.FILL
            }
        }
    }

    private fun drawAndroidHeart(canvas: Canvas, cx: Float, cy: Float, size: Float, paint: Paint) {
        val path = AndroidPath().apply {
            val topY = cy - size / 2
            val midY = cy - size / 6
            val bottomY = cy + size / 2
            moveTo(cx, midY)
            cubicTo(cx - size / 2, topY - size / 4, cx - size, midY, cx, bottomY)
            cubicTo(cx + size, midY, cx + size / 2, topY - size / 4, cx, midY)
            close()
        }
        canvas.drawPath(path, paint)
    }

    private fun drawAndroidStar(canvas: Canvas, cx: Float, cy: Float, size: Float, paint: Paint) {
        val half = size / 2
        val path = AndroidPath().apply {
            moveTo(cx, cy - half)
            quadTo(cx, cy, cx + half, cy)
            quadTo(cx, cy, cx, cy + half)
            quadTo(cx, cy, cx - half, cy)
            quadTo(cx, cy, cx, cy - half)
            close()
        }
        canvas.drawPath(path, paint)
    }
}
