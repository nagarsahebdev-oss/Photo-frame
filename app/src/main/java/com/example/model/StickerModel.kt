package com.example.model

import java.util.UUID

/**
 * Types of stickers supported on the collage canvas.
 */
enum class StickerType {
    EMOJI,
    BADGE,
    VECTOR
}

/**
 * Represents an individual sticker placed on the collage canvas.
 * All positions are normalized (0f..1f) relative to the collage bounds
 * so they scale perfectly regardless of preview display size or export resolution.
 */
data class StickerItem(
    val id: String = UUID.randomUUID().toString(),
    val type: StickerType = StickerType.EMOJI,
    val content: String, // Emoji char (e.g., "✨") or badge text (e.g., "VIBES")
    val x: Float = 0.5f, // Normalized center X (0f..1f)
    val y: Float = 0.5f, // Normalized center Y (0f..1f)
    val scale: Float = 1.0f, // Scale factor (0.3f..4.0f)
    val rotation: Float = 0f, // Rotation in degrees (0f..360f)
    val isFlipped: Boolean = false,
    val badgeBgColor: Long = 0xFFFF4081,
    val badgeTextColor: Long = 0xFFFFFFFF,
    val baseSizeDp: Float = 64f
)

/**
 * Definition for a sticker preset in the sticker catalog.
 */
data class StickerPreset(
    val id: String,
    val type: StickerType,
    val content: String,
    val displayName: String,
    val category: String,
    val badgeBgColor: Long = 0xFFFF4081,
    val badgeTextColor: Long = 0xFFFFFFFF,
    val defaultScale: Float = 1.0f
)

/**
 * Curated library of fun, transparent-background stickers.
 */
object StickerCatalog {

    val categories = listOf(
        "Popular",
        "Emojis",
        "Badges",
        "Love & Mood",
        "Party & Fun",
        "Summer & Nature"
    )

    val presets: List<StickerPreset> = listOf(
        // Popular
        StickerPreset("pop_sparkles", StickerType.EMOJI, "✨", "Sparkles", "Popular"),
        StickerPreset("pop_heart_eyes", StickerType.EMOJI, "😍", "Heart Eyes", "Popular"),
        StickerPreset("pop_fire", StickerType.EMOJI, "🔥", "Fire", "Popular"),
        StickerPreset("pop_sunglasses", StickerType.EMOJI, "😎", "Cool", "Popular"),
        StickerPreset("pop_crown", StickerType.EMOJI, "👑", "Crown", "Popular"),
        StickerPreset("pop_party", StickerType.EMOJI, "🎉", "Party", "Popular"),
        StickerPreset("pop_badge_vibes", StickerType.BADGE, "VIBES", "Vibes", "Popular", 0xFFFF2A6D, 0xFFFFFFFF),
        StickerPreset("pop_badge_bestday", StickerType.BADGE, "BEST DAY", "Best Day", "Popular", 0xFF05D9E8, 0xFF01012B),
        StickerPreset("pop_badge_love", StickerType.BADGE, "LOVE", "Love", "Popular", 0xFFFF4081, 0xFFFFFFFF),

        // Emojis
        StickerPreset("emo_star", StickerType.EMOJI, "🌟", "Star", "Emojis"),
        StickerPreset("emo_heart", StickerType.EMOJI, "❤️", "Red Heart", "Emojis"),
        StickerPreset("emo_pink_heart", StickerType.EMOJI, "💖", "Sparkle Heart", "Emojis"),
        StickerPreset("emo_rainbow", StickerType.EMOJI, "🌈", "Rainbow", "Emojis"),
        StickerPreset("emo_celebrate", StickerType.EMOJI, "🥳", "Party Face", "Emojis"),
        StickerPreset("emo_butterfly", StickerType.EMOJI, "🦋", "Butterfly", "Emojis"),
        StickerPreset("emo_flower", StickerType.EMOJI, "🌸", "Cherry Blossom", "Emojis"),
        StickerPreset("emo_lightning", StickerType.EMOJI, "⚡", "Lightning", "Emojis"),
        StickerPreset("emo_balloon", StickerType.EMOJI, "🎈", "Balloon", "Emojis"),
        StickerPreset("emo_hundred", StickerType.EMOJI, "💯", "100", "Emojis"),
        StickerPreset("emo_camera", StickerType.EMOJI, "📸", "Camera", "Emojis"),
        StickerPreset("emo_dizzy", StickerType.EMOJI, "💫", "Dizzy Star", "Emojis"),
        StickerPreset("emo_diamond", StickerType.EMOJI, "💎", "Diamond", "Emojis"),
        StickerPreset("emo_peace", StickerType.EMOJI, "✌️", "Peace", "Emojis"),
        StickerPreset("emo_hug", StickerType.EMOJI, "🤗", "Hug", "Emojis"),

        // Badges & Stamps
        StickerPreset("bdg_vibes", StickerType.BADGE, "VIBES", "Vibes", "Badges", 0xFFFF2A6D, 0xFFFFFFFF),
        StickerPreset("bdg_love", StickerType.BADGE, "LOVE", "Love", "Badges", 0xFFFF4081, 0xFFFFFFFF),
        StickerPreset("bdg_bestday", StickerType.BADGE, "BEST DAY", "Best Day", "Badges", 0xFF00E676, 0xFF1B5E20),
        StickerPreset("bdg_summer", StickerType.BADGE, "SUMMER", "Summer", "Badges", 0xFFFFAB00, 0xFF3E2723),
        StickerPreset("bdg_happy", StickerType.BADGE, "HAPPY", "Happy", "Badges", 0xFFFFD600, 0xFF212121),
        StickerPreset("bdg_chill", StickerType.BADGE, "CHILL", "Chill", "Badges", 0xFF00B0FF, 0xFFFFFFFF),
        StickerPreset("bdg_magic", StickerType.BADGE, "MAGIC", "Magic", "Badges", 0xFF7C4DFF, 0xFFFFFFFF),
        StickerPreset("bdg_memories", StickerType.BADGE, "MEMORIES", "Memories", "Badges", 0xFFFF6E40, 0xFFFFFFFF),
        StickerPreset("bdg_ootd", StickerType.BADGE, "OOTD", "OOTD", "Badges", 0xFF212121, 0xFFFFFFFF),
        StickerPreset("bdg_smile", StickerType.BADGE, "SMILE", "Smile", "Badges", 0xFFFF4081, 0xFFFFFFFF),
        StickerPreset("bdg_lucky", StickerType.BADGE, "LUCKY", "Lucky", "Badges", 0xFF00C853, 0xFFFFFFFF),
        StickerPreset("bdg_forever", StickerType.BADGE, "FOREVER", "Forever", "Badges", 0xFFE040FB, 0xFFFFFFFF),

        // Love & Mood
        StickerPreset("lov_red_heart", StickerType.EMOJI, "❤️", "Red Heart", "Love & Mood"),
        StickerPreset("lov_pink_hearts", StickerType.EMOJI, "💕", "Two Hearts", "Love & Mood"),
        StickerPreset("lov_sparkle_heart", StickerType.EMOJI, "💖", "Sparkle Heart", "Love & Mood"),
        StickerPreset("lov_kiss", StickerType.EMOJI, "💋", "Kiss", "Love & Mood"),
        StickerPreset("lov_letter", StickerType.EMOJI, "💌", "Love Letter", "Love & Mood"),
        StickerPreset("lov_rose", StickerType.EMOJI, "🌹", "Rose", "Love & Mood"),
        StickerPreset("lov_blossom", StickerType.EMOJI, "🌸", "Blossom", "Love & Mood"),
        StickerPreset("lov_butterfly", StickerType.EMOJI, "🦋", "Butterfly", "Love & Mood"),
        StickerPreset("lov_teddy", StickerType.EMOJI, "🧸", "Teddy", "Love & Mood"),
        StickerPreset("lov_coffee", StickerType.EMOJI, "☕", "Coffee", "Love & Mood"),

        // Party & Fun
        StickerPreset("pty_party_popper", StickerType.EMOJI, "🎉", "Popper", "Party & Fun"),
        StickerPreset("pty_confetti_ball", StickerType.EMOJI, "🎊", "Confetti", "Party & Fun"),
        StickerPreset("pty_party_face", StickerType.EMOJI, "🥳", "Party Face", "Party & Fun"),
        StickerPreset("pty_champagne", StickerType.EMOJI, "🍾", "Champagne", "Party & Fun"),
        StickerPreset("pty_cake", StickerType.EMOJI, "🎂", "Cake", "Party & Fun"),
        StickerPreset("pty_pizza", StickerType.EMOJI, "🍕", "Pizza", "Party & Fun"),
        StickerPreset("pty_popcorn", StickerType.EMOJI, "🍿", "Popcorn", "Party & Fun"),
        StickerPreset("pty_guitar", StickerType.EMOJI, "🎸", "Guitar", "Party & Fun"),
        StickerPreset("pty_headphones", StickerType.EMOJI, "🎧", "Headphones", "Party & Fun"),
        StickerPreset("pty_game", StickerType.EMOJI, "🎮", "Game", "Party & Fun"),
        StickerPreset("pty_balloon", StickerType.EMOJI, "🎈", "Balloon", "Party & Fun"),
        StickerPreset("pty_fire", StickerType.EMOJI, "🔥", "Fire", "Party & Fun"),

        // Summer & Nature
        StickerPreset("sum_sun", StickerType.EMOJI, "☀️", "Sun", "Summer & Nature"),
        StickerPreset("sum_palm", StickerType.EMOJI, "🌴", "Palm Tree", "Summer & Nature"),
        StickerPreset("sum_beach", StickerType.EMOJI, "🏖️", "Beach", "Summer & Nature"),
        StickerPreset("sum_wave", StickerType.EMOJI, "🌊", "Wave", "Summer & Nature"),
        StickerPreset("sum_sunflower", StickerType.EMOJI, "🌻", "Sunflower", "Summer & Nature"),
        StickerPreset("sum_hibiscus", StickerType.EMOJI, "🌺", "Hibiscus", "Summer & Nature"),
        StickerPreset("sum_icecream", StickerType.EMOJI, "🍦", "Ice Cream", "Summer & Nature"),
        StickerPreset("sum_watermelon", StickerType.EMOJI, "🍉", "Watermelon", "Summer & Nature"),
        StickerPreset("sum_sunglasses", StickerType.EMOJI, "🕶️", "Sunglasses", "Summer & Nature"),
        StickerPreset("sum_rocket", StickerType.EMOJI, "🚀", "Rocket", "Summer & Nature"),
        StickerPreset("sum_saturn", StickerType.EMOJI, "🪐", "Saturn", "Summer & Nature")
    )

    fun getPresets(category: String): List<StickerPreset> {
        return if (category == "Popular") {
            presets.filter { it.category == "Popular" }
        } else {
            presets.filter { it.category == category }
        }
    }

    fun createStickerItem(preset: StickerPreset, posX: Float = 0.5f, posY: Float = 0.5f): StickerItem {
        return StickerItem(
            id = UUID.randomUUID().toString(),
            type = preset.type,
            content = preset.content,
            x = posX,
            y = posY,
            scale = preset.defaultScale,
            badgeBgColor = preset.badgeBgColor,
            badgeTextColor = preset.badgeTextColor
        )
    }
}
