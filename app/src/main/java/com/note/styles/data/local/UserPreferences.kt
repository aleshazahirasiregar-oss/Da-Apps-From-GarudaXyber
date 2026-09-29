package com.note.styles.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("notes_user_prefs", Context.MODE_PRIVATE)

    private val _isSetupCompleted = MutableStateFlow(
        prefs.getBoolean(KEY_SETUP_COMPLETED, false)
    )
    val isSetupCompleted: StateFlow<Boolean> = _isSetupCompleted.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(
        prefs.getString(KEY_LANGUAGE, "id") ?: "id"
    )
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    fun setSetupCompleted(completed: Boolean) {
        prefs.edit().putBoolean(KEY_SETUP_COMPLETED, completed).apply()
        _isSetupCompleted.value = completed
    }

    fun setLanguage(languageCode: String) {
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
        _selectedLanguage.value = languageCode
    }

    fun resetSetup() {
        prefs.edit().putBoolean(KEY_SETUP_COMPLETED, false).apply()
        _isSetupCompleted.value = false
    }

    companion object {
        private const val KEY_SETUP_COMPLETED = "key_setup_completed"
        private const val KEY_LANGUAGE = "key_language"
    }
}
