package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.model.BackgroundMode
import com.example.model.CollageStyle
import com.example.model.FrameTemplate
import com.example.model.PhotoSlot
import com.example.model.SlotShapeSpec
import com.example.model.SlotSpec
import com.example.model.SocialAspectRatio
import com.example.model.StickerItem
import com.example.ui.shapes.HeartShape
import com.example.ui.shapes.PatternRenderer
import com.example.ui.shapes.PolygonShape
import com.example.ui.shapes.TriangleShape

@Composable
fun CollageCanvasView(
    template: FrameTemplate,
    slots: List<PhotoSlot>,
    style: CollageStyle,
    aspectRatio: SocialAspectRatio,
    selectedSlotId: Int?,
    onSelectSlot: (Int) -> Unit,
    onTransformSlot: (slotId: Int, panX: Float, panY: Float, zoom: Float) -> Unit,
    stickers: List<StickerItem> = emptyList(),
    selectedStickerId: String? = null,
    onSelectSticker: (String) -> Unit = {},
    onMoveSticker: (id: String, x: Float, y: Float) -> Unit = { _, _, _ -> },
    onTransformSticker: (id: String, panDeltaX: Float, panDeltaY: Float, zoomDelta: Float, rotationDelta: Float) -> Unit = { _, _, _, _, _ -> },
    onScaleSticker: (id: String, scale: Float) -> Unit = { _, _ -> },
    onRotateSticker: (id: String, rotation: Float) -> Unit = { _, _ -> },
    onDeleteSticker: (id: String) -> Unit = {},
    onDuplicateSticker: (id: String) -> Unit = {},
    onFlipSticker: (id: String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val frameBorderModifier = if (style.frameBorderWidthDp > 0f) {
        Modifier.border(
            width = style.frameBorderWidthDp.dp,
            color = Color(style.frameBorderColor),
            shape = RoundedCornerShape(style.cornerRadiusDp.dp)
        )
    } else Modifier

    Surface(
        modifier = modifier
            .aspectRatio(aspectRatio.ratio)
            .shadow(12.dp, RoundedCornerShape(style.cornerRadiusDp.dp))
            .clip(RoundedCornerShape(style.cornerRadiusDp.dp))
            .then(frameBorderModifier)
            .testTag("collage_canvas_container"),
        tonalElevation = 4.dp
    ) {
        // Background: Solid Color, Gradient, or Pattern
        val backgroundModifier = when (style.backgroundMode) {
            BackgroundMode.SOLID -> {
                Modifier.background(Color(style.backgroundColor))
            }
            BackgroundMode.GRADIENT -> {
                val colors = style.backgroundGradient ?: listOf(
                    style.backgroundColor,
                    style.backgroundColor
                )
                Modifier.background(Brush.linearGradient(colors.map { Color(it) }))
            }
            BackgroundMode.PATTERN -> {
                Modifier.drawBehind {
                    PatternRenderer.drawComposePattern(this, style)
                }
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .then(backgroundModifier)
                .padding(style.outerPaddingDp.dp)
        ) {
            val canvasW = maxWidth
            val canvasH = maxHeight

            template.slots.forEach { slotSpec ->
                val slotData = slots.find { it.id == slotSpec.id }
                val isSelected = selectedSlotId == slotSpec.id

                val slotShape = remember(slotSpec.shape, style.cornerRadiusDp) {
                    resolveShape(slotSpec.shape, style.cornerRadiusDp)
                }

                // Calculate relative bounds
                val leftOffset = canvasW * slotSpec.left
                val topOffset = canvasH * slotSpec.top
                val slotWidth = canvasW * slotSpec.width
                val slotHeight = canvasH * slotSpec.height

                Box(
                    modifier = Modifier
                        .offset(x = leftOffset, y = topOffset)
                        .size(width = slotWidth, height = slotHeight)
                        .padding(1.dp) // minimal subpixel separation
                ) {
                    SingleSlotView(
                        slotSpec = slotSpec,
                        slotData = slotData,
                        shape = slotShape,
                        borderWidthDp = style.slotBorderWidthDp,
                        borderColor = Color(style.slotBorderColor),
                        isSelected = isSelected,
                        onClick = { onSelectSlot(slotSpec.id) },
                        onTransform = { panX, panY, zoom ->
                            onTransformSlot(slotSpec.id, panX, panY, zoom)
                        }
                    )
                }
            }

            // Sticker Overlay Layer
            if (stickers.isNotEmpty()) {
                StickerOverlayLayer(
                    stickers = stickers,
                    selectedStickerId = selectedStickerId,
                    canvasWidth = canvasW,
                    canvasHeight = canvasH,
                    onSelectSticker = onSelectSticker,
                    onMoveSticker = onMoveSticker,
                    onTransformSticker = onTransformSticker,
                    onScaleSticker = onScaleSticker,
                    onRotateSticker = onRotateSticker,
                    onDeleteSticker = onDeleteSticker,
                    onDuplicateSticker = onDuplicateSticker,
                    onFlipSticker = onFlipSticker
                )
            }
        }
    }
}

@Composable
private fun SingleSlotView(
    slotSpec: SlotSpec,
    slotData: PhotoSlot?,
    shape: Shape,
    borderWidthDp: Float,
    borderColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    onTransform: (panX: Float, panY: Float, zoom: Float) -> Unit
) {
    val context = LocalContext.current

    val borderModifier = if (borderWidthDp > 0f) {
        Modifier.border(width = borderWidthDp.dp, color = borderColor, shape = shape)
    } else Modifier

    val selectionBorderModifier = if (isSelected) {
        Modifier.border(width = 3.dp, color = MaterialTheme.colorScheme.primary, shape = shape)
    } else Modifier

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("slot_${slotSpec.id}")
            .clip(shape)
            .background(Color(0xFFE8ECEF))
            .clickable(onClick = onClick)
            .then(borderModifier)
            .then(selectionBorderModifier)
            .pointerInput(slotSpec.id) {
                detectTransformGestures { _, pan, zoom, _ ->
                    onTransform(pan.x, pan.y, zoom)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (slotData != null && slotData.hasImage) {
            val scale = slotData.scale
            val rotation = slotData.rotation
            val offsetX = slotData.offsetX
            val offsetY = slotData.offsetY
            val isFlipped = slotData.isFlipped
            val colorFilter = slotData.filter.toComposeColorFilter()

            val graphicsModifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = (if (isFlipped) -1f else 1f) * scale
                    scaleY = scale
                    rotationZ = rotation
                    translationX = offsetX
                    translationY = offsetY
                }

            if (slotData.uri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(slotData.uri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Collage slot photo ${slotSpec.id + 1}",
                    contentScale = ContentScale.Crop,
                    colorFilter = colorFilter,
                    modifier = graphicsModifier
                )
            } else if (slotData.defaultDrawableRes != null) {
                Image(
                    painter = painterResource(id = slotData.defaultDrawableRes),
                    contentDescription = "Collage slot sample photo ${slotSpec.id + 1}",
                    contentScale = ContentScale.Crop,
                    colorFilter = colorFilter,
                    modifier = graphicsModifier
                )
            }
        } else {
            // Placeholder when empty
            Icon(
                imageVector = Icons.Default.AddAPhoto,
                contentDescription = "Add photo to slot ${slotSpec.id + 1}",
                tint = Color(0xFF9E9E9E),
                modifier = Modifier.size(28.dp)
            )
        }

        // Active selection tag in corner
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
            )
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Selected slot",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(20.dp)
            )
        }
    }
}

private fun resolveShape(shapeSpec: SlotShapeSpec, cornerRadiusDp: Float): Shape {
    return when (shapeSpec) {
        is SlotShapeSpec.Rectangle -> RoundedCornerShape(cornerRadiusDp.dp)
        is SlotShapeSpec.Circle -> CircleShape
        is SlotShapeSpec.Heart -> HeartShape()
        is SlotShapeSpec.Triangle -> TriangleShape(shapeSpec.type)
        is SlotShapeSpec.Polygon -> PolygonShape(shapeSpec.relativePoints)
    }
}
