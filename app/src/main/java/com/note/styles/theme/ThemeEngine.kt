package com.note.styles.theme

import android.graphics.Color as AndroidColor
import com.note.styles.data.model.CardShapes
import com.note.styles.data.model.FontFamilies
import com.note.styles.data.model.NoteStyle
import com.note.styles.data.model.PatternTypes
import com.note.styles.data.model.PinTypes
import com.note.styles.data.model.Stamps
import kotlin.math.abs
import kotlin.random.Random

object ThemeEngine {
    const val MAX_THEME_COUNT = 2_000_000L

    private val ADJECTIVES = listOf(
        "Velvet", "Cyber", "Vintage", "Solar", "Nordic", "Matcha", "Pastel", "Midnight",
        "Golden", "Emerald", "Obsidian", "Neon", "Cosmic", "Sakura", "Terracotta", "Paper",
        "Lavender", "Ocean", "Tokyo", "Autumn", "Ethereal", "Brutalist", "Gilded", "Retro",
        "Minimal", "Arctic", "Sunset", "Forest", "Desert", "Amber", "Crystal", "Lofi"
    )

    private val NOUNS = listOf(
        "Horizon", "Manuscript", "Pulse", "Grove", "Dusk", "Breeze", "Archive", "Nebula",
        "Journal", "Matrix", "Haze", "Papyrus", "Memo", "Blossom", "Oasis", "Sanctuary",
        "Canvas", "Echo", "Circuit", "Stone", "Serenade", "Vault", "Mirage", "Aura",
        "Draft", "Palette", "Solitude", "Spectrum", "Tide", "Fable", "Zenith", "Notes"
    )

    /**
     * Deterministically produces a complete, harmonious NoteStyle from any themeId in [1 .. 2,000,000].
     */
    fun getThemeById(themeId: Long): NoteStyle {
        val safeId = if (themeId <= 0) 1L else themeId
        
        // Check if matching curated preset first for prominent numbers
        CURATED_PRESETS.firstOrNull { it.themeId == safeId }?.let {
            return it
        }

        // Pseudo-random deterministic hash
        val seed = (safeId * 2654435761L) xor (safeId shr 16)
        val rng = Random(seed)

        // Palette Mood Family (0..7)
        val moodFamily = ((safeId / 7) % 8).toInt()
        val hue = (rng.nextFloat() * 360f)

        val (bgHsv, isDark) = when (moodFamily) {
            0 -> floatArrayOf(hue, 0.12f + rng.nextFloat() * 0.12f, 0.97f + rng.nextFloat() * 0.03f) to false // Soft pastel
            1 -> floatArrayOf(hue, 0.25f + rng.nextFloat() * 0.35f, 0.07f + rng.nextFloat() * 0.08f) to true  // OLED & dark cyber
            2 -> floatArrayOf(30f + rng.nextFloat() * 15f, 0.12f + rng.nextFloat() * 0.18f, 0.90f + rng.nextFloat() * 0.08f) to false // Parchment
            3 -> floatArrayOf(hue, 0.55f + rng.nextFloat() * 0.35f, 0.16f + rng.nextFloat() * 0.16f) to true  // Jewel tone
            4 -> floatArrayOf(hue, 0.40f + rng.nextFloat() * 0.30f, 0.92f + rng.nextFloat() * 0.07f) to false // Vibrant pop
            5 -> floatArrayOf(hue, 0.02f + rng.nextFloat() * 0.06f, 0.94f + rng.nextFloat() * 0.05f) to false // Minimal stone
            6 -> floatArrayOf(hue, 0.50f + rng.nextFloat() * 0.40f, 0.10f + rng.nextFloat() * 0.10f) to true  // Neon synth
            else -> floatArrayOf(70f + rng.nextFloat() * 70f, 0.18f + rng.nextFloat() * 0.25f, 0.88f + rng.nextFloat() * 0.10f) to false // Earthy botanical
        }

        val bgColorInt = AndroidColor.HSVToColor(bgHsv)
        val bgColorHex = (bgColorInt.toLong() and 0xFFFFFFFFL) or 0xFF000000L

        // Optional gradient end
        val hasGradient = (safeId % 3L == 0L)
        val bgGradientEndHex = if (hasGradient) {
            val endHsv = floatArrayOf((bgHsv[0] + 15f) % 360f, bgHsv[1] * 1.15f, (bgHsv[2] * 0.94f).coerceIn(0.05f, 1f))
            (AndroidColor.HSVToColor(endHsv).toLong() and 0xFFFFFFFFL) or 0xFF000000L
        } else {
            null
        }

        // Text color with guaranteed high contrast
        val textColorHex = if (isDark) {
            // Bright tinted text
            val textHsv = floatArrayOf((bgHsv[0] + 30f) % 360f, 0.15f, 0.96f)
            (AndroidColor.HSVToColor(textHsv).toLong() and 0xFFFFFFFFL) or 0xFF000000L
        } else {
            // Deep espresso/charcoal text
            val textHsv = floatArrayOf(bgHsv[0], 0.35f, 0.12f)
            (AndroidColor.HSVToColor(textHsv).toLong() and 0xFFFFFFFFL) or 0xFF000000L
        }

        // Accent color (Complementary or vivid triadic)
        val accentHue = (bgHsv[0] + 160f + (safeId % 50)) % 360f
        val accentHsv = floatArrayOf(accentHue, 0.75f, if (isDark) 0.95f else 0.65f)
        val accentColorHex = (AndroidColor.HSVToColor(accentHsv).toLong() and 0xFFFFFFFFL) or 0xFF000000L

        // Attributes
        val pattern = PatternTypes.ALL[((safeId / 13) % PatternTypes.ALL.size).toInt()]
        val cardShape = CardShapes.ALL[((safeId / 37) % CardShapes.ALL.size).toInt()]
        val font = FontFamilies.ALL[((safeId / 19) % FontFamilies.ALL.size).toInt()]
        val stamp = Stamps.ALL[((safeId / 23) % Stamps.ALL.size).toInt()]
        val pin = PinTypes.ALL[((safeId / 29) % PinTypes.ALL.size).toInt()]

        val adjIndex = (abs(seed shr 8) % ADJECTIVES.size).toInt()
        val nounIndex = (abs(seed shr 16) % NOUNS.size).toInt()
        val name = "${ADJECTIVES[adjIndex]} ${NOUNS[nounIndex]}"

        return NoteStyle(
            themeId = safeId,
            themeName = name,
            bgColorHex = bgColorHex,
            bgGradientEndHex = bgGradientEndHex,
            textColorHex = textColorHex,
            accentColorHex = accentColorHex,
            patternType = pattern,
            cardShape = cardShape,
            fontFamily = font,
            stamp = stamp,
            pinType = pin,
            borderWidthDp = if (cardShape == CardShapes.BRUTALIST) 2 else 1,
            isDark = isDark
        )
    }

    fun getRandomTheme(): NoteStyle {
        val randomId = Random.nextLong(1L, MAX_THEME_COUNT + 1)
        return getThemeById(randomId)
    }

    // Curated high-aesthetic showcase themes
    val CURATED_PRESETS: List<NoteStyle> = listOf(
        NoteStyle(
            themeId = 1L,
            themeName = "Classic Paper",
            bgColorHex = 0xFFFFFDF8,
            bgGradientEndHex = 0xFFF7F2E7,
            textColorHex = 0xFF27272A,
            accentColorHex = 0xFFD97706,
            patternType = PatternTypes.RULED,
            cardShape = CardShapes.ROUNDED,
            fontFamily = FontFamilies.SERIF,
            stamp = Stamps.NONE,
            pinType = PinTypes.WASHI_TAPE,
            borderWidthDp = 1,
            isDark = false
        ),
        NoteStyle(
            themeId = 777L,
            themeName = "Cyberpunk Pulse",
            bgColorHex = 0xFF0B0F19,
            bgGradientEndHex = 0xFF151C2C,
            textColorHex = 0xFF38BDF8,
            accentColorHex = 0xFFF43F5E,
            patternType = PatternTypes.CIRCUIT,
            cardShape = CardShapes.BRUTALIST,
            fontFamily = FontFamilies.MONO,
            stamp = Stamps.IDEA,
            pinType = PinTypes.NONE,
            borderWidthDp = 2,
            isDark = true
        ),
        NoteStyle(
            themeId = 1420L,
            themeName = "Matcha Botanical",
            bgColorHex = 0xFFF1F6F0,
            bgGradientEndHex = 0xFFE2EDE0,
            textColorHex = 0xFF14301B,
            accentColorHex = 0xFF2D6A4F,
            patternType = PatternTypes.RULED,
            cardShape = CardShapes.ROUNDED,
            fontFamily = FontFamilies.SERIF,
            stamp = Stamps.DAILY,
            pinType = PinTypes.WASHI_TAPE,
            borderWidthDp = 1,
            isDark = false
        ),
        NoteStyle(
            themeId = 1842019L,
            themeName = "Antique Parchment",
            bgColorHex = 0xFFFDF6E2,
            bgGradientEndHex = 0xFFF4E4BA,
            textColorHex = 0xFF432818,
            accentColorHex = 0xFF99582A,
            patternType = PatternTypes.PARCHMENT,
            cardShape = CardShapes.DECKLE,
            fontFamily = FontFamilies.SERIF,
            stamp = Stamps.RECIPE,
            pinType = PinTypes.PUSHPIN_BRASS,
            borderWidthDp = 1,
            isDark = false
        ),
        NoteStyle(
            themeId = 50201L,
            themeName = "Solar Sticky Memo",
            bgColorHex = 0xFFFEF08A,
            bgGradientEndHex = 0xFFFDE047,
            textColorHex = 0xFF713F12,
            accentColorHex = 0xFFCA8A04,
            patternType = PatternTypes.DOTS,
            cardShape = CardShapes.STICKY,
            fontFamily = FontFamilies.SANS,
            stamp = Stamps.STAR,
            pinType = PinTypes.PUSHPIN_RED,
            borderWidthDp = 1,
            isDark = false
        ),
        NoteStyle(
            themeId = 8888L,
            themeName = "Obsidian Pro",
            bgColorHex = 0xFF121214,
            bgGradientEndHex = 0xFF1A1A1E,
            textColorHex = 0xFFE4E4E7,
            accentColorHex = 0xFFA1A1AA,
            patternType = PatternTypes.DOTS,
            cardShape = CardShapes.GLASS,
            fontFamily = FontFamilies.SANS,
            stamp = Stamps.NONE,
            pinType = PinTypes.NONE,
            borderWidthDp = 1,
            isDark = true
        ),
        NoteStyle(
            themeId = 99999L,
            themeName = "Cotton Candy",
            bgColorHex = 0xFFFDF2F8,
            bgGradientEndHex = 0xFFEDE9FE,
            textColorHex = 0xFF831843,
            accentColorHex = 0xFFEC4899,
            patternType = PatternTypes.TERRAZZO,
            cardShape = CardShapes.ROUNDED,
            fontFamily = FontFamilies.CURSIVE,
            stamp = Stamps.DREAM,
            pinType = PinTypes.WASHI_TAPE,
            borderWidthDp = 1,
            isDark = false
        ),
        NoteStyle(
            themeId = 256100L,
            themeName = "Blueprint Architect",
            bgColorHex = 0xFF0F284E,
            bgGradientEndHex = 0xFF0A1D3A,
            textColorHex = 0xFFE0F2FE,
            accentColorHex = 0xFF38BDF8,
            patternType = PatternTypes.GRAPH,
            cardShape = CardShapes.BRUTALIST,
            fontFamily = FontFamilies.MONO,
            stamp = Stamps.APPROVED,
            pinType = PinTypes.PAPERCLIP,
            borderWidthDp = 2,
            isDark = true
        ),
        NoteStyle(
            themeId = 444333L,
            themeName = "Lavender Haze",
            bgColorHex = 0xFFF5F3FF,
            bgGradientEndHex = 0xFFEDE9FE,
            textColorHex = 0xFF4C1D95,
            accentColorHex = 0xFF7C3AED,
            patternType = PatternTypes.RULED,
            cardShape = CardShapes.ROUNDED,
            fontFamily = FontFamilies.SANS,
            stamp = Stamps.QUOTE,
            pinType = PinTypes.WASHI_TAPE,
            borderWidthDp = 1,
            isDark = false
        ),
        NoteStyle(
            themeId = 1205555L,
            themeName = "Desert Terracotta",
            bgColorHex = 0xFFFDF4ED,
            bgGradientEndHex = 0xFFF9E0CE,
            textColorHex = 0xFF5C240E,
            accentColorHex = 0xFFC2410C,
            patternType = PatternTypes.WAVES,
            cardShape = CardShapes.ROUNDED,
            fontFamily = FontFamilies.SERIF,
            stamp = Stamps.DAILY,
            pinType = PinTypes.PUSHPIN_BRASS,
            borderWidthDp = 1,
            isDark = false
        ),
        NoteStyle(
            themeId = 2000000L,
            themeName = "Omega 2,000,000 Gold",
            bgColorHex = 0xFFFFFBEB,
            bgGradientEndHex = 0xFFFEF3C7,
            textColorHex = 0xFF78350F,
            accentColorHex = 0xFFD97706,
            patternType = PatternTypes.HATCH,
            cardShape = CardShapes.BRUTALIST,
            fontFamily = FontFamilies.SERIF,
            stamp = Stamps.STAR,
            pinType = PinTypes.WASHI_TAPE,
            borderWidthDp = 2,
            isDark = false
        )
    )
}
