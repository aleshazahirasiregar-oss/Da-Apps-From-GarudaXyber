package com.note.styles.data.model

data class NoteStyle(
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
    companion object {
        val DEFAULT = NoteStyle()
    }
}

object PatternTypes {
    const val BLANK = "blank"
    const val RULED = "ruled"
    const val DOTS = "dots"
    const val GRAPH = "graph"
    const val PARCHMENT = "parchment"
    const val CIRCUIT = "circuit"
    const val TERRAZZO = "terrazzo"
    const val WAVES = "waves"
    const val HATCH = "hatch"

    val ALL = listOf(BLANK, RULED, DOTS, GRAPH, PARCHMENT, CIRCUIT, TERRAZZO, WAVES, HATCH)
}

object CardShapes {
    const val ROUNDED = "rounded"
    const val STICKY = "sticky"
    const val BRUTALIST = "brutalist"
    const val GLASS = "glass"
    const val DECKLE = "deckle"
    const val MINIMAL = "minimal"

    val ALL = listOf(ROUNDED, STICKY, BRUTALIST, GLASS, DECKLE, MINIMAL)
}

object FontFamilies {
    const val SANS = "sans"
    const val SERIF = "serif"
    const val MONO = "mono"
    const val CURSIVE = "cursive"

    val ALL = listOf(SANS, SERIF, MONO, CURSIVE)
}

object Stamps {
    const val NONE = "none"
    const val APPROVED = "APPROVED"
    const val CONFIDENTIAL = "CONFIDENTIAL"
    const val IDEA = "IDEA 💡"
    const val STAR = "STAR ⭐"
    const val URGENT = "URGENT ⚡"
    const val DAILY = "DAILY 📅"
    const val DREAM = "DREAM 🌙"
    const val RECIPE = "RECIPE 🍳"
    const val QUOTE = "QUOTE 📜"

    val ALL = listOf(NONE, IDEA, STAR, URGENT, DAILY, DREAM, RECIPE, QUOTE, APPROVED, CONFIDENTIAL)
}

object PinTypes {
    const val NONE = "none"
    const val PUSHPIN_RED = "pushpin_red"
    const val PUSHPIN_BRASS = "pushpin_brass"
    const val WASHI_TAPE = "washi_tape"
    const val PAPERCLIP = "paperclip"

    val ALL = listOf(NONE, WASHI_TAPE, PUSHPIN_RED, PUSHPIN_BRASS, PAPERCLIP)
}
