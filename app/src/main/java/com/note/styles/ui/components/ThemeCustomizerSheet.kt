package com.note.styles.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.note.styles.data.model.CardShapes
import com.note.styles.data.model.FontFamilies
import com.note.styles.data.model.NoteStyle
import com.note.styles.data.model.PatternTypes
import com.note.styles.data.model.PinTypes
import com.note.styles.data.model.Stamps
import com.note.styles.theme.ThemeEngine

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ThemeCustomizerSheet(
    currentStyle: NoteStyle,
    onStyleChange: (NoteStyle) -> Unit,
    onSaveFavoriteTheme: (NoteStyle) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTab by remember { mutableIntStateOf(0) }
    var customThemeIdInput by remember { mutableStateOf("") }

    val tabs = listOf("2M Roulette", "Showcase", "Paper & Pattern", "Card Frame", "Font & Stamps", "Colors")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        modifier = Modifier.testTag("theme_customizer_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Theme Studio",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Theme #${currentStyle.themeId}: ${currentStyle.themeName}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Row {
                    IconButton(
                        onClick = { onSaveFavoriteTheme(currentStyle) },
                        modifier = Modifier.testTag("save_theme_fav_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Save to favorites",
                            tint = Color(0xFFEF4444)
                        )
                    }
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("apply_theme_btn")
                    ) {
                        Text("Apply")
                    }
                }
            }

            // Tab Navigation
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TAB 0: 2M Roulette & Direct ID Search
            if (selectedTab == 0) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    // Big Dice Roll Button
                    Button(
                        onClick = {
                            val newStyle = ThemeEngine.getRandomTheme()
                            onStyleChange(newStyle)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("roulette_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "🎲 Roll Theme Roulette (1 in 2,000,000)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Direct Theme Number Lookup",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Every number between 1 and 2,000,000 generates a unique deterministic theme!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = customThemeIdInput,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() } && input.length <= 8) {
                                    customThemeIdInput = input
                                }
                            },
                            placeholder = { Text("e.g. 777 or 1842019") },
                            leadingIcon = {
                                Text("#", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Go
                            ),
                            keyboardActions = KeyboardActions(
                                onGo = {
                                    val id = customThemeIdInput.toLongOrNull()
                                    if (id != null && id in 1L..ThemeEngine.MAX_THEME_COUNT) {
                                        onStyleChange(ThemeEngine.getThemeById(id))
                                    }
                                }
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_id_input")
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                val id = customThemeIdInput.toLongOrNull()
                                if (id != null && id in 1L..ThemeEngine.MAX_THEME_COUNT) {
                                    onStyleChange(ThemeEngine.getThemeById(id))
                                }
                            },
                            modifier = Modifier.testTag("apply_theme_id_btn")
                        ) {
                            Text("Go")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Jump Chips
                    Text(
                        text = "Popular Theme Numbers:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(1L, 777L, 1420L, 8888L, 50201L, 99999L, 256100L, 444333L, 1205555L, 1842019L, 2000000L).forEach { id ->
                            FilledTonalButton(
                                onClick = { onStyleChange(ThemeEngine.getThemeById(id)) },
                                modifier = Modifier.testTag("popular_chip_$id")
                            ) {
                                Text("#$id", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // TAB 1: Curated Showcase
            if (selectedTab == 1) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Hand-Crafted Preset Collections",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    ThemeEngine.CURATED_PRESETS.forEach { preset ->
                        val isSelected = currentStyle.themeId == preset.themeId
                        Card(
                            onClick = { onStyleChange(preset) },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(preset.bgColorHex)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .border(
                                    width = if (isSelected) 2.5.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .testTag("curated_theme_${preset.themeId}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = preset.themeName,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(preset.textColorHex),
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "Theme #${preset.themeId} • ${preset.cardShape.replaceFirstChar { it.uppercase() }} • ${preset.patternType.replaceFirstChar { it.uppercase() }}",
                                        color = Color(preset.textColorHex).copy(alpha = 0.7f),
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color(preset.accentColorHex),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // TAB 2: Paper & Pattern
            if (selectedTab == 2) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Paper Pattern & Texture",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PatternTypes.ALL.forEach { pattern ->
                            val isSelected = currentStyle.patternType == pattern
                            FilterChip(
                                selected = isSelected,
                                onClick = { onStyleChange(currentStyle.copy(patternType = pattern)) },
                                label = {
                                    Text(
                                        text = when (pattern) {
                                            PatternTypes.RULED -> "📝 Ruled Notebook"
                                            PatternTypes.DOTS -> "⏺️ Dot Grid (Bullet)"
                                            PatternTypes.GRAPH -> "📐 Graph Paper"
                                            PatternTypes.PARCHMENT -> "📜 Parchment Grain"
                                            PatternTypes.CIRCUIT -> "⚡ Cyber Circuit"
                                            PatternTypes.TERRAZZO -> "🎉 Terrazzo Confetti"
                                            PatternTypes.WAVES -> "🌊 Wave Contours"
                                            PatternTypes.HATCH -> "📏 Hatching"
                                            else -> "📄 Clean Blank"
                                        }
                                    )
                                },
                                modifier = Modifier.testTag("pattern_$pattern")
                            )
                        }
                    }
                }
            }

            // TAB 3: Card Frame & Shape
            if (selectedTab == 3) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Card Material & Finish",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CardShapes.ALL.forEach { shape ->
                            val isSelected = currentStyle.cardShape == shape
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    onStyleChange(
                                        currentStyle.copy(
                                            cardShape = shape,
                                            borderWidthDp = if (shape == CardShapes.BRUTALIST) 2 else 1
                                        )
                                    )
                                },
                                label = {
                                    Text(
                                        text = when (shape) {
                                            CardShapes.ROUNDED -> "🔲 Rounded Modern"
                                            CardShapes.STICKY -> "📌 Sticky Note"
                                            CardShapes.BRUTALIST -> "⬛ Stark Brutalist"
                                            CardShapes.GLASS -> "🪟 Frosted Glass"
                                            CardShapes.DECKLE -> "📜 Vintage Deckle"
                                            else -> "⬜ Minimal Flat"
                                        }
                                    )
                                },
                                modifier = Modifier.testTag("card_shape_$shape")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Border Width",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(0, 1, 2, 3).forEach { width ->
                            FilterChip(
                                selected = currentStyle.borderWidthDp == width,
                                onClick = { onStyleChange(currentStyle.copy(borderWidthDp = width)) },
                                label = { Text("${width}dp") }
                            )
                        }
                    }
                }
            }

            // TAB 4: Font & Stamps
            if (selectedTab == 4) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Typography Style",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FontFamilies.ALL.forEach { font ->
                            FilterChip(
                                selected = currentStyle.fontFamily == font,
                                onClick = { onStyleChange(currentStyle.copy(fontFamily = font)) },
                                label = {
                                    Text(
                                        text = when (font) {
                                            FontFamilies.SERIF -> "Serif (Book)"
                                            FontFamilies.MONO -> "Mono (Typewriter)"
                                            FontFamilies.CURSIVE -> "Cursive (Script)"
                                            else -> "Sans (Clean)"
                                        }
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Mood Stamp",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Stamps.ALL.forEach { stamp ->
                            FilterChip(
                                selected = currentStyle.stamp == stamp,
                                onClick = { onStyleChange(currentStyle.copy(stamp = stamp)) },
                                label = { Text(if (stamp == Stamps.NONE) "None" else stamp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Pin / Header Fastener",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PinTypes.ALL.forEach { pin ->
                            FilterChip(
                                selected = currentStyle.pinType == pin,
                                onClick = { onStyleChange(currentStyle.copy(pinType = pin)) },
                                label = {
                                    Text(
                                        text = when (pin) {
                                            PinTypes.WASHI_TAPE -> "🩹 Washi Tape"
                                            PinTypes.PUSHPIN_RED -> "🔴 Red Pushpin"
                                            PinTypes.PUSHPIN_BRASS -> "🟡 Brass Pushpin"
                                            PinTypes.PAPERCLIP -> "📎 Paperclip"
                                            else -> "None"
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // TAB 5: Colors & Palettes
            if (selectedTab == 5) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Base Color Palette",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val paletteOptions = listOf(
                        Triple(0xFFFFFDF8, 0xFF1C1917, "Ivory Classic"),
                        Triple(0xFF0F172A, 0xFF38BDF8, "Midnight Cyber"),
                        Triple(0xFFFEF08A, 0xFF713F12, "Sticky Sun"),
                        Triple(0xFFF1F6F0, 0xFF14301B, "Matcha Tea"),
                        Triple(0xFFFDF6E2, 0xFF432818, "Parchment"),
                        Triple(0xFFFDF2F8, 0xFF831843, "Rose Pastel"),
                        Triple(0xFFF5F3FF, 0xFF4C1D95, "Lavender"),
                        Triple(0xFF18181B, 0xFFE4E4E7, "Dark Slate"),
                        Triple(0xFFE0F2FE, 0xFF0369A1, "Ocean Blue"),
                        Triple(0xFFFFFBEB, 0xFF78350F, "Warm Ochre")
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        paletteOptions.forEach { (bg, text, name) ->
                            val isSelected = currentStyle.bgColorHex == bg
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(bg))
                                    .border(
                                        width = if (isSelected) 2.5.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        val isDarkTheme = (bg and 0xFF) < 50
                                        onStyleChange(
                                            currentStyle.copy(
                                                bgColorHex = bg,
                                                bgGradientEndHex = null,
                                                textColorHex = text,
                                                isDark = isDarkTheme
                                            )
                                        )
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = name,
                                    color = Color(text),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Accent Color",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        listOf(
                            0xFFD97706L, 0xFFEC4899L, 0xFF6366F1L, 0xFF10B981L,
                            0xFFF43F5EL, 0xFF06B6D4L, 0xFF8B5CF6L, 0xFFEAB308L
                        ).forEach { accent ->
                            val isSelected = currentStyle.accentColorHex == accent
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(accent))
                                    .border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = Color.White,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        onStyleChange(currentStyle.copy(accentColorHex = accent))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
