package com.note.styles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.note.styles.data.local.AppDatabase
import com.note.styles.data.local.UserPreferences
import com.note.styles.data.model.NoteEntity
import com.note.styles.data.model.NoteStyle
import com.note.styles.data.repository.NoteRepository
import com.note.styles.ui.screens.NoteDetailScreen
import com.note.styles.ui.screens.NoteListScreen
import com.note.styles.ui.screens.OnboardingScreen
import com.note.styles.ui.screens.ThemeVaultScreen
import com.note.styles.ui.theme.NoteSTheme
import com.note.styles.ui.viewmodel.NoteViewModel
import com.note.styles.ui.viewmodel.NoteViewModelFactory

sealed interface AppScreen {
    data object List : AppScreen
    data class Detail(val note: NoteEntity) : AppScreen
    data object Vault : AppScreen
}

class MainActivity : ComponentActivity() {

    private val viewModel: NoteViewModel by viewModels {
        val scope = (application as? android.app.Application)?.let {
            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.SupervisorJob() + kotlinx.coroutines.Dispatchers.Main)
        } ?: kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main)
        val database = AppDatabase.getDatabase(applicationContext, scope)
        val repository = NoteRepository(database.noteDao(), database.themeDao())
        val userPreferences = UserPreferences(applicationContext)
        NoteViewModelFactory(repository, userPreferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteSTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NoteSApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun NoteSApp(viewModel: NoteViewModel) {
    val isSetupCompleted by viewModel.isSetupCompleted.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()

    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.List) }
    val notes by viewModel.allNotes.collectAsStateWithLifecycle()
    val savedThemes by viewModel.savedThemes.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = isSetupCompleted,
        transitionSpec = {
            (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                slideOutHorizontally { width -> -width / 3 } + fadeOut()
            )
        },
        label = "onboarding_main_switch"
    ) { setupDone ->
        if (!setupDone) {
            OnboardingScreen(
                initialLanguage = selectedLanguage,
                onCompleteSetup = { chosenLanguage ->
                    viewModel.completeSetup(chosenLanguage)
                }
            )
        } else {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    if (targetState is AppScreen.Detail || targetState is AppScreen.Vault) {
                        (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> -width / 3 } + fadeOut()
                        )
                    } else {
                        (slideInHorizontally { width -> -width / 3 } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> width } + fadeOut()
                        )
                    }
                },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    is AppScreen.List -> {
                        NoteListScreen(
                            notes = notes,
                            selectedLanguage = selectedLanguage,
                            onLanguageChange = { newLang ->
                                viewModel.setLanguage(newLang)
                            },
                            onOpenSetupTour = {
                                viewModel.resetSetup()
                            },
                            onNoteClick = { note ->
                                currentScreen = AppScreen.Detail(note)
                            },
                            onCreateNote = {
                                val defaultNote = viewModel.createEmptyNoteWithStyle(NoteStyle.DEFAULT)
                                currentScreen = AppScreen.Detail(defaultNote)
                            },
                            onQuickRouletteCreate = {
                                val rouletteNote = viewModel.createRouletteNote()
                                currentScreen = AppScreen.Detail(rouletteNote)
                            },
                            onOpenThemeVault = {
                                currentScreen = AppScreen.Vault
                            },
                            onToggleFavorite = { note ->
                                viewModel.toggleFavorite(note)
                            },
                            onTogglePin = { note ->
                                viewModel.togglePin(note)
                            }
                        )
                    }

                    is AppScreen.Detail -> {
                        BackHandler {
                            currentScreen = AppScreen.List
                        }
                        NoteDetailScreen(
                            initialNote = screen.note,
                            onSaveNote = { noteToSave ->
                                if (noteToSave.title.isNotBlank() || noteToSave.content.isNotBlank() || noteToSave.checklistRaw.isNotBlank()) {
                                    viewModel.saveNote(noteToSave)
                                }
                            },
                            onDeleteNote = { noteToDelete ->
                                if (noteToDelete.id != 0L) {
                                    viewModel.deleteNote(noteToDelete)
                                }
                            },
                            onSaveFavoriteTheme = { themeToSave ->
                                viewModel.saveFavoriteTheme(themeToSave)
                            },
                            onBack = {
                                currentScreen = AppScreen.List
                            }
                        )
                    }

                    is AppScreen.Vault -> {
                        BackHandler {
                            currentScreen = AppScreen.List
                        }
                        ThemeVaultScreen(
                            savedThemes = savedThemes,
                            onBack = {
                                currentScreen = AppScreen.List
                            },
                            onCreateNoteWithTheme = { selectedStyle ->
                                val note = viewModel.createEmptyNoteWithStyle(selectedStyle)
                                currentScreen = AppScreen.Detail(note)
                            },
                            onSaveFavoriteTheme = { style ->
                                viewModel.saveFavoriteTheme(style)
                            },
                            onDeleteSavedTheme = { themeId ->
                                viewModel.deleteSavedTheme(themeId)
                            }
                        )
                    }
                }
            }
        }
    }
}
