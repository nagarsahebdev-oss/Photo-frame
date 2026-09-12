package com.example.model

import android.net.Uri
import androidx.annotation.DrawableRes

/**
 * Represents an individual photo slot in a collage frame.
 */
data class PhotoSlot(
    val id: Int,
    val uri: Uri? = null,
    @DrawableRes val defaultDrawableRes: Int? = null,
    val scale: Float = 1f,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val rotation: Float = 0f,
    val isFlipped: Boolean = false,
    val filter: PhotoFilter = PhotoFilter.NONE
) {
    val hasImage: Boolean
        get() = uri != null || defaultDrawableRes != null
}

/**
 * Design categories of frame layouts supported by the app.
 */
enum class FrameCategory(val displayName: String, val iconEmoji: String) {
    RECTANGULAR("Rectangular", "🔲"),
    TRIANGLE("Triangle", "🔺"),
    QUAD("Quad", "💠"),
    HEART("Heart", "❤️"),
    CIRCLE("Circle", "⭕")
}

/**
 * Aspect ratios optimized for popular social media sharing platforms.
 */
enum class SocialAspectRatio(
    val title: String,
    val subtitle: String,
    val ratio: Float,
    val widthPx: Int,
    val heightPx: Int
) {
    SQUARE_1_1("1:1", "Square (Instagram/Post)", 1f, 1080, 1080),
    PORTRAIT_4_5("4:5", "Portrait (Feed/Pin)", 4f / 5f, 1080, 1350),
    STORY_9_16("9:16", "Story / Reels / TikTok", 9f / 16f, 1080, 1920),
    LANDSCAPE_16_9("16:9", "Banner (YouTube/X)", 16f / 9f, 1920, 1080)
}

/**
 * Shape types for slots.
 */
sealed interface SlotShapeSpec {
    object Rectangle : SlotShapeSpec
    object Circle : SlotShapeSpec
    object Heart : SlotShapeSpec
    data class Polygon(val relativePoints: List<Pair<Float, Float>>) : SlotShapeSpec
    enum class TriangleType {
        UP, DOWN, LEFT, RIGHT,
        DIAGONAL_TOP_LEFT, DIAGONAL_BOTTOM_RIGHT,
        DIAGONAL_TOP_RIGHT, DIAGONAL_BOTTOM_LEFT
    }
    data class Triangle(val type: TriangleType) : SlotShapeSpec
}

/**
 * Normalized slot placement in relative 0f..1f coordinates:
 * left, top, width, height (or left, top, right, bottom).
 */
data class SlotSpec(
    val id: Int,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val shape: SlotShapeSpec = SlotShapeSpec.Rectangle
) {
    val width: Float get() = (right - left).coerceAtLeast(0.01f)
    val height: Float get() = (bottom - top).coerceAtLeast(0.01f)
}

/**
 * Frame template definition.
 */
data class FrameTemplate(
    val id: String,
    val name: String,
    val category: FrameCategory,
    val photoCount: Int,
    val slots: List<SlotSpec>
)

/**
 * Types of background styling.
 */
enum class BackgroundMode(val displayName: String) {
    SOLID("Solid Color"),
    GRADIENT("Gradient"),
    PATTERN("Pattern")
}

/**
 * Pattern styles for collage background.
 */
enum class PatternStyle(val displayName: String, val iconEmoji: String) {
    POLKA_DOTS("Polka Dots", "⚪"),
    DIAGONAL_STRIPES("Stripes", "╱"),
    GRID_MESH("Grid Mesh", "▦"),
    HEARTS("Hearts", "💕"),
    STARS("Stars", "✨"),
    TERRAZZO("Confetti", "🎉"),
    WAVY("Waves", "〰")
}

/**
 * Visual styling configuration for the frame.
 */
data class CollageStyle(
    val backgroundMode: BackgroundMode = BackgroundMode.SOLID,
    val backgroundColor: Long = 0xFFFFFFFF,
    val backgroundGradient: List<Long>? = null,
    val patternStyle: PatternStyle = PatternStyle.POLKA_DOTS,
    val patternBaseColor: Long = 0xFFFDF7F4,
    val patternElementColor: Long = 0xFFFF708D,
    val patternScaleDp: Float = 32f,

    // Border around individual photo slots
    val slotBorderColor: Long = 0xFFFFFFFF,
    val slotBorderWidthDp: Float = 4f,

    // Border around the main overall frame
    val frameBorderColor: Long = 0xFF1E1B2E,
    val frameBorderWidthDp: Float = 0f,

    // Corner radius of slots and frame
    val cornerRadiusDp: Float = 14f,
    val outerPaddingDp: Float = 12f
)
