package com.note.styles.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.note.styles.data.model.CardShapes
import com.note.styles.data.model.FontFamilies
import com.note.styles.data.model.NoteEntity
import com.note.styles.data.model.PatternTypes
import com.note.styles.data.model.PinTypes
import com.note.styles.data.model.SavedThemeEntity
import com.note.styles.data.model.Stamps
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [NoteEntity::class, SavedThemeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun themeDao(): ThemeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notes_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.noteDao())
                    }
                }
            }

            private suspend fun populateInitialData(noteDao: NoteDao) {
                val now = System.currentTimeMillis()
                
                // Note: Official App Guide & Features Overview ("Apa Aja Dalam App Ini")
                noteDao.insertNote(
                    NoteEntity(
                        title = "✨ Panduan & Fitur Aplikasi NoteS",
                        content = "Selamat datang di NoteS! Aplikasi catatan aesthetic dengan lebih dari 2.000.000 kombinasi tema visual unik.\n\nBerikut fitur-fitur lengkap yang tersedia di aplikasi ini:\n\n1. 🎨 2 Juta Tema Visual Prosedural\nSetiap nomor tema (1 s/d 2.000.000) menghasilkan gradien warna, palet kertas, dan gaya tipografi yang unik secara matematis.\n\n2. 🎲 Theme Roulette (Acak Gaya Cepat)\nTekan tombol dadu di pojok atas atau saat membuat catatan baru untuk mendapatkan tema aesthetic acak secara instan!\n\n3. 📝 Kustomisasi Pola Kertas & Bentuk Kartu\nBuka Studio Gaya (🎨) untuk mengatur:\n• Pola kertas: Garis Buku (Ruled), Grid Titik (Dots), Vintage Parchment, dan Circuit.\n• Bentuk kartu: Rounded, Brutalist, Sticky Note, dan Pinggiran Kertas (Deckle Edge).\n• Hiasan: Perangko (Stamps), Pin Kuningan, dan Selotip Washi Tape.\n\n4. ✅ Checklist & To-Do Interaktif\nFormat teks dengan tanda kurung siku seperti [ ] Tugas atau [x] Selesai untuk to-do list praktis dengan tombol centang langsung di kartu catatan.\n\n5. 🏛️ Theme Vault (Brankas Tema)\nSimpan kombinasi gaya yang Anda sukai ke dalam Brankas Tema agar bisa digunakan kembali untuk catatan lain kapan saja.\n\n6. 🌐 Pilihan Bahasa (Multi-Language)\nGanti bahasa aplikasi kapan saja antara Bahasa Indonesia 🇮🇩, English 🇺🇸, Español 🇪🇸, atau 日本語 🇯🇵 melalui tombol bahasa di menu atas.\n\n7. 🔒 100% Offline & Privat\nSemua catatan tersimpan secara privat di penyimpanan SQLite lokal perangkat Anda tanpa memerlukan internet.",
                        checklistRaw = "[x] Selamat datang di NoteS! 🎉\n[x] Pilih bahasa aplikasi yang nyaman\n[ ] Buat catatan pertamamu dengan tombol (+)\n[ ] Coba tombol Dadu Roulette 🎲 untuk tema acak\n[ ] Buka Theme Vault 🎨 dan simpan gaya favoritmu",
                        category = "General",
                        isPinned = true,
                        isFavorite = true,
                        createdTimestamp = now,
                        updatedTimestamp = now,
                        themeId = 179240L,
                        themeName = "Warm Kraft NoteS",
                        bgColorHex = 0xFFFDF6E9,
                        bgGradientEndHex = 0xFFF7E6CA,
                        textColorHex = 0xFF2D1808,
                        accentColorHex = 0xFFF07122,
                        patternType = PatternTypes.RULED,
                        cardShape = CardShapes.ROUNDED,
                        fontFamily = FontFamilies.SANS,
                        stamp = Stamps.STAR,
                        pinType = PinTypes.WASHI_TAPE,
                        borderWidthDp = 2,
                        isDark = false
                    )
                )
            }
        }
    }
}
