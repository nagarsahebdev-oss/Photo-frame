package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import com.example.model.FrameCategory
import com.example.model.FrameTemplates
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.CollageViewModel
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun testTemplatesAvailableFor2To8Photos() {
    for (count in 2..8) {
      for (category in FrameCategory.values()) {
        val templates = FrameTemplates.getTemplates(count, category)
        assertTrue("Expected templates for $count photos and $category", templates.isNotEmpty())
      }
    }
  }

  @Test
  fun collage_screenshot() {
    val context = ApplicationProvider.getApplicationContext<android.app.Application>()
    val viewModel = CollageViewModel(context)
    composeTestRule.setContent {
      MyApplicationTheme {
        MainCollageScreen(viewModel = viewModel)
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
