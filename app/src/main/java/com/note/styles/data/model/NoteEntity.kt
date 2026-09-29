package com.note.styles.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val checklistRaw: String = "",
    val category: String = "General",
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val createdTimestamp: Long = System.currentTimeMillis(),
    val updatedTimestamp: Long = System.currentTimeMillis(),
    
    // Styling properties
    val themeId: Long = 1L,
    val themeName: String = "Classic Paper",
    val bgColorHex: Long = 0xFFFFFDF8,
    val bgGradientEndHex: Long? = 0xFFF5EEDB,
    val textColorHex: Long = 0xFF1C1917,
    val accentColorHex: Long = 0xFFD97706,
    val patternType: String = PatternTypes.RULED,
    val cardShape: String = CardShapes.ROUNDED,
    val fontFamily: String = FontFamilies.SANS,
    val stamp: String = Stamps.NONE,
    val pinType: String = PinTypes.WASHI_TAPE,
    val borderWidthDp: Int = 1,
    val isDark: Boolean = false
) {
    fun toNoteStyle(): NoteStyle {
        return NoteStyle(
            themeId = themeId,
            themeName = themeName,
            bgColorHex = bgColorHex,
            bgGradientEndHex = bgGradientEndHex,
            textColorHex = textColorHex,
            accentColorHex = accentColorHex,
            patternType = patternType,
            cardShape = cardShape,
            fontFamily = fontFamily,
            stamp = stamp,
            pinType = pinType,
            borderWidthDp = borderWidthDp,
            isDark = isDark
        )
    }

    fun applyStyle(style: NoteStyle): NoteEntity {
        return copy(
            themeId = style.themeId,
            themeName = style.themeName,
            bgColorHex = style.bgColorHex,
            bgGradientEndHex = style.bgGradientEndHex,
            textColorHex = style.textColorHex,
            accentColorHex = style.accentColorHex,
            patternType = style.patternType,
            cardShape = style.cardShape,
            fontFamily = style.fontFamily,
            stamp = style.stamp,
            pinType = style.pinType,
            borderWidthDp = style.borderWidthDp,
            isDark = style.isDark,
            updatedTimestamp = System.currentTimeMillis()
        )
    }

    fun getChecklistItems(): List<ChecklistItem> {
        if (checklistRaw.isBlank()) return emptyList()
        return checklistRaw.lines().filter { it.isNotBlank() }.mapIndexed { index, line ->
            val isChecked = line.startsWith("[x] ", ignoreCase = true)
            val text = when {
                line.startsWith("[x] ", ignoreCase = true) -> line.substring(4)
                line.startsWith("[ ] ") -> line.substring(4)
                else -> line
            }
            ChecklistItem(id = index, text = text, isCompleted = isChecked)
        }
    }

    companion object {
        fun formatChecklist(items: List<ChecklistItem>): String {
            return items.joinToString("\n") { item ->
                val check = if (item.isCompleted) "[x]" else "[ ]"
                "$check ${item.text}"
            }
        }
    }
}

data class ChecklistItem(
    val id: Int,
    val text: String,
    val isCompleted: Boolean
)
