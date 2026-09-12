package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.BorderStyle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BackgroundMode
import com.example.model.CollageStyle
import com.example.model.FrameCategory
import com.example.model.FrameTemplate
import com.example.model.PatternStyle
import com.example.model.PhotoFilter
import com.example.model.PhotoSlot
import com.example.model.SocialAspectRatio
import com.example.model.StickerCatalog
import com.example.model.StickerItem
import com.example.model.StickerPreset
import com.example.model.StickerType

data class GradientPreset(
    val name: String,
    val colors: List<Long>
)

val gradientPresets = listOf(
    GradientPreset("Sunset", listOf(0xFFFF5E36, 0xFFFFAE33)),
    GradientPreset("Aurora", listOf(0xFF8A2387, 0xFFE94057, 0xFFF27121)),
    GradientPreset("Violet Bliss", listOf(0xFF667EEA, 0xFF764BA2)),
    GradientPreset("Ocean Deep", listOf(0xFF2E3192, 0xFF1BFFFF)),
    GradientPreset("Cotton Candy", listOf(0xFFFF9A9E, 0xFFFAD0C4, 0xFFFBC2EB)),
    GradientPreset("Emerald Mint", listOf(0xFF0BA360, 0xFF3CBA92)),
    GradientPreset("Cyber Neon", listOf(0xFFFF0844, 0xFFFFB199)),
    GradientPreset("Midnight", listOf(0xFF0F2027, 0xFF203A43, 0xFF2C5364))
)

val solidColorPalette = listOf(
    0xFFFFFFFF, // Pure White
    0xFFF8F9FA, // Off-white
    0xFFFAF3E0, // Cream
    0xFFFFE5EC, // Blush
    0xFFE8F5E9, // Mint
    0xFFE3F2FD, // Sky
    0xFFF3E5F5, // Lavender
    0xFFFFE0B2, // Peach
    0xFFFFCDD2, // Coral
    0xFFFFEB3B, // Warm Yellow
    0xFF424242, // Charcoal
    0xFF121216  // Obsidian Dark
)

val borderColors = listOf(
    0xFFFFFFFF, // White
    0xFF1E1B2E, // Dark Charcoal
    0xFFFFD700, // Gold
    0xFFFF6584, // Coral Pink
    0xFF6C5CE7, // Violet
    0xFF00CEC9, // Teal
    0xFFE0E0E0, // Silver Gray
    0xFFFF7675  // Rose
)

data class PatternColorTheme(
    val name: String,
    val baseColor: Long,
    val elemColor: Long
)

val patternColorThemes = listOf(
    PatternColorTheme("Blush", 0xFFFFF0F5, 0xFFFF6584),
    PatternColorTheme("Classic", 0xFFFFFFFF, 0xFF222222),
    PatternColorTheme("Slate", 0xFFF1F5F9, 0xFF64748B),
    PatternColorTheme("Lavender", 0xFFF5F3FF, 0xFF7C3AED),
    PatternColorTheme("Mint", 0xFFECFDF5, 0xFF059669),
    PatternColorTheme("Honey", 0xFFFFFBEB, 0xFFD97706),
    PatternColorTheme("Midnight", 0xFF0F172A, 0xFF38BDF8)
)

@Composable
fun CollageControlsView(
    photoCount: Int,
    onPhotoCountChange: (Int) -> Unit,
    selectedCategory: FrameCategory,
    onCategoryChange: (FrameCategory) -> Unit,
    availableTemplates: List<FrameTemplate>,
    currentTemplate: FrameTemplate,
    onTemplateSelect: (FrameTemplate) -> Unit,
    aspectRatio: SocialAspectRatio,
    onAspectRatioChange: (SocialAspectRatio) -> Unit,
    style: CollageStyle,
    slots: List<PhotoSlot> = emptyList(),
    selectedSlotId: Int? = null,
    onSelectSlot: (Int) -> Unit = {},
    onSlotFilterChange: (slotId: Int, PhotoFilter) -> Unit = { _, _ -> },
    onApplyFilterToAll: (PhotoFilter) -> Unit = {},
    stickers: List<StickerItem> = emptyList(),
    selectedStickerId: String? = null,
    onAddSticker: (StickerPreset) -> Unit = {},
    onSelectSticker: (String) -> Unit = {},
    onRemoveSticker: (String) -> Unit = {},
    onClearAllStickers: () -> Unit = {},
    onBackgroundModeChange: (BackgroundMode) -> Unit,
    onSolidColorChange: (Long) -> Unit,
    onGradientChange: (List<Long>) -> Unit,
    onPatternChange: (PatternStyle, baseColor: Long?, elemColor: Long?) -> Unit,
    onPatternScaleChange: (Float) -> Unit,
    onSlotBorderWidthChange: (Float) -> Unit,
    onSlotBorderColorChange: (Long) -> Unit,
    onFrameBorderWidthChange: (Float) -> Unit,
    onFrameBorderColorChange: (Long) -> Unit,
    onCornerRadiusChange: (Float) -> Unit,
    onOuterPaddingChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Layouts", "Stickers", "Filters", "Shapes", "Background", "Borders", "Aspect")

    Column(modifier = modifier.fillMaxWidth()) {
        // 1. Photo Count Selector Bar (2 to 8)
        PhotoCountSelector(
            currentCount = photoCount,
            onSelect = onPhotoCountChange,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        // 2. Control Tabs
        ScrollableTabRow(
            selectedTabIndex = activeTab,
            edgePadding = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = activeTab == index,
                    onClick = { activeTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = {
                        when (index) {
                            0 -> Icon(Icons.Default.Dashboard, contentDescription = null, modifier = Modifier.size(18.dp))
                            1 -> Icon(Icons.Default.Stars, contentDescription = null, modifier = Modifier.size(18.dp))
                            2 -> Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(18.dp))
                            3 -> Icon(Icons.Default.Category, contentDescription = null, modifier = Modifier.size(18.dp))
                            4 -> Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(18.dp))
                            5 -> Icon(Icons.Default.BorderStyle, contentDescription = null, modifier = Modifier.size(18.dp))
                            else -> Icon(Icons.Default.AspectRatio, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3. Tab Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            when (activeTab) {
                0 -> {
                    // Templates for current Category & Count
                    TemplateCarousel(
                        templates = availableTemplates,
                        selectedTemplate = currentTemplate,
                        onSelect = onTemplateSelect
                    )
                }
                1 -> {
                    // Stickers overlay catalog & active stickers management
                    StickerControlsSection(
                        stickers = stickers,
                        selectedStickerId = selectedStickerId,
                        onAddSticker = onAddSticker,
                        onSelectSticker = onSelectSticker,
                        onRemoveSticker = onRemoveSticker,
                        onClearAllStickers = onClearAllStickers
                    )
                }
                2 -> {
                    // Photo Filters: B&W, Sepia, Vintage, Warm, Cool, etc. per slot & all slots
                    FilterControlsSection(
                        slots = slots,
                        photoCount = photoCount,
                        selectedSlotId = selectedSlotId,
                        onSelectSlot = onSelectSlot,
                        onSlotFilterChange = onSlotFilterChange,
                        onApplyFilterToAll = onApplyFilterToAll
                    )
                }
                3 -> {
                    // Frame Design Categories: Rectangular, Triangle, Quad, Heart, Circle
                    CategorySelector(
                        currentCategory = selectedCategory,
                        onSelect = onCategoryChange
                    )
                }
                4 -> {
                    // Background Customization: Solid colors, Gradients, Patterns
                    BackgroundCustomizer(
                        style = style,
                        onModeChange = onBackgroundModeChange,
                        onSolidSelect = onSolidColorChange,
                        onGradientSelect = onGradientChange,
                        onPatternSelect = onPatternChange,
                        onPatternScaleChange = onPatternScaleChange
                    )
                }
                5 -> {
                    // Border Customization: Slot Borders, Main Frame Border, Radius, Padding
                    BorderCustomizer(
                        style = style,
                        onSlotBorderWidthChange = onSlotBorderWidthChange,
                        onSlotBorderColorChange = onSlotBorderColorChange,
                        onFrameBorderWidthChange = onFrameBorderWidthChange,
                        onFrameBorderColorChange = onFrameBorderColorChange,
                        onCornerRadiusChange = onCornerRadiusChange,
                        onOuterPaddingChange = onOuterPaddingChange
                    )
                }
                6 -> {
                    // Aspect Ratio Selector
                    AspectRatioSelector(
                        currentRatio = aspectRatio,
                        onSelect = onAspectRatioChange
                    )
                }
            }
        }
    }
}

@Composable
private fun BackgroundCustomizer(
    style: CollageStyle,
    onModeChange: (BackgroundMode) -> Unit,
    onSolidSelect: (Long) -> Unit,
    onGradientSelect: (List<Long>) -> Unit,
    onPatternSelect: (PatternStyle, baseColor: Long?, elemColor: Long?) -> Unit,
    onPatternScaleChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Mode Selector: Solid | Gradient | Pattern
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BackgroundMode.values().forEach { mode ->
                val isSelected = style.backgroundMode == mode
                FilterChip(
                    selected = isSelected,
                    onClick = { onModeChange(mode) },
                    label = { Text(mode.displayName, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    } else null,
                    modifier = Modifier.testTag("bg_mode_${mode.name.lowercase()}")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        when (style.backgroundMode) {
            BackgroundMode.SOLID -> {
                Text(
                    text = "Solid Color Palette:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    solidColorPalette.forEach { colorVal ->
                        val isSelected = style.backgroundColor == colorVal
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(colorVal))
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0x33000000),
                                    shape = CircleShape
                                )
                                .clickable { onSolidSelect(colorVal) }
                                .testTag("color_${colorVal.toString(16)}"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (colorVal > 0xFF888888) Color.Black else Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            BackgroundMode.GRADIENT -> {
                Text(
                    text = "Gradient Presets:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    gradientPresets.forEach { preset ->
                        val isSelected = style.backgroundGradient == preset.colors
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { onGradientSelect(preset.colors) }
                                .testTag("gradient_${preset.name.lowercase().replace(" ", "_")}")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 54.dp, height = 42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Brush.linearGradient(preset.colors.map { Color(it) }))
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0x33000000),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = preset.name,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            BackgroundMode.PATTERN -> {
                Text(
                    text = "Pattern Designs:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Pattern Type chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PatternStyle.values().forEach { pattern ->
                        val isSelected = style.patternStyle == pattern
                        FilterChip(
                            selected = isSelected,
                            onClick = { onPatternSelect(pattern, null, null) },
                            label = { Text("${pattern.iconEmoji} ${pattern.displayName}") },
                            modifier = Modifier.testTag("pattern_${pattern.name.lowercase()}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Pattern Color Themes
                Text(
                    text = "Pattern Color Palette:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    patternColorThemes.forEach { theme ->
                        val isSelected = style.patternBaseColor == theme.baseColor && style.patternElementColor == theme.elemColor
                        ElevatedCard(
                            onClick = { onPatternSelect(style.patternStyle, theme.baseColor, theme.elemColor) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .testTag("pattern_theme_${theme.name.lowercase()}")
                                .border(
                                    width = if (isSelected) 2.5.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0x22000000),
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(Color(theme.baseColor))
                                        .border(0.5.dp, Color.Gray, CircleShape)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(Color(theme.elemColor))
                                )
                                Text(theme.name, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Pattern Scale Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Pattern Density (${style.patternScaleDp.toInt()}dp)", style = MaterialTheme.typography.bodySmall)
                    Slider(
                        value = style.patternScaleDp,
                        onValueChange = onPatternScaleChange,
                        valueRange = 16f..64f,
                        modifier = Modifier
                            .width(180.dp)
                            .testTag("slider_pattern_density")
                    )
                }
            }
        }
    }
}

@Composable
private fun BorderCustomizer(
    style: CollageStyle,
    onSlotBorderWidthChange: (Float) -> Unit,
    onSlotBorderColorChange: (Long) -> Unit,
    onFrameBorderWidthChange: (Float) -> Unit,
    onFrameBorderColorChange: (Long) -> Unit,
    onCornerRadiusChange: (Float) -> Unit,
    onOuterPaddingChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // 1. Photo Borders
        Text(
            text = "Photo Borders (Individual Slots):",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Thickness (${style.slotBorderWidthDp.toInt()}dp)", style = MaterialTheme.typography.bodySmall)
            Slider(
                value = style.slotBorderWidthDp,
                onValueChange = onSlotBorderWidthChange,
                valueRange = 0f..20f,
                modifier = Modifier
                    .width(180.dp)
                    .testTag("slider_slot_border_width")
            )
        }

        // Slot Border Color Swatches
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Color:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
            borderColors.forEach { col ->
                val isSelected = style.slotBorderColor == col
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(col))
                        .border(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0x44000000),
                            shape = CircleShape
                        )
                        .clickable { onSlotBorderColorChange(col) }
                        .testTag("slot_border_color_${col.toString(16)}"),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = if (col > 0xFF888888) Color.Black else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Main Frame Border
        Text(
            text = "Main Frame Border (Outer Frame):",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Thickness (${style.frameBorderWidthDp.toInt()}dp)", style = MaterialTheme.typography.bodySmall)
            Slider(
                value = style.frameBorderWidthDp,
                onValueChange = onFrameBorderWidthChange,
                valueRange = 0f..24f,
                modifier = Modifier
                    .width(180.dp)
                    .testTag("slider_frame_border_width")
            )
        }

        // Frame Border Color Swatches
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Color:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
            borderColors.forEach { col ->
                val isSelected = style.frameBorderColor == col
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(col))
                        .border(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0x44000000),
                            shape = CircleShape
                        )
                        .clickable { onFrameBorderColorChange(col) }
                        .testTag("frame_border_color_${col.toString(16)}"),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = if (col > 0xFF888888) Color.Black else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3. Radius & Outer Padding
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Corner Radius (${style.cornerRadiusDp.toInt()}dp)", style = MaterialTheme.typography.bodySmall)
            Slider(
                value = style.cornerRadiusDp,
                onValueChange = onCornerRadiusChange,
                valueRange = 0f..32f,
                modifier = Modifier
                    .width(180.dp)
                    .testTag("slider_corner_radius")
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Outer Padding (${style.outerPaddingDp.toInt()}dp)", style = MaterialTheme.typography.bodySmall)
            Slider(
                value = style.outerPaddingDp,
                onValueChange = onOuterPaddingChange,
                valueRange = 0f..28f,
                modifier = Modifier
                    .width(180.dp)
                    .testTag("slider_outer_padding")
            )
        }
    }
}

@Composable
private fun PhotoCountSelector(
    currentCount: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Photos:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(end = 4.dp)
        )
        for (count in 2..8) {
            val isSelected = currentCount == count
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(count) },
                label = {
                    Text(
                        text = "$count",
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.testTag("photo_count_$count")
            )
        }
    }
}

@Composable
private fun CategorySelector(
    currentCategory: FrameCategory,
    onSelect: (FrameCategory) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Choose Frame Design Style:",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FrameCategory.values().forEach { category ->
                val isSelected = currentCategory == category
                ElevatedCard(
                    onClick = { onSelect(category) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .testTag("category_${category.name.lowercase()}")
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(14.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = category.iconEmoji, fontSize = 18.sp)
                        Text(
                            text = category.displayName,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TemplateCarousel(
    templates: List<FrameTemplate>,
    selectedTemplate: FrameTemplate,
    onSelect: (FrameTemplate) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Select Layout (${templates.size} available):",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            templates.forEach { template ->
                val isSelected = template.id == selectedTemplate.id
                ElevatedCard(
                    onClick = { onSelect(template) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .width(110.dp)
                        .testTag("template_${template.id}")
                        .border(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MiniTemplateThumbnail(
                            template = template,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE9ECEF))
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = template.name,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1,
                            color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniTemplateThumbnail(
    template: FrameTemplate,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.padding(2.dp)) {
        template.slots.forEach { slot ->
            Box(
                modifier = Modifier
                    .fillMaxWidth(slot.width)
                    .fillMaxWidth(slot.height)
                    .background(Color(0xFF4A6572).copy(alpha = 0.5f))
                    .border(0.5.dp, Color.White)
            )
        }
    }
}

@Composable
private fun AspectRatioSelector(
    currentRatio: SocialAspectRatio,
    onSelect: (SocialAspectRatio) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Social Media Format:",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SocialAspectRatio.values().forEach { ratio ->
                val isSelected = currentRatio == ratio
                ElevatedCard(
                    onClick = { onSelect(ratio) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .testTag("ratio_${ratio.title.replace(':', '_')}")
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(14.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = ratio.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = ratio.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSelected) MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterControlsSection(
    slots: List<PhotoSlot>,
    photoCount: Int,
    selectedSlotId: Int?,
    onSelectSlot: (Int) -> Unit,
    onSlotFilterChange: (slotId: Int, PhotoFilter) -> Unit,
    onApplyFilterToAll: (PhotoFilter) -> Unit
) {
    val activeSlotId = selectedSlotId ?: 0
    val activeSlot = slots.find { it.id == activeSlotId }
    val currentFilter = activeSlot?.filter ?: PhotoFilter.NONE

    Column(modifier = Modifier.fillMaxWidth()) {
        // 1. Slot Selector row
        Text(
            text = "1. Select Photo to Filter:",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (i in 0 until photoCount) {
                val slot = slots.find { it.id == i }
                val isSlotSelected = (selectedSlotId == i) || (selectedSlotId == null && i == 0)
                val filter = slot?.filter ?: PhotoFilter.NONE
                ElevatedCard(
                    onClick = { onSelectSlot(i) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = if (isSlotSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .testTag("filter_slot_chip_$i")
                        .border(
                            width = if (isSlotSelected) 2.dp else 1.dp,
                            color = if (isSlotSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Photo #${i + 1}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSlotSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${filter.iconEmoji} ${filter.displayName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSlotSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Filter Presets
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "2. Filter for Photo #${activeSlotId + 1}:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${currentFilter.iconEmoji} ${currentFilter.displayName}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PhotoFilter.values().forEach { filter ->
                val isSelected = currentFilter == filter
                ElevatedCard(
                    onClick = { onSlotFilterChange(activeSlotId, filter) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .testTag("preset_filter_${filter.id}")
                        .border(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = filter.iconEmoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = filter.displayName,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Quick Action: Unify Collage
        Text(
            text = "3. Quick Action — Apply to All Photos:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                PhotoFilter.NONE,
                PhotoFilter.BLACK_AND_WHITE,
                PhotoFilter.SEPIA,
                PhotoFilter.VINTAGE,
                PhotoFilter.WARM,
                PhotoFilter.COOL
            ).forEach { filter ->
                FilterChip(
                    selected = false,
                    onClick = { onApplyFilterToAll(filter) },
                    label = { Text("All: ${filter.iconEmoji} ${filter.displayName}", fontSize = 12.sp) },
                    modifier = Modifier.testTag("apply_all_filter_${filter.id}")
                )
            }
        }
    }
}

@Composable
private fun StickerControlsSection(
    stickers: List<StickerItem>,
    selectedStickerId: String?,
    onAddSticker: (StickerPreset) -> Unit,
    onSelectSticker: (String) -> Unit,
    onRemoveSticker: (String) -> Unit,
    onClearAllStickers: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(StickerCatalog.categories.first()) }
    val presets = remember(selectedCategory) { StickerCatalog.getPresets(selectedCategory) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // 1. Category chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StickerCatalog.categories.forEach { category ->
                val isSelected = category == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = category },
                    label = { Text(category, fontSize = 13.sp) },
                    modifier = Modifier.testTag("sticker_category_$category")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Sticker Presets
        Text(
            text = "Tap any sticker to add to collage:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))

        // 2-row horizontal scrolling grid of stickers
        val chunkedPresets = remember(presets) {
            val half = (presets.size + 1) / 2
            listOf(presets.take(half), presets.drop(half))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            chunkedPresets.forEach { rowPresets ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rowPresets.forEach { preset ->
                        ElevatedCard(
                            onClick = { onAddSticker(preset) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier
                                .testTag("sticker_preset_${preset.id}")
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                when (preset.type) {
                                    StickerType.EMOJI, StickerType.VECTOR -> {
                                        Text(preset.content, fontSize = 22.sp)
                                    }
                                    StickerType.BADGE -> {
                                        Surface(
                                            shape = RoundedCornerShape(percent = 50),
                                            color = Color(preset.badgeBgColor),
                                            modifier = Modifier.border(
                                                width = 1.dp,
                                                color = Color.White,
                                                shape = RoundedCornerShape(percent = 50)
                                            )
                                        ) {
                                            Text(
                                                text = preset.content,
                                                color = Color(preset.badgeTextColor),
                                                fontWeight = FontWeight.Black,
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = preset.displayName,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Active Stickers Management
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Active Stickers (${stickers.size}):",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (stickers.isNotEmpty()) {
                TextButton(
                    onClick = onClearAllStickers,
                    modifier = Modifier.testTag("clear_all_stickers_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Clear All",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (stickers.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("💡", fontSize = 20.sp)
                    Text(
                        text = "Tap any sticker above to place it on your collage. You can resize, rotate, and reposition them freely!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stickers.forEachIndexed { index, sticker ->
                    val isStickerSelected = sticker.id == selectedStickerId
                    ElevatedCard(
                        onClick = { onSelectSticker(sticker.id) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = if (isStickerSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .testTag("active_sticker_chip_${sticker.id}")
                            .border(
                                width = if (isStickerSelected) 2.dp else 1.dp,
                                color = if (isStickerSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = when (sticker.type) {
                                    StickerType.EMOJI, StickerType.VECTOR -> sticker.content
                                    StickerType.BADGE -> "🏷️ ${sticker.content}"
                                },
                                fontSize = 16.sp
                            )
                            Text(
                                text = "#${index + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isStickerSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                            IconButton(
                                onClick = { onRemoveSticker(sticker.id) },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
