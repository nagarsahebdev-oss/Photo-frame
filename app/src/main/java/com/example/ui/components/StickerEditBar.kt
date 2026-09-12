package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.FlipToBack
import androidx.compose.material.icons.filled.FlipToFront
import androidx.compose.material.icons.filled.Rotate90DegreesCcw
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StickerItem
import com.example.model.StickerType

@Composable
fun StickerEditBar(
    sticker: StickerItem?,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onScaleChange: (Float) -> Unit,
    onRotateLeft: () -> Unit,
    onRotateRight: () -> Unit,
    onRotate90: () -> Unit,
    onRotationChange: (Float) -> Unit,
    onFlip: () -> Unit,
    onBringToFront: () -> Unit,
    onSendToBack: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = sticker != null,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 },
        modifier = modifier
    ) {
        if (sticker == null) return@AnimatedVisibility

        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("sticker_edit_toolbar")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                // Header row: Icon/badge preview, label, and Quick Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                when (sticker.type) {
                                    StickerType.EMOJI, StickerType.VECTOR -> {
                                        Text(sticker.content, fontSize = 20.sp)
                                    }
                                    StickerType.BADGE -> {
                                        Text(
                                            text = sticker.content.take(4),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                            }
                        }

                        Column {
                            Text(
                                text = "Edit Sticker",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Size: ${(sticker.scale * 100).toInt()}% • Rot: ${sticker.rotation.toInt()}°",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        // Duplicate
                        FilledTonalIconButton(
                            onClick = onDuplicate,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("sticker_bar_duplicate")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Duplicate",
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Delete
                        IconButton(
                            onClick = onDelete,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("sticker_bar_delete")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Done / Close
                        IconButton(
                            onClick = onClose,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("sticker_bar_close")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Transform Controls Row: Scrollable button bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Zoom Out
                    FilledTonalIconButton(
                        onClick = onZoomOut,
                        modifier = Modifier.size(36.dp).testTag("sticker_bar_zoom_out")
                    ) {
                        Icon(Icons.Default.ZoomOut, contentDescription = "Zoom out", modifier = Modifier.size(18.dp))
                    }

                    // Zoom In
                    FilledTonalIconButton(
                        onClick = onZoomIn,
                        modifier = Modifier.size(36.dp).testTag("sticker_bar_zoom_in")
                    ) {
                        Icon(Icons.Default.ZoomIn, contentDescription = "Zoom in", modifier = Modifier.size(18.dp))
                    }

                    // Rotate -15°
                    FilledTonalIconButton(
                        onClick = onRotateLeft,
                        modifier = Modifier.size(36.dp).testTag("sticker_bar_rotate_left")
                    ) {
                        Icon(Icons.Default.RotateLeft, contentDescription = "Rotate -15°", modifier = Modifier.size(18.dp))
                    }

                    // Rotate +15°
                    FilledTonalIconButton(
                        onClick = onRotateRight,
                        modifier = Modifier.size(36.dp).testTag("sticker_bar_rotate_right")
                    ) {
                        Icon(Icons.Default.RotateRight, contentDescription = "Rotate +15°", modifier = Modifier.size(18.dp))
                    }

                    // Rotate 90°
                    FilledTonalIconButton(
                        onClick = onRotate90,
                        modifier = Modifier.size(36.dp).testTag("sticker_bar_rotate_90")
                    ) {
                        Icon(Icons.Default.Rotate90DegreesCcw, contentDescription = "Rotate 90°", modifier = Modifier.size(18.dp))
                    }

                    // Flip Horizontally
                    FilledTonalIconButton(
                        onClick = onFlip,
                        modifier = Modifier.size(36.dp).testTag("sticker_bar_flip")
                    ) {
                        Icon(Icons.Default.Flip, contentDescription = "Flip Horizontally", modifier = Modifier.size(18.dp))
                    }

                    // Bring to Front
                    FilledTonalIconButton(
                        onClick = onBringToFront,
                        modifier = Modifier.size(36.dp).testTag("sticker_bar_front")
                    ) {
                        Icon(Icons.Default.FlipToFront, contentDescription = "Bring to front", modifier = Modifier.size(18.dp))
                    }

                    // Send to Back
                    FilledTonalIconButton(
                        onClick = onSendToBack,
                        modifier = Modifier.size(36.dp).testTag("sticker_bar_back")
                    ) {
                        Icon(Icons.Default.FlipToBack, contentDescription = "Send to back", modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Sliders for precise Resize & Rotation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Size",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(36.dp)
                    )
                    Slider(
                        value = sticker.scale,
                        onValueChange = onScaleChange,
                        valueRange = 0.3f..3.0f,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("sticker_scale_slider"),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rot",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(36.dp)
                    )
                    Slider(
                        value = sticker.rotation,
                        onValueChange = onRotationChange,
                        valueRange = 0f..360f,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("sticker_rotation_slider"),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.secondary,
                            activeTrackColor = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }
    }
}
