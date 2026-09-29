package com.note.styles.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.note.styles.data.local.UserPreferences
import com.note.styles.data.model.CardShapes
import com.note.styles.data.model.FontFamilies
import com.note.styles.data.model.NoteEntity
import com.note.styles.data.model.NoteStyle
import com.note.styles.data.model.PatternTypes
import com.note.styles.data.model.PinTypes
import com.note.styles.data.model.SavedThemeEntity
import com.note.styles.data.model.Stamps
import com.note.styles.data.repository.NoteRepository
import com.note.styles.theme.ThemeEngine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(
    private val repository: NoteRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val isSetupCompleted: StateFlow<Boolean> = userPreferences.isSetupCompleted
    val selectedLanguage: StateFlow<String> = userPreferences.selectedLanguage

    val allNotes: StateFlow<List<NoteEntity>> = repository.allNotes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val savedThemes: StateFlow<List<SavedThemeEntity>> = repository.savedThemes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Automatically remove old demo notes so only the official guide/user notes remain
        viewModelScope.launch {
            repository.removeOldDemoNotes()
        }
    }

    fun completeSetup(languageCode: String) {
        viewModelScope.launch {
            userPreferences.setLanguage(languageCode)
            userPreferences.setSetupCompleted(true)
            repository.removeOldDemoNotes()
        }
    }

    fun setLanguage(languageCode: String) {
        userPreferences.setLanguage(languageCode)
    }

    fun resetSetup() {
        userPreferences.resetSetup()
    }

    fun saveNote(note: NoteEntity, onSaved: ((Long) -> Unit)? = null) {
        viewModelScope.launch {
            if (note.id == 0L) {
                val newId = repository.insertNote(note)
                onSaved?.invoke(newId)
            } else {
                repository.updateNote(note)
                onSaved?.invoke(note.id)
            }
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun togglePin(note: NoteEntity) {
        viewModelScope.launch {
            repository.setPinned(note.id, !note.isPinned)
        }
    }

    fun toggleFavorite(note: NoteEntity) {
        viewModelScope.launch {
            repository.setFavorite(note.id, !note.isFavorite)
        }
    }

    fun saveFavoriteTheme(style: NoteStyle) {
        viewModelScope.launch {
            val entity = SavedThemeEntity.fromNoteStyle(style)
            repository.insertSavedTheme(entity)
        }
    }

    fun deleteSavedTheme(id: Long) {
        viewModelScope.launch {
            repository.deleteSavedTheme(id)
        }
    }

    fun createEmptyNoteWithStyle(style: NoteStyle): NoteEntity {
        return NoteEntity(
            title = "",
            content = "",
            category = "General"
        ).applyStyle(style)
    }

    fun createRouletteNote(): NoteEntity {
        val randomStyle = ThemeEngine.getRandomTheme()
        return createEmptyNoteWithStyle(randomStyle)
    }
}

class NoteViewModelFactory(
    private val repository: NoteRepository,
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repository, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
