package com.example

import com.example.model.PhotoFilter
import com.example.model.PhotoSlot
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testPhotoFilterMatrixDimensions() {
    PhotoFilter.values().forEach { filter ->
      assertEquals("Filter ${filter.name} must have 20 matrix values", 20, filter.matrixValues.size)
      if (filter == PhotoFilter.NONE) {
        assertNull(filter.toComposeColorFilter())
        assertNull(filter.toAndroidColorFilter())
      } else {
        assertNotNull("Filter ${filter.name} must produce Compose ColorFilter", filter.toComposeColorFilter())
        assertNotNull("Filter ${filter.name} must produce Android ColorMatrixColorFilter", filter.toAndroidColorFilter())
      }
    }
  }

  @Test
  fun testCommonEffectsPresent() {
    val filterNames = PhotoFilter.values().map { it.name }
    assertTrue(filterNames.contains("BLACK_AND_WHITE"))
    assertTrue(filterNames.contains("SEPIA"))
    assertTrue(filterNames.contains("VINTAGE"))
  }

  @Test
  fun testPhotoSlotDefaultFilter() {
    val slot = PhotoSlot(id = 0)
    assertEquals(PhotoFilter.NONE, slot.filter)
    val vintageSlot = slot.copy(filter = PhotoFilter.VINTAGE)
    assertEquals(PhotoFilter.VINTAGE, vintageSlot.filter)
  }

  @Test
  fun testStickerCatalogAndPresets() {
    val categories = com.example.model.StickerCatalog.categories
    assertTrue("Sticker categories must not be empty", categories.isNotEmpty())
    assertTrue("Must have Emojis category", categories.contains("Emojis"))
    assertTrue("Must have Badges category", categories.contains("Badges"))

    val emojis = com.example.model.StickerCatalog.getPresets("Emojis")
    assertTrue("Emojis category must contain items", emojis.isNotEmpty())

    val preset = emojis.first()
    val stickerItem = com.example.model.StickerCatalog.createStickerItem(preset, 0.5f, 0.5f)
    assertEquals(0.5f, stickerItem.x, 0.001f)
    assertEquals(0.5f, stickerItem.y, 0.001f)
    assertEquals(1.0f, stickerItem.scale, 0.001f)
    assertEquals(0f, stickerItem.rotation, 0.001f)
    assertFalse(stickerItem.isFlipped)
  }

  @Test
  fun testStickerTransformBounds() {
    val item = com.example.model.StickerItem(
      id = "test-1",
      content = "✨",
      type = com.example.model.StickerType.EMOJI,
      x = 0.5f,
      y = 0.5f,
      scale = 1.0f,
      rotation = 0f
    )
    val scaled = item.copy(scale = 2.5f)
    assertEquals(2.5f, scaled.scale, 0.001f)

    val rotated = item.copy(rotation = 45f)
    assertEquals(45f, rotated.rotation, 0.001f)

    val flipped = item.copy(isFlipped = true)
    assertTrue(flipped.isFlipped)
  }

  @Test
  fun testExportFormatMimeTypes() {
    val jpeg = com.example.util.ExportFormat.JPEG
    assertEquals("jpg", jpeg.extension)
    assertEquals("image/jpeg", jpeg.mimeType)

    val png = com.example.util.ExportFormat.PNG
    assertEquals("png", png.extension)
    assertEquals("image/png", png.mimeType)
  }
}

