package com.example.model

import android.graphics.ColorMatrixColorFilter
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix

/**
 * Filter effects that can be applied to individual photos in the collage.
 */
enum class PhotoFilter(
    val id: String,
    val displayName: String,
    val iconEmoji: String,
    val matrixValues: FloatArray
) {
    NONE(
        id = "none",
        displayName = "Normal",
        iconEmoji = "✨",
        matrixValues = floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    ),
    BLACK_AND_WHITE(
        id = "bw",
        displayName = "B&W",
        iconEmoji = "🎞️",
        matrixValues = floatArrayOf(
            0.299f, 0.587f, 0.114f, 0f, 0f,
            0.299f, 0.587f, 0.114f, 0f, 0f,
            0.299f, 0.587f, 0.114f, 0f, 0f,
            0f,     0f,     0f,     1f, 0f
        )
    ),
    SEPIA(
        id = "sepia",
        displayName = "Sepia",
        iconEmoji = "📜",
        matrixValues = floatArrayOf(
            0.393f, 0.769f, 0.189f, 0f, 0f,
            0.349f, 0.686f, 0.168f, 0f, 0f,
            0.272f, 0.534f, 0.131f, 0f, 0f,
            0f,     0f,     0f,     1f, 0f
        )
    ),
    VINTAGE(
        id = "vintage",
        displayName = "Vintage",
        iconEmoji = "📷",
        matrixValues = floatArrayOf(
            0.90f, 0.30f, 0.10f, 0f, 18f,
            0.15f, 0.85f, 0.15f, 0f, 10f,
            0.10f, 0.20f, 0.60f, 0f, -12f,
            0f,    0f,    0f,    1f, 0f
        )
    ),
    WARM(
        id = "warm",
        displayName = "Warm",
        iconEmoji = "☀️",
        matrixValues = floatArrayOf(
            1.20f, 0f,    0f,    0f, 10f,
            0f,    1.08f, 0f,    0f, 5f,
            0f,    0f,    0.82f, 0f, -15f,
            0f,    0f,    0f,    1f, 0f
        )
    ),
    COOL(
        id = "cool",
        displayName = "Cool",
        iconEmoji = "❄️",
        matrixValues = floatArrayOf(
            0.82f, 0f,    0f,    0f, -10f,
            0f,    0.95f, 0f,    0f, 0f,
            0f,    0f,    1.25f, 0f, 18f,
            0f,    0f,    0f,    1f, 0f
        )
    ),
    DRAMATIC(
        id = "dramatic",
        displayName = "Dramatic",
        iconEmoji = "🎭",
        matrixValues = floatArrayOf(
            1.35f, 0f,    0f,    0f, -32f,
            0f,    1.35f, 0f,    0f, -32f,
            0f,    0f,    1.35f, 0f, -32f,
            0f,    0f,    0f,    1f, 0f
        )
    ),
    FADE(
        id = "fade",
        displayName = "Fade",
        iconEmoji = "🌫️",
        matrixValues = floatArrayOf(
            0.85f, 0f,    0f,    0f, 36f,
            0f,    0.85f, 0f,    0f, 36f,
            0f,    0f,    0.85f, 0f, 36f,
            0f,    0f,    0f,    1f, 0f
        )
    );

    /**
     * Converts to Jetpack Compose ColorFilter for realtime UI rendering.
     */
    fun toComposeColorFilter(): ColorFilter? {
        if (this == NONE) return null
        return ColorFilter.colorMatrix(ColorMatrix(matrixValues))
    }

    /**
     * Converts to Android Graphics ColorFilter for high-resolution bitmap export.
     */
    fun toAndroidColorFilter(): ColorMatrixColorFilter? {
        if (this == NONE) return null
        return ColorMatrixColorFilter(android.graphics.ColorMatrix(matrixValues))
    }
}
