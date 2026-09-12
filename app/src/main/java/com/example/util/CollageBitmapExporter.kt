package com.example.util

import android.app.Activity
import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.model.BackgroundMode
import com.example.model.CollageStyle
import com.example.model.FrameTemplate
import com.example.model.PhotoSlot
import com.example.model.SlotShapeSpec
import com.example.model.SocialAspectRatio
import com.example.model.StickerItem
import com.example.model.StickerType
import com.example.ui.shapes.PatternRenderer
import com.example.ui.shapes.buildAndroidHeartPath
import com.example.ui.shapes.buildAndroidPolygonPath
import com.example.ui.shapes.buildAndroidTrianglePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max

enum class ExportFormat(val extension: String, val mimeType: String, val displayName: String) {
    JPEG("jpg", "image/jpeg", "JPEG (Social Media)"),
    PNG("png", "image/png", "PNG (Lossless High-Res)")
}

object CollageBitmapExporter {

    suspend fun createCollageBitmap(
        context: Context,
        template: FrameTemplate,
        slots: List<PhotoSlot>,
        style: CollageStyle,
        aspectRatio: SocialAspectRatio,
        stickers: List<StickerItem> = emptyList()
    ): Bitmap = withContext(Dispatchers.IO) {
        val width = aspectRatio.widthPx
        val height = aspectRatio.heightPx
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val density = width / 360f

        // 1. Draw Background: Solid, Gradient, or Pattern
        when (style.backgroundMode) {
            BackgroundMode.SOLID -> {
                val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = style.backgroundColor.toInt()
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
            }
            BackgroundMode.GRADIENT -> {
                val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                val gradientColors = style.backgroundGradient
                if (gradientColors != null && gradientColors.size >= 2) {
                    val colors = gradientColors.map { it.toInt() }.toIntArray()
                    val shader = LinearGradient(
                        0f, 0f, width.toFloat(), height.toFloat(),
                        colors, null, Shader.TileMode.CLAMP
                    )
                    bgPaint.shader = shader
                } else {
                    bgPaint.color = style.backgroundColor.toInt()
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
            }
            BackgroundMode.PATTERN -> {
                PatternRenderer.drawAndroidPattern(
                    canvas = canvas,
                    width = width.toFloat(),
                    height = height.toFloat(),
                    style = style,
                    density = density
                )
            }
        }

        // 2. Compute inner bounds for slots
        val outerPadPx = style.outerPaddingDp * density
        val innerW = (width - 2 * outerPadPx).coerceAtLeast(10f)
        val innerH = (height - 2 * outerPadPx).coerceAtLeast(10f)
        val slotBorderWidthPx = style.slotBorderWidthDp * density
        val cornerRadiusPx = style.cornerRadiusDp * density

        val slotBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = style.slotBorderColor.toInt()
            this.style = Paint.Style.STROKE
            strokeWidth = slotBorderWidthPx
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        val placeholderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#E0E0E0")
        }

        // 3. Draw Each Photo Slot
        for (spec in template.slots) {
            val slotData = slots.find { it.id == spec.id }

            val left = outerPadPx + spec.left * innerW
            val top = outerPadPx + spec.top * innerH
            val right = outerPadPx + spec.right * innerW
            val bottom = outerPadPx + spec.bottom * innerH
            val slotW = (right - left).coerceAtLeast(1f)
            val slotH = (bottom - top).coerceAtLeast(1f)

            // Shape path
            val path = Path()
            when (val shapeSpec = spec.shape) {
                is SlotShapeSpec.Rectangle -> {
                    path.addRoundRect(
                        RectF(0f, 0f, slotW, slotH),
                        cornerRadiusPx, cornerRadiusPx,
                        Path.Direction.CW
                    )
                }
                is SlotShapeSpec.Circle -> {
                    path.addOval(RectF(0f, 0f, slotW, slotH), Path.Direction.CW)
                }
                is SlotShapeSpec.Heart -> {
                    path.set(buildAndroidHeartPath(slotW, slotH))
                }
                is SlotShapeSpec.Triangle -> {
                    path.set(buildAndroidTrianglePath(shapeSpec.type, slotW, slotH))
                }
                is SlotShapeSpec.Polygon -> {
                    path.set(buildAndroidPolygonPath(shapeSpec.relativePoints, slotW, slotH))
                }
            }

            // Draw Photo
            canvas.save()
            canvas.translate(left, top)
            canvas.clipPath(path)

            val photoBitmap = loadSlotBitmap(context, slotData, slotW.toInt(), slotH.toInt())
            if (photoBitmap != null) {
                val photoPaint = Paint(fillPaint).apply {
                    colorFilter = slotData?.filter?.toAndroidColorFilter()
                }
                drawTransformedBitmap(
                    canvas = canvas,
                    bitmap = photoBitmap,
                    slotW = slotW,
                    slotH = slotH,
                    scale = slotData?.scale ?: 1f,
                    offsetX = (slotData?.offsetX ?: 0f) * density,
                    offsetY = (slotData?.offsetY ?: 0f) * density,
                    rotation = slotData?.rotation ?: 0f,
                    isFlipped = slotData?.isFlipped ?: false,
                    paint = photoPaint
                )
            } else {
                canvas.drawRect(0f, 0f, slotW, slotH, placeholderPaint)
            }

            canvas.restore()

            // Draw Slot Border
            if (slotBorderWidthPx > 0f) {
                canvas.save()
                canvas.translate(left, top)
                canvas.drawPath(path, slotBorderPaint)
                canvas.restore()
            }
        }

        // 4. Draw Main Frame Border
        val frameBorderWidthPx = style.frameBorderWidthDp * density
        if (frameBorderWidthPx > 0f) {
            val frameBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = style.frameBorderColor.toInt()
                this.style = Paint.Style.STROKE
                strokeWidth = frameBorderWidthPx
            }
            val halfBorder = frameBorderWidthPx / 2f
            val frameRadius = (style.cornerRadiusDp * density).coerceAtLeast(0f)
            canvas.drawRoundRect(
                RectF(halfBorder, halfBorder, width - halfBorder, height - halfBorder),
                frameRadius, frameRadius,
                frameBorderPaint
            )
        }

        // 5. Draw Sticker Overlays
        if (stickers.isNotEmpty()) {
            drawStickers(canvas, stickers, width, height, density)
        }

        bitmap
    }

    private fun drawStickers(
        canvas: Canvas,
        stickers: List<StickerItem>,
        width: Int,
        height: Int,
        density: Float
    ) {
        val emojiPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
        }
        val badgeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }
        val badgeBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = Color.WHITE
        }
        val badgeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }

        for (sticker in stickers) {
            val cx = sticker.x * width
            val cy = sticker.y * height
            val baseSizePx = sticker.baseSizeDp * density * sticker.scale

            canvas.save()
            canvas.translate(cx, cy)
            canvas.rotate(sticker.rotation)
            if (sticker.isFlipped) {
                canvas.scale(-1f, 1f)
            }

            when (sticker.type) {
                StickerType.EMOJI, StickerType.VECTOR -> {
                    emojiPaint.textSize = baseSizePx
                    val fontMetrics = emojiPaint.fontMetrics
                    val yOffset = -(fontMetrics.ascent + fontMetrics.descent) / 2f
                    canvas.drawText(sticker.content, 0f, yOffset, emojiPaint)
                }
                StickerType.BADGE -> {
                    val text = sticker.content
                    val textSz = baseSizePx * 0.42f
                    badgeTextPaint.textSize = textSz
                    badgeTextPaint.color = sticker.badgeTextColor.toInt()
                    val textWidth = badgeTextPaint.measureText(text)
                    val padH = 14f * density * sticker.scale
                    val padV = 8f * density * sticker.scale
                    val badgeW = textWidth + padH * 2f
                    val badgeH = textSz + padV * 2f
                    val badgeRadius = badgeH / 2f

                    badgeBgPaint.color = sticker.badgeBgColor.toInt()
                    val rect = RectF(-badgeW / 2f, -badgeH / 2f, badgeW / 2f, badgeH / 2f)

                    // Draw subtle drop shadow for realistic sticker look
                    val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = Color.argb(70, 0, 0, 0)
                        style = Paint.Style.FILL
                    }
                    val shadowRect = RectF(
                        rect.left + 2f * density,
                        rect.top + 3f * density,
                        rect.right + 2f * density,
                        rect.bottom + 3f * density
                    )
                    canvas.drawRoundRect(shadowRect, badgeRadius, badgeRadius, shadowPaint)

                    // Draw badge background
                    canvas.drawRoundRect(rect, badgeRadius, badgeRadius, badgeBgPaint)

                    // Draw crisp white sticker contour border
                    badgeBorderPaint.strokeWidth = 2.5f * density * sticker.scale
                    canvas.drawRoundRect(rect, badgeRadius, badgeRadius, badgeBorderPaint)

                    // Draw badge text
                    val fontMetrics = badgeTextPaint.fontMetrics
                    val textYOffset = -(fontMetrics.ascent + fontMetrics.descent) / 2f
                    canvas.drawText(text, 0f, textYOffset, badgeTextPaint)
                }
            }

            canvas.restore()
        }
    }

    private fun loadSlotBitmap(
        context: Context,
        slot: PhotoSlot?,
        reqW: Int,
        reqH: Int
    ): Bitmap? {
        if (slot == null) return null
        return try {
            if (slot.uri != null) {
                context.contentResolver.openInputStream(slot.uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
            } else if (slot.defaultDrawableRes != null) {
                BitmapFactory.decodeResource(context.resources, slot.defaultDrawableRes)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun drawTransformedBitmap(
        canvas: Canvas,
        bitmap: Bitmap,
        slotW: Float,
        slotH: Float,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        rotation: Float,
        isFlipped: Boolean,
        paint: Paint
    ) {
        val bw = bitmap.width.toFloat()
        val bh = bitmap.height.toFloat()

        // Center crop base scale
        val scaleFactor = max(slotW / bw, slotH / bh) * scale
        val cx = slotW / 2f + offsetX
        val cy = slotH / 2f + offsetY

        val matrix = Matrix()
        // Center image
        matrix.postTranslate(-bw / 2f, -bh / 2f)
        if (isFlipped) {
            matrix.postScale(-1f, 1f)
        }
        matrix.postRotate(rotation)
        matrix.postScale(scaleFactor, scaleFactor)
        matrix.postTranslate(cx, cy)

        canvas.drawBitmap(bitmap, matrix, paint)
    }

    /**
     * Exports the final collage bitmap as an image file (JPEG or PNG) into the app's cache directory.
     * Guaranteed to execute on Dispatchers.IO for fast, smooth performance.
     */
    suspend fun exportCollageToFile(
        context: Context,
        bitmap: Bitmap,
        format: ExportFormat = ExportFormat.JPEG,
        quality: Int = 95,
        filenamePrefix: String = "photo_collage"
    ): File = withContext(Dispatchers.IO) {
        val cachePath = File(context.cacheDir, "images")
        if (!cachePath.exists()) {
            cachePath.mkdirs()
        }
        val file = File(cachePath, "${filenamePrefix}_${System.currentTimeMillis()}.${format.extension}")
        FileOutputStream(file).use { out ->
            val compressFormat = when (format) {
                ExportFormat.JPEG -> Bitmap.CompressFormat.JPEG
                ExportFormat.PNG -> Bitmap.CompressFormat.PNG
            }
            bitmap.compress(compressFormat, quality.coerceIn(10, 100), out)
        }
        file
    }

    /**
     * Resolves a content URI for an exported image file using Android FileProvider.
     */
    fun getUriForFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    /**
     * Launches the system share intent chooser for social media sharing.
     * Attaches the file URI, ClipData, and FLAG_GRANT_READ_URI_PERMISSION so all apps
     * (Instagram, WhatsApp, Facebook, Twitter/X, Pinterest, Snapchat, etc.) can read the file.
     */
    fun launchShareIntent(
        context: Context,
        fileUri: Uri,
        mimeType: String = "image/jpeg",
        caption: String = "Created with Photo Frame ✨ #photoframe #collage"
    ) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = mimeType
                putExtra(Intent.EXTRA_STREAM, fileUri)
                putExtra(Intent.EXTRA_TEXT, caption)
                clipData = ClipData.newUri(context.contentResolver, "Photo Collage", fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooserIntent = Intent.createChooser(shareIntent, "Share Collage to Social Media").apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                if (context !is Activity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Convenience method: Exports the collage as an image file and immediately launches the system share intent.
     */
    suspend fun exportAndShareCollage(
        context: Context,
        bitmap: Bitmap,
        format: ExportFormat = ExportFormat.JPEG,
        quality: Int = 95,
        caption: String = "Created with Photo Frame ✨ #photoframe #collage"
    ): Uri = withContext(Dispatchers.IO) {
        val file = exportCollageToFile(context, bitmap, format, quality)
        val uri = getUriForFile(context, file)
        withContext(Dispatchers.Main) {
            launchShareIntent(context, uri, format.mimeType, caption)
        }
        uri
    }

    fun shareBitmap(context: Context, bitmap: Bitmap) {
        try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()
            val file = File(cachePath, "photo_frame_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
            }

            val fileUri: Uri = getUriForFile(context, file)
            launchShareIntent(context, fileUri, "image/jpeg", "Created with Photo Frame ✨ #photoframe #collage")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun saveBitmapToGallery(
        context: Context,
        bitmap: Bitmap,
        format: ExportFormat = ExportFormat.JPEG,
        quality: Int = 95
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val filename = "PhotoFrame_${System.currentTimeMillis()}.${format.extension}"
            val resolver = context.contentResolver

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, format.mimeType)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/PhotoFrame")
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }

            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                resolver.openOutputStream(imageUri)?.use { out ->
                    val compressFormat = when (format) {
                        ExportFormat.JPEG -> Bitmap.CompressFormat.JPEG
                        ExportFormat.PNG -> Bitmap.CompressFormat.PNG
                    }
                    bitmap.compress(compressFormat, quality.coerceIn(10, 100), out)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(imageUri, contentValues, null, null)
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
