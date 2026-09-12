package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.model.BackgroundMode
import com.example.model.CollageStyle
import com.example.model.FrameCategory
import com.example.model.FrameTemplate
import com.example.model.FrameTemplates
import com.example.model.PatternStyle
import com.example.model.PhotoFilter
import com.example.model.PhotoSlot
import com.example.model.SocialAspectRatio
import com.example.model.StickerCatalog
import com.example.model.StickerItem
import com.example.model.StickerPreset
import com.example.util.CollageBitmapExporter
import com.example.util.ExportFormat
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface UiEvent {
    data class ShowToast(val message: String) : UiEvent
    data class ShareImage(val bitmap: Bitmap) : UiEvent
    data class ShareImageUri(val uri: Uri, val mimeType: String, val caption: String) : UiEvent
}

data class CollageUiState(
    val photoCount: Int = 4,
    val selectedCategory: FrameCategory = FrameCategory.RECTANGULAR,
    val availableTemplates: List<FrameTemplate> = emptyList(),
    val currentTemplate: FrameTemplate = FrameTemplates.getDefaultTemplate(4, FrameCategory.RECTANGULAR),
    val slots: List<PhotoSlot> = emptyList(),
    val aspectRatio: SocialAspectRatio = SocialAspectRatio.SQUARE_1_1,
    val style: CollageStyle = CollageStyle(),
    val selectedSlotId: Int? = null,
    val stickers: List<StickerItem> = emptyList(),
    val selectedStickerId: String? = null,
    val isExporting: Boolean = false
)

class CollageViewModel(application: Application) : AndroidViewModel(application) {

    private val defaultPhotos = listOf(
        R.drawable.sample_nature,
        R.drawable.sample_city,
        R.drawable.sample_ocean,
        R.drawable.sample_portrait
    )

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<CollageUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    private fun createInitialState(): CollageUiState {
        val count = 4
        val category = FrameCategory.RECTANGULAR
        val templates = FrameTemplates.getTemplates(count, category)
        val template = templates.firstOrNull() ?: FrameTemplates.getDefaultTemplate(count, category)
        val initialSlots = (0 until 8).map { i ->
            PhotoSlot(
                id = i,
                defaultDrawableRes = defaultPhotos[i % defaultPhotos.size]
            )
        }

        return CollageUiState(
            photoCount = count,
            selectedCategory = category,
            availableTemplates = templates,
            currentTemplate = template,
            slots = initialSlots,
            aspectRatio = SocialAspectRatio.SQUARE_1_1,
            style = CollageStyle(
                backgroundMode = BackgroundMode.SOLID,
                backgroundColor = 0xFFFFFFFF,
                slotBorderColor = 0xFFFFFFFF,
                slotBorderWidthDp = 4f,
                frameBorderColor = 0xFF1E1B2E,
                frameBorderWidthDp = 0f,
                cornerRadiusDp = 14f,
                outerPaddingDp = 12f
            )
        )
    }

    fun setPhotoCount(count: Int) {
        if (count !in 2..8) return
        _uiState.update { state ->
            val templates = FrameTemplates.getTemplates(count, state.selectedCategory)
            val newTemplate = templates.firstOrNull()
                ?: FrameTemplates.getDefaultTemplate(count, state.selectedCategory)
            state.copy(
                photoCount = count,
                availableTemplates = templates,
                currentTemplate = newTemplate,
                selectedSlotId = null
            )
        }
    }

    fun setCategory(category: FrameCategory) {
        _uiState.update { state ->
            val templates = FrameTemplates.getTemplates(state.photoCount, category)
            val newTemplate = templates.firstOrNull()
                ?: FrameTemplates.getDefaultTemplate(state.photoCount, category)
            state.copy(
                selectedCategory = category,
                availableTemplates = templates,
                currentTemplate = newTemplate,
                selectedSlotId = null
            )
        }
    }

    fun setTemplate(template: FrameTemplate) {
        _uiState.update { state ->
            state.copy(currentTemplate = template, selectedSlotId = null)
        }
    }

    fun setAspectRatio(ratio: SocialAspectRatio) {
        _uiState.update { it.copy(aspectRatio = ratio) }
    }

    fun selectSlot(slotId: Int?) {
        _uiState.update {
            it.copy(
                selectedSlotId = slotId,
                selectedStickerId = if (slotId != null) null else it.selectedStickerId
            )
        }
    }

    fun updateSlotImage(slotId: Int, uri: Uri?) {
        if (uri == null) return
        _uiState.update { state ->
            val updated = state.slots.map { slot ->
                if (slot.id == slotId) {
                    slot.copy(uri = uri, defaultDrawableRes = null)
                } else slot
            }
            state.copy(slots = updated)
        }
    }

    fun batchUpdateImages(uris: List<Uri>) {
        if (uris.isEmpty()) return
        _uiState.update { state ->
            val updated = state.slots.mapIndexed { index, slot ->
                if (index < uris.size) {
                    slot.copy(uri = uris[index], defaultDrawableRes = null)
                } else slot
            }
            state.copy(slots = updated)
        }
    }

    fun rotateSelectedSlot() {
        val slotId = _uiState.value.selectedSlotId ?: return
        _uiState.update { state ->
            val updated = state.slots.map { slot ->
                if (slot.id == slotId) {
                    val nextRotation = (slot.rotation + 90f) % 360f
                    slot.copy(rotation = nextRotation)
                } else slot
            }
            state.copy(slots = updated)
        }
    }

    fun flipSelectedSlot() {
        val slotId = _uiState.value.selectedSlotId ?: return
        _uiState.update { state ->
            val updated = state.slots.map { slot ->
                if (slot.id == slotId) {
                    slot.copy(isFlipped = !slot.isFlipped)
                } else slot
            }
            state.copy(slots = updated)
        }
    }

    fun zoomSelectedSlot(delta: Float) {
        val slotId = _uiState.value.selectedSlotId ?: return
        _uiState.update { state ->
            val updated = state.slots.map { slot ->
                if (slot.id == slotId) {
                    val newScale = (slot.scale + delta).coerceIn(0.5f, 3.5f)
                    slot.copy(scale = newScale)
                } else slot
            }
            state.copy(slots = updated)
        }
    }

    fun panSelectedSlot(dx: Float, dy: Float) {
        val slotId = _uiState.value.selectedSlotId ?: return
        _uiState.update { state ->
            val updated = state.slots.map { slot ->
                if (slot.id == slotId) {
                    slot.copy(
                        offsetX = slot.offsetX + dx,
                        offsetY = slot.offsetY + dy
                    )
                } else slot
            }
            state.copy(slots = updated)
        }
    }

    fun resetSelectedSlot() {
        val slotId = _uiState.value.selectedSlotId ?: return
        _uiState.update { state ->
            val updated = state.slots.map { slot ->
                if (slot.id == slotId) {
                    slot.copy(
                        scale = 1f,
                        offsetX = 0f,
                        offsetY = 0f,
                        rotation = 0f,
                        isFlipped = false,
                        filter = PhotoFilter.NONE
                    )
                } else slot
            }
            state.copy(slots = updated)
        }
    }

    fun setSlotFilter(slotId: Int, filter: PhotoFilter) {
        _uiState.update { state ->
            val updated = state.slots.map { slot ->
                if (slot.id == slotId) {
                    slot.copy(filter = filter)
                } else slot
            }
            state.copy(slots = updated)
        }
    }

    fun setSelectedSlotFilter(filter: PhotoFilter) {
        val slotId = _uiState.value.selectedSlotId ?: return
        setSlotFilter(slotId, filter)
    }

    fun applyFilterToAllSlots(filter: PhotoFilter) {
        _uiState.update { state ->
            val updated = state.slots.map { slot ->
                slot.copy(filter = filter)
            }
            state.copy(slots = updated)
        }
    }

    // --- Background Customization ---

    fun setBackgroundMode(mode: BackgroundMode) {
        _uiState.update { it.copy(style = it.style.copy(backgroundMode = mode)) }
    }

    fun setSolidBackgroundColor(color: Long) {
        _uiState.update {
            it.copy(
                style = it.style.copy(
                    backgroundMode = BackgroundMode.SOLID,
                    backgroundColor = color
                )
            )
        }
    }

    fun setGradientBackground(colors: List<Long>) {
        _uiState.update {
            it.copy(
                style = it.style.copy(
                    backgroundMode = BackgroundMode.GRADIENT,
                    backgroundGradient = colors
                )
            )
        }
    }

    fun setPattern(
        pattern: PatternStyle,
        baseColor: Long? = null,
        elemColor: Long? = null
    ) {
        _uiState.update { state ->
            state.copy(
                style = state.style.copy(
                    backgroundMode = BackgroundMode.PATTERN,
                    patternStyle = pattern,
                    patternBaseColor = baseColor ?: state.style.patternBaseColor,
                    patternElementColor = elemColor ?: state.style.patternElementColor
                )
            )
        }
    }

    fun setPatternScale(scaleDp: Float) {
        _uiState.update { it.copy(style = it.style.copy(patternScaleDp = scaleDp)) }
    }

    // --- Border Customization ---

    fun setSlotBorderWidth(widthDp: Float) {
        _uiState.update { it.copy(style = it.style.copy(slotBorderWidthDp = widthDp)) }
    }

    fun setSlotBorderColor(color: Long) {
        _uiState.update { it.copy(style = it.style.copy(slotBorderColor = color)) }
    }

    fun setFrameBorderWidth(widthDp: Float) {
        _uiState.update { it.copy(style = it.style.copy(frameBorderWidthDp = widthDp)) }
    }

    fun setFrameBorderColor(color: Long) {
        _uiState.update { it.copy(style = it.style.copy(frameBorderColor = color)) }
    }

    fun setCornerRadius(radius: Float) {
        _uiState.update { it.copy(style = it.style.copy(cornerRadiusDp = radius)) }
    }

    fun setOuterPadding(padding: Float) {
        _uiState.update { it.copy(style = it.style.copy(outerPaddingDp = padding)) }
    }

    // --- Sticker Overlay Management ---

    fun addSticker(preset: StickerPreset) {
        val currentCount = _uiState.value.stickers.size
        // Stagger positions slightly around center (0.5f, 0.5f)
        val jitterX = ((currentCount % 5) - 2) * 0.05f
        val jitterY = (((currentCount / 5) % 5) - 1) * 0.05f
        val posX = (0.5f + jitterX).coerceIn(0.2f, 0.8f)
        val posY = (0.5f + jitterY).coerceIn(0.2f, 0.8f)

        val newSticker = StickerCatalog.createStickerItem(preset, posX, posY)
        _uiState.update { state ->
            state.copy(
                stickers = state.stickers + newSticker,
                selectedStickerId = newSticker.id,
                selectedSlotId = null
            )
        }
    }

    fun selectSticker(id: String?) {
        _uiState.update { state ->
            state.copy(
                selectedStickerId = id,
                selectedSlotId = if (id != null) null else state.selectedSlotId
            )
        }
    }

    fun updateStickerPosition(id: String, x: Float, y: Float) {
        _uiState.update { state ->
            val updated = state.stickers.map { sticker ->
                if (sticker.id == id) {
                    sticker.copy(
                        x = x.coerceIn(0.05f, 0.95f),
                        y = y.coerceIn(0.05f, 0.95f)
                    )
                } else sticker
            }
            state.copy(stickers = updated)
        }
    }

    fun updateStickerTransform(id: String, panDeltaX: Float, panDeltaY: Float, zoomDelta: Float, rotationDelta: Float) {
        _uiState.update { state ->
            val updated = state.stickers.map { sticker ->
                if (sticker.id == id) {
                    val newX = (sticker.x + panDeltaX).coerceIn(0.05f, 0.95f)
                    val newY = (sticker.y + panDeltaY).coerceIn(0.05f, 0.95f)
                    val newScale = (sticker.scale * zoomDelta).coerceIn(0.3f, 4.0f)
                    val newRot = (sticker.rotation + rotationDelta) % 360f
                    sticker.copy(
                        x = newX,
                        y = newY,
                        scale = newScale,
                        rotation = if (newRot < 0f) newRot + 360f else newRot
                    )
                } else sticker
            }
            state.copy(stickers = updated)
        }
    }

    fun setStickerScale(id: String, scale: Float) {
        _uiState.update { state ->
            val updated = state.stickers.map { sticker ->
                if (sticker.id == id) {
                    sticker.copy(scale = scale.coerceIn(0.3f, 4.0f))
                } else sticker
            }
            state.copy(stickers = updated)
        }
    }

    fun zoomSticker(id: String, delta: Float) {
        _uiState.update { state ->
            val updated = state.stickers.map { sticker ->
                if (sticker.id == id) {
                    sticker.copy(scale = (sticker.scale + delta).coerceIn(0.3f, 4.0f))
                } else sticker
            }
            state.copy(stickers = updated)
        }
    }

    fun setStickerRotation(id: String, rotation: Float) {
        _uiState.update { state ->
            val normalized = ((rotation % 360f) + 360f) % 360f
            val updated = state.stickers.map { sticker ->
                if (sticker.id == id) {
                    sticker.copy(rotation = normalized)
                } else sticker
            }
            state.copy(stickers = updated)
        }
    }

    fun rotateSticker(id: String, deltaDegrees: Float) {
        _uiState.update { state ->
            val updated = state.stickers.map { sticker ->
                if (sticker.id == id) {
                    val newRot = ((sticker.rotation + deltaDegrees) % 360f + 360f) % 360f
                    sticker.copy(rotation = newRot)
                } else sticker
            }
            state.copy(stickers = updated)
        }
    }

    fun flipSticker(id: String) {
        _uiState.update { state ->
            val updated = state.stickers.map { sticker ->
                if (sticker.id == id) {
                    sticker.copy(isFlipped = !sticker.isFlipped)
                } else sticker
            }
            state.copy(stickers = updated)
        }
    }

    fun duplicateSticker(id: String) {
        _uiState.update { state ->
            val target = state.stickers.find { it.id == id } ?: return@update state
            val duplicate = target.copy(
                id = java.util.UUID.randomUUID().toString(),
                x = (target.x + 0.05f).coerceIn(0.05f, 0.95f),
                y = (target.y + 0.05f).coerceIn(0.05f, 0.95f)
            )
            state.copy(
                stickers = state.stickers + duplicate,
                selectedStickerId = duplicate.id,
                selectedSlotId = null
            )
        }
    }

    fun bringStickerToFront(id: String) {
        _uiState.update { state ->
            val target = state.stickers.find { it.id == id } ?: return@update state
            val remaining = state.stickers.filter { it.id != id }
            state.copy(stickers = remaining + target)
        }
    }

    fun sendStickerToBack(id: String) {
        _uiState.update { state ->
            val target = state.stickers.find { it.id == id } ?: return@update state
            val remaining = state.stickers.filter { it.id != id }
            state.copy(stickers = listOf(target) + remaining)
        }
    }

    fun removeSticker(id: String) {
        _uiState.update { state ->
            state.copy(
                stickers = state.stickers.filter { it.id != id },
                selectedStickerId = if (state.selectedStickerId == id) null else state.selectedStickerId
            )
        }
    }

    fun clearAllStickers() {
        _uiState.update { state ->
            state.copy(
                stickers = emptyList(),
                selectedStickerId = null
            )
        }
    }

    // --- Export & Share ---

    fun exportAndShare(
        format: ExportFormat = ExportFormat.JPEG,
        quality: Int = 95,
        caption: String = "Created with Photo Frame ✨ #photoframe #collage"
    ) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isExporting = true) }
                val state = _uiState.value
                val bitmap = CollageBitmapExporter.createCollageBitmap(
                    context = getApplication(),
                    template = state.currentTemplate,
                    slots = state.slots,
                    style = state.style,
                    aspectRatio = state.aspectRatio,
                    stickers = state.stickers
                )
                val file = CollageBitmapExporter.exportCollageToFile(
                    context = getApplication(),
                    bitmap = bitmap,
                    format = format,
                    quality = quality
                )
                val fileUri = CollageBitmapExporter.getUriForFile(getApplication(), file)
                _uiState.update { it.copy(isExporting = false) }
                _events.emit(UiEvent.ShareImageUri(fileUri, format.mimeType, caption))
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isExporting = false) }
                _events.emit(UiEvent.ShowToast("Failed to export: ${e.localizedMessage ?: "Unknown error"}"))
            }
        }
    }

    fun saveToDeviceGallery(
        format: ExportFormat = ExportFormat.JPEG,
        quality: Int = 95
    ) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isExporting = true) }
                val state = _uiState.value
                val bitmap = CollageBitmapExporter.createCollageBitmap(
                    context = getApplication(),
                    template = state.currentTemplate,
                    slots = state.slots,
                    style = state.style,
                    aspectRatio = state.aspectRatio,
                    stickers = state.stickers
                )
                val success = CollageBitmapExporter.saveBitmapToGallery(
                    context = getApplication(),
                    bitmap = bitmap,
                    format = format,
                    quality = quality
                )
                _uiState.update { it.copy(isExporting = false) }
                val message = if (success) "Saved to Pictures/PhotoFrame! 🖼️" else "Failed to save image"
                _events.emit(UiEvent.ShowToast(message))
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isExporting = false) }
                _events.emit(UiEvent.ShowToast("Failed to save: ${e.localizedMessage ?: "Unknown error"}"))
            }
        }
    }
}
