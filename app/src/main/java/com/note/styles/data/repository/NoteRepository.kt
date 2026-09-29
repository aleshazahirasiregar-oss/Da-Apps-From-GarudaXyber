package com.note.styles.data.repository

import com.note.styles.data.local.NoteDao
import com.note.styles.data.local.ThemeDao
import com.note.styles.data.model.NoteEntity
import com.note.styles.data.model.SavedThemeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class NoteRepository(
    private val noteDao: NoteDao,
    private val themeDao: ThemeDao
) {
    val allNotes: Flow<List<NoteEntity>> = noteDao.getAllNotes().flowOn(Dispatchers.IO)
    val savedThemes: Flow<List<SavedThemeEntity>> = themeDao.getAllSavedThemes().flowOn(Dispatchers.IO)

    fun getNoteById(id: Long): Flow<NoteEntity?> {
        return noteDao.getNoteById(id).flowOn(Dispatchers.IO)
    }

    suspend fun getNoteByIdDirect(id: Long): NoteEntity? = withContext(Dispatchers.IO) {
        noteDao.getNoteByIdDirect(id)
    }

    suspend fun insertNote(note: NoteEntity): Long = withContext(Dispatchers.IO) {
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: NoteEntity) = withContext(Dispatchers.IO) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: NoteEntity) = withContext(Dispatchers.IO) {
        noteDao.deleteNote(note)
    }

    suspend fun deleteNoteById(id: Long) = withContext(Dispatchers.IO) {
        noteDao.deleteNoteById(id)
    }

    suspend fun setPinned(id: Long, isPinned: Boolean) = withContext(Dispatchers.IO) {
        noteDao.setPinned(id, isPinned)
    }

    suspend fun setFavorite(id: Long, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        noteDao.setFavorite(id, isFavorite)
    }

    suspend fun insertSavedTheme(theme: SavedThemeEntity): Long = withContext(Dispatchers.IO) {
        themeDao.insertTheme(theme)
    }

    suspend fun deleteSavedTheme(id: Long) = withContext(Dispatchers.IO) {
        themeDao.deleteThemeById(id)
    }

    suspend fun removeOldDemoNotes() = withContext(Dispatchers.IO) {
        noteDao.removeOldDemoNotes()
    }
}
