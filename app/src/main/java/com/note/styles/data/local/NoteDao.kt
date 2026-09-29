package com.note.styles.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.note.styles.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY isPinned DESC, updatedTimestamp DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    fun getNoteById(id: Long): Flow<NoteEntity?>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteByIdDirect(id: Long): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNoteById(id: Long)

    @Query("UPDATE notes SET isPinned = :isPinned, updatedTimestamp = :timestamp WHERE id = :id")
    suspend fun setPinned(id: Long, isPinned: Boolean, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE notes SET isFavorite = :isFavorite, updatedTimestamp = :timestamp WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean, timestamp: Long = System.currentTimeMillis())
    @Query("DELETE FROM notes WHERE title IN ('Weekend Coffee & Book List ☕', 'Secret Herb Focaccia 🍞', 'Quick Project Ideas 💡')")
    suspend fun removeOldDemoNotes()
}
