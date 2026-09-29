package com.note.styles.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_themes")
data class SavedThemeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val themeId: Long,
    val name: String,
    val category: String = "Custom",
    val bgColorHex: Long,
    val bgGradientEndHex: Long?,
    val textColorHex: Long,
    val accentColorHex: Long,
    val patternType: String,
    val cardShape: String,
    val fontFamily: String,
    val stamp: String,
    val pinType: String,
    val borderWidthDp: Int = 1,
    val isDark: Boolean = false,
    val createdTimestamp: Long = System.currentTimeMillis()
) {
    fun toNoteStyle(): NoteStyle {
        return NoteStyle(
            themeId = themeId,
            themeName = name,
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

    companion object {
        fun fromNoteStyle(style: NoteStyle, customName: String? = null, category: String = "Custom"): SavedThemeEntity {
            return SavedThemeEntity(
                themeId = style.themeId,
                name = customName ?: style.themeName,
                category = category,
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
                isDark = style.isDark
            )
        }
    }
}
