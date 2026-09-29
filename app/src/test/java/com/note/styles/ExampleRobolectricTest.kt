package com.note.styles

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.note.styles.data.model.ChecklistItem
import com.note.styles.data.model.NoteEntity
import com.note.styles.theme.ThemeEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context matches app name NoteS`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("NoteS", appName)
  }

  @Test
  fun `theme engine provides 2 million themes`() {
    assertEquals(2_000_000L, ThemeEngine.MAX_THEME_COUNT)
    val theme1 = ThemeEngine.getThemeById(1L)
    val theme777 = ThemeEngine.getThemeById(777L)
    val theme2M = ThemeEngine.getThemeById(2_000_000L)
    assertNotNull(theme1)
    assertNotNull(theme777)
    assertNotNull(theme2M)
  }

  @Test
  fun `checklist parser and formatter works accurately`() {
    val items = listOf(
      ChecklistItem(0, "Buy Milk", true),
      ChecklistItem(1, "Pick up flowers", false)
    )
    val formatted = NoteEntity.formatChecklist(items)
    val note = NoteEntity(title = "Test", content = "Test", checklistRaw = formatted)
    val parsed = note.getChecklistItems()
    assertEquals(2, parsed.size)
    assertTrue(parsed[0].isCompleted)
    assertEquals("Buy Milk", parsed[0].text)
    assertEquals(false, parsed[1].isCompleted)
    assertEquals("Pick up flowers", parsed[1].text)
  }

  @Test
  fun `user preferences default to setup not completed and language is id`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = com.note.styles.data.local.UserPreferences(context)
    prefs.resetSetup()
    assertEquals(false, prefs.isSetupCompleted.value)
    assertEquals("id", prefs.selectedLanguage.value)

    prefs.setLanguage("en")
    assertEquals("en", prefs.selectedLanguage.value)

    prefs.setSetupCompleted(true)
    assertEquals(true, prefs.isSetupCompleted.value)
  }

  @Test
  fun `app languages list has required options`() {
    val languages = com.note.styles.util.AppLanguages.list
    assertEquals(4, languages.size)
    assertTrue(languages.any { it.code == "id" })
    assertTrue(languages.any { it.code == "en" })
    assertTrue(languages.any { it.code == "es" })
    assertTrue(languages.any { it.code == "ja" })
  }

  @Test
  fun `app version is 2_0_0 and version code is 2`() {
    assertEquals("2.0.0", BuildConfig.VERSION_NAME)
    assertEquals(2, BuildConfig.VERSION_CODE)
  }
}
