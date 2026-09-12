package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.OpenWith
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StickerItem
import com.example.model.StickerType
import kotlin.math.atan2
import kotlin.math.hypot

@Composable
fun StickerOverlayLayer(
    stickers: List<StickerItem>,
    selectedStickerId: String?,
    canvasWidth: Dp,
    canvasHeight: Dp,
    onSelectSticker: (String) -> Unit,
    onMoveSticker: (id: String, x: Float, y: Float) -> Unit,
    onTransformSticker: (id: String, panDeltaX: Float, panDeltaY: Float, zoomDelta: Float, rotationDelta: Float) -> Unit,
    onScaleSticker: (id: String, scale: Float) -> Unit,
    onRotateSticker: (id: String, rotation: Float) -> Unit,
    onDeleteSticker: (id: String) -> Unit,
    onDuplicateSticker: (id: String) -> Unit,
    onFlipSticker: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val canvasWidthPx = with(density) { canvasWidth.toPx() }
    val canvasHeightPx = with(density) { canvasHeight.toPx() }

    Box(modifier = modifier.fillMaxSize()) {
        stickers.forEach { sticker ->
            val isSelected = sticker.id == selectedStickerId

            StickerItemView(
                sticker = sticker,
                isSelected = isSelected,
                canvasWidth = canvasWidth,
                canvasHeight = canvasHeight,
                canvasWidthPx = canvasWidthPx,
                canvasHeightPx = canvasHeightPx,
                onSelect = { onSelectSticker(sticker.id) },
                onMove = { x, y -> onMoveSticker(sticker.id, x, y) },
                onTransform = { panX, panY, zoom, rot ->
                    onTransformSticker(sticker.id, panX, panY, zoom, rot)
                },
                onScale = { scale -> onScaleSticker(sticker.id, scale) },
                onRotate = { rot -> onRotateSticker(sticker.id, rot) },
                onDelete = { onDeleteSticker(sticker.id) },
                onDuplicate = { onDuplicateSticker(sticker.id) },
                onFlip = { onFlipSticker(sticker.id) }
            )
        }
    }
}

@Composable
private fun StickerItemView(
    sticker: StickerItem,
    isSelected: Boolean,
    canvasWidth: Dp,
    canvasHeight: Dp,
    canvasWidthPx: Float,
    canvasHeightPx: Float,
    onSelect: () -> Unit,
    onMove: (Float, Float) -> Unit,
    onTransform: (panX: Float, panY: Float, zoom: Float, rot: Float) -> Unit,
    onScale: (Float) -> Unit,
    onRotate: (Float) -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    onFlip: () -> Unit
) {
    val baseSize = sticker.baseSizeDp.dp * sticker.scale
    val halfSize = baseSize / 2f

    // Center coordinates relative to canvas
    val centerX = canvasWidth * sticker.x
    val centerY = canvasHeight * sticker.y

    // Left and top offsets so that (centerX, centerY) is the anchor center
    val offsetX = centerX - halfSize
    val offsetY = centerY - halfSize

    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .size(baseSize)
            .graphicsLayer {
                rotationZ = sticker.rotation
                scaleX = if (sticker.isFlipped) -1f else 1f
            }
            .pointerInput(sticker.id) {
                detectTransformGestures { _, pan, zoom, rotation ->
                    if (canvasWidthPx > 0f && canvasHeightPx > 0f) {
                        val panDeltaX = pan.x / canvasWidthPx
                        val panDeltaY = pan.y / canvasHeightPx
                        onTransform(panDeltaX, panDeltaY, zoom, rotation)
                    }
                }
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect
            )
            .testTag("sticker_item_${sticker.id}"),
        contentAlignment = Alignment.Center
    ) {
        // Transparent Sticker Content
        when (sticker.type) {
            StickerType.EMOJI, StickerType.VECTOR -> {
                Text(
                    text = sticker.content,
                    fontSize = (sticker.baseSizeDp * sticker.scale * 0.72f).sp,
                    lineHeight = (sticker.baseSizeDp * sticker.scale * 0.75f).sp
                )
            }
            StickerType.BADGE -> {
                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = Color(sticker.badgeBgColor),
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .border(
                            width = (2.5f * sticker.scale).coerceIn(1.5f, 5f).dp,
                            color = Color.White,
                            shape = RoundedCornerShape(percent = 50)
                        )
                ) {
                    Text(
                        text = sticker.content,
                        color = Color(sticker.badgeTextColor),
                        fontWeight = FontWeight.Black,
                        fontSize = (sticker.baseSizeDp * sticker.scale * 0.28f).coerceAtLeast(10f).sp,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(
                            horizontal = (12f * sticker.scale).coerceIn(8f, 24f).dp,
                            vertical = (6f * sticker.scale).coerceIn(4f, 14f).dp
                        )
                    )
                }
            }
        }

        // Selected bounding border and handles
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
            )

            // Top-Right: Delete handle (❌)
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.error,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = (-10).dp)
                    .size(26.dp)
                    .clickable(onClick = onDelete)
                    .testTag("sticker_delete_${sticker.id}")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete sticker",
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Top-Left: Duplicate handle (➕)
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-10).dp, y = (-10).dp)
                    .size(26.dp)
                    .clickable(onClick = onDuplicate)
                    .testTag("sticker_duplicate_${sticker.id}")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Duplicate sticker",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Bottom-Left: Flip handle (↔)
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondary,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = (-10).dp, y = 10.dp)
                    .size(26.dp)
                    .clickable(onClick = onFlip)
                    .testTag("sticker_flip_${sticker.id}")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Flip,
                        contentDescription = "Flip sticker",
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Bottom-Right: Resize & Rotate handle (↔️ / 🔄)
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.tertiary,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 10.dp, y = 10.dp)
                    .size(26.dp)
                    .pointerInput(sticker.id) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            // Dragging bottom-right corner changes scale
                            val scaleDelta = (dragAmount.x + dragAmount.y) / 100f
                            val newScale = (sticker.scale + scaleDelta).coerceIn(0.3f, 4.0f)
                            onScale(newScale)
                        }
                    }
                    .testTag("sticker_resize_handle_${sticker.id}")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.OpenWith,
                        contentDescription = "Resize sticker",
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
