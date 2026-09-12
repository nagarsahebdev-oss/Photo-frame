package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Photo Frame", appName)
  }

  @Test
  fun `test exportCollageToFile creates file with correct extension`() = kotlinx.coroutines.runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val bitmap = android.graphics.Bitmap.createBitmap(100, 100, android.graphics.Bitmap.Config.ARGB_8888)
    val file = com.example.util.CollageBitmapExporter.exportCollageToFile(
      context = context,
      bitmap = bitmap,
      format = com.example.util.ExportFormat.JPEG,
      quality = 90
    )
    org.junit.Assert.assertTrue("Exported file must exist", file.exists())
    org.junit.Assert.assertTrue("Exported file must end with .jpg", file.name.endsWith(".jpg"))
    org.junit.Assert.assertTrue("Exported file must have content", file.length() > 0)
  }
}
