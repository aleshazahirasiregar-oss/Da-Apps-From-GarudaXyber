package com.note.styles.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.note.styles.data.model.SavedThemeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemeDao {
    @Query("SELECT * FROM saved_themes ORDER BY createdTimestamp DESC")
    fun getAllSavedThemes(): Flow<List<SavedThemeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(theme: SavedThemeEntity): Long

    @Delete
    suspend fun deleteTheme(theme: SavedThemeEntity)

    @Query("DELETE FROM saved_themes WHERE id = :id")
    suspend fun deleteThemeById(id: Long)
}
