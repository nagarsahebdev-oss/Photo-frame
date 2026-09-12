package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.PhotoFilter
import com.example.ui.components.CollageCanvasView
import com.example.ui.components.CollageControlsView
import com.example.ui.components.ExportShareDialog
import com.example.ui.components.SlotEditBar
import com.example.ui.components.StickerEditBar
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.CollageViewModel
import com.example.ui.viewmodel.UiEvent
import com.example.util.CollageBitmapExporter

class MainActivity : ComponentActivity() {

    private val viewModel: CollageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainCollageScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCollageScreen(viewModel: CollageViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showExportSheet by remember { mutableStateOf(false) }

    // Multi-photo picker
    val multiPhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 8)
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.batchUpdateImages(uris)
        }
    }

    // Single photo picker for specific slot
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        val slotId = uiState.selectedSlotId
        if (uri != null && slotId != null) {
            viewModel.updateSlotImage(slotId, uri)
        }
    }

    // Observe ViewModel Events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is UiEvent.ShareImage -> {
                    CollageBitmapExporter.shareBitmap(context, event.bitmap)
                }
                is UiEvent.ShareImageUri -> {
                    CollageBitmapExporter.launchShareIntent(
                        context = context,
                        fileUri = event.uri,
                        mimeType = event.mimeType,
                        caption = event.caption
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Photo Frame",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(
                                text = "✨",
                                fontSize = 18.sp
                            )
                        }
                        Text(
                            text = "${uiState.photoCount} photos • ${uiState.selectedCategory.displayName} • ${uiState.aspectRatio.title}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // Pick multiple photos
                    FilledTonalButton(
                        onClick = {
                            multiPhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .testTag("button_pick_photos"),
                        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("Add")
                    }

                    // Save to Gallery
                    IconButton(
                        onClick = { viewModel.saveToDeviceGallery() },
                        modifier = Modifier.testTag("button_save_gallery")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save to device gallery",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Share to Social Media
                    Button(
                        onClick = { showExportSheet = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("button_share")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    // Deselect slot and sticker on background tap
                    viewModel.selectSlot(null)
                    viewModel.selectSticker(null)
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Canvas Container (Adaptive width, max 500dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .widthIn(max = 500.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CollageCanvasView(
                        template = uiState.currentTemplate,
                        slots = uiState.slots,
                        style = uiState.style,
                        aspectRatio = uiState.aspectRatio,
                        selectedSlotId = uiState.selectedSlotId,
                        onSelectSlot = { id -> viewModel.selectSlot(id) },
                        onTransformSlot = { slotId, panX, panY, zoom ->
                            viewModel.selectSlot(slotId)
                            if (zoom != 1f) {
                                viewModel.zoomSelectedSlot(zoom - 1f)
                            }
                            if (panX != 0f || panY != 0f) {
                                viewModel.panSelectedSlot(panX, panY)
                            }
                        },
                        stickers = uiState.stickers,
                        selectedStickerId = uiState.selectedStickerId,
                        onSelectSticker = { id -> viewModel.selectSticker(id) },
                        onMoveSticker = { id, x, y -> viewModel.updateStickerPosition(id, x, y) },
                        onTransformSticker = { id, panX, panY, zoom, rot ->
                            viewModel.updateStickerTransform(id, panX, panY, zoom, rot)
                        },
                        onScaleSticker = { id, scale -> viewModel.setStickerScale(id, scale) },
                        onRotateSticker = { id, rot -> viewModel.setStickerRotation(id, rot) },
                        onDeleteSticker = { id -> viewModel.removeSticker(id) },
                        onDuplicateSticker = { id -> viewModel.duplicateSticker(id) },
                        onFlipSticker = { id -> viewModel.flipSticker(id) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Slot Edit Toolbar (visible when a slot is tapped)
                val selectedSlot = uiState.slots.find { it.id == uiState.selectedSlotId }
                SlotEditBar(
                    slotId = uiState.selectedSlotId,
                    currentFilter = selectedSlot?.filter ?: PhotoFilter.NONE,
                    onFilterSelect = { filter -> viewModel.setSelectedSlotFilter(filter) },
                    onChangePhoto = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onRotate = { viewModel.rotateSelectedSlot() },
                    onFlip = { viewModel.flipSelectedSlot() },
                    onZoomIn = { viewModel.zoomSelectedSlot(0.15f) },
                    onZoomOut = { viewModel.zoomSelectedSlot(-0.15f) },
                    onReset = { viewModel.resetSelectedSlot() },
                    onClose = { viewModel.selectSlot(null) }
                )

                // Sticker Edit Toolbar (visible when a sticker is selected)
                val selectedSticker = uiState.stickers.find { it.id == uiState.selectedStickerId }
                StickerEditBar(
                    sticker = selectedSticker,
                    onZoomIn = { selectedSticker?.id?.let { viewModel.zoomSticker(it, 0.15f) } },
                    onZoomOut = { selectedSticker?.id?.let { viewModel.zoomSticker(it, -0.15f) } },
                    onScaleChange = { scale -> selectedSticker?.id?.let { viewModel.setStickerScale(it, scale) } },
                    onRotateLeft = { selectedSticker?.id?.let { viewModel.rotateSticker(it, -15f) } },
                    onRotateRight = { selectedSticker?.id?.let { viewModel.rotateSticker(it, 15f) } },
                    onRotate90 = { selectedSticker?.id?.let { viewModel.rotateSticker(it, 90f) } },
                    onRotationChange = { rot -> selectedSticker?.id?.let { viewModel.setStickerRotation(it, rot) } },
                    onFlip = { selectedSticker?.id?.let { viewModel.flipSticker(it) } },
                    onBringToFront = { selectedSticker?.id?.let { viewModel.bringStickerToFront(it) } },
                    onSendToBack = { selectedSticker?.id?.let { viewModel.sendStickerToBack(it) } },
                    onDuplicate = { selectedSticker?.id?.let { viewModel.duplicateSticker(it) } },
                    onDelete = { selectedSticker?.id?.let { viewModel.removeSticker(it) } },
                    onClose = { viewModel.selectSticker(null) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Controls: Photo Counts, Frame Categories, Background, Borders, Aspect Ratio, Filters, Stickers
                CollageControlsView(
                    photoCount = uiState.photoCount,
                    onPhotoCountChange = { count -> viewModel.setPhotoCount(count) },
                    selectedCategory = uiState.selectedCategory,
                    onCategoryChange = { category -> viewModel.setCategory(category) },
                    availableTemplates = uiState.availableTemplates,
                    currentTemplate = uiState.currentTemplate,
                    onTemplateSelect = { template -> viewModel.setTemplate(template) },
                    aspectRatio = uiState.aspectRatio,
                    onAspectRatioChange = { ratio -> viewModel.setAspectRatio(ratio) },
                    style = uiState.style,
                    slots = uiState.slots,
                    selectedSlotId = uiState.selectedSlotId,
                    onSelectSlot = { id -> viewModel.selectSlot(id) },
                    onSlotFilterChange = { id, filter -> viewModel.setSlotFilter(id, filter) },
                    onApplyFilterToAll = { filter -> viewModel.applyFilterToAllSlots(filter) },
                    stickers = uiState.stickers,
                    selectedStickerId = uiState.selectedStickerId,
                    onAddSticker = { preset -> viewModel.addSticker(preset) },
                    onSelectSticker = { id -> viewModel.selectSticker(id) },
                    onRemoveSticker = { id -> viewModel.removeSticker(id) },
                    onClearAllStickers = { viewModel.clearAllStickers() },
                    onBackgroundModeChange = { mode -> viewModel.setBackgroundMode(mode) },
                    onSolidColorChange = { color -> viewModel.setSolidBackgroundColor(color) },
                    onGradientChange = { colors -> viewModel.setGradientBackground(colors) },
                    onPatternChange = { pattern, baseColor, elemColor ->
                        viewModel.setPattern(pattern, baseColor, elemColor)
                    },
                    onPatternScaleChange = { scale -> viewModel.setPatternScale(scale) },
                    onSlotBorderWidthChange = { width -> viewModel.setSlotBorderWidth(width) },
                    onSlotBorderColorChange = { color -> viewModel.setSlotBorderColor(color) },
                    onFrameBorderWidthChange = { width -> viewModel.setFrameBorderWidth(width) },
                    onFrameBorderColorChange = { color -> viewModel.setFrameBorderColor(color) },
                    onCornerRadiusChange = { radius -> viewModel.setCornerRadius(radius) },
                    onOuterPaddingChange = { padding -> viewModel.setOuterPadding(padding) },
                    modifier = Modifier.widthIn(max = 600.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Exporting progress overlay
            if (uiState.isExporting) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Creating high-res frame...",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }

    if (showExportSheet) {
        ExportShareDialog(
            aspectRatio = uiState.aspectRatio,
            isExporting = uiState.isExporting,
            onDismiss = { showExportSheet = false },
            onShare = { format, quality, caption ->
                viewModel.exportAndShare(format, quality, caption)
                showExportSheet = false
            },
            onSaveToGallery = { format, quality ->
                viewModel.saveToDeviceGallery(format, quality)
                showExportSheet = false
            }
        )
    }
}
