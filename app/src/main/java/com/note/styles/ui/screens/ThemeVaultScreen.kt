package com.note.styles.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.note.styles.data.model.NoteEntity
import com.note.styles.data.model.NoteStyle
import com.note.styles.data.model.SavedThemeEntity
import com.note.styles.theme.ThemeEngine
import com.note.styles.ui.components.StyledNoteCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ThemeVaultScreen(
    savedThemes: List<SavedThemeEntity>,
    onBack: () -> Unit,
    onCreateNoteWithTheme: (NoteStyle) -> Unit,
    onSaveFavoriteTheme: (NoteStyle) -> Unit,
    onDeleteSavedTheme: (Long) -> Unit
) {
    var previewTheme by remember { mutableStateOf(ThemeEngine.CURATED_PRESETS[1]) }
    var customIdInput by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "2,000,000 Themes Vault",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Infinite Procedural & Aesthetic Styles",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("vault_back_btn")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Preview Card
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "LIVE THEME PREVIEW",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                val dummyNote = NoteEntity(
                    id = 0,
                    title = "Sample: ${previewTheme.themeName}",
                    content = "This is how notes look under Theme #${previewTheme.themeId}.\nCombine over 2M colors, paper lines, frames, and stamps.",
                    checklistRaw = "[x] 2,000,000 generative themes\n[ ] Pick your favorite vibe",
                    category = "Showcase",
                    isPinned = true,
                    isFavorite = true,
                    themeId = previewTheme.themeId,
                    themeName = previewTheme.themeName,
                    bgColorHex = previewTheme.bgColorHex,
                    bgGradientEndHex = previewTheme.bgGradientEndHex,
                    textColorHex = previewTheme.textColorHex,
                    accentColorHex = previewTheme.accentColorHex,
                    patternType = previewTheme.patternType,
                    cardShape = previewTheme.cardShape,
                    fontFamily = previewTheme.fontFamily,
                    stamp = previewTheme.stamp,
                    pinType = previewTheme.pinType,
                    borderWidthDp = previewTheme.borderWidthDp,
                    isDark = previewTheme.isDark
                )

                StyledNoteCard(
                    note = dummyNote,
                    onClick = { /* Preview only */ },
                    modifier = Modifier.fillMaxWidth()
                )

                // Buttons to use or save
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onCreateNoteWithTheme(previewTheme) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("create_note_with_theme_btn")
                    ) {
                        Icon(imageVector = Icons.Default.NoteAdd, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Create Note with This")
                    }

                    FilledTonalButton(
                        onClick = { onSaveFavoriteTheme(previewTheme) },
                        modifier = Modifier.testTag("save_theme_vault_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFEF4444))
                    }
                }
            }

            // Roulette & ID Lookup Box
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Casino,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Theme Roulette",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                            Text(
                                text = "1 to 2,000,000",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                previewTheme = ThemeEngine.getRandomTheme()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("vault_roulette_btn")
                        ) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("🎲 Spin Roulette (Pick Random Theme)")
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Jump to Theme Number",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = customIdInput,
                                onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 8) customIdInput = it },
                                placeholder = { Text("Enter ID (1 - 2000000)") },
                                leadingIcon = { Text("#", fontWeight = FontWeight.Bold) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Go),
                                keyboardActions = KeyboardActions(onGo = {
                                    val id = customIdInput.toLongOrNull()
                                    if (id != null && id in 1L..ThemeEngine.MAX_THEME_COUNT) {
                                        previewTheme = ThemeEngine.getThemeById(id)
                                    }
                                }),
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("vault_theme_id_input")
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    val id = customIdInput.toLongOrNull()
                                    if (id != null && id in 1L..ThemeEngine.MAX_THEME_COUNT) {
                                        previewTheme = ThemeEngine.getThemeById(id)
                                    }
                                }
                            ) {
                                Text("Inspect")
                            }
                        }
                    }
                }
            }

            // Tab bar for Showcases vs Saved
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    edgePadding = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Curated Showcases (${ThemeEngine.CURATED_PRESETS.size})") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("My Saved Styles (${savedThemes.size})") }
                    )
                }
            }

            if (selectedTab == 0) {
                items(ThemeEngine.CURATED_PRESETS) { preset ->
                    Card(
                        onClick = { previewTheme = preset },
                        colors = CardDefaults.cardColors(containerColor = Color(preset.bgColorHex)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (previewTheme.themeId == preset.themeId) 2.5.dp else 1.dp,
                                color = if (previewTheme.themeId == preset.themeId) MaterialTheme.colorScheme.primary else Color.Black.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .testTag("vault_preset_${preset.themeId}")
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
                                    fontSize = 15.sp,
                                    color = Color(preset.textColorHex)
                                )
                                Text(
                                    text = "Theme #${preset.themeId} • ${preset.cardShape} • ${preset.patternType}",
                                    fontSize = 11.sp,
                                    color = Color(preset.textColorHex).copy(alpha = 0.7f),
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Button(
                                onClick = { onCreateNoteWithTheme(preset) }
                            ) {
                                Text("Use")
                            }
                        }
                    }
                }
            } else {
                if (savedThemes.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No saved themes yet.\nTap the heart icon in the Theme Studio to save any theme!",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(savedThemes) { saved ->
                        val savedStyle = saved.toNoteStyle()
                        Card(
                            onClick = { previewTheme = savedStyle },
                            colors = CardDefaults.cardColors(containerColor = Color(saved.bgColorHex)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (previewTheme.themeId == saved.themeId) 2.5.dp else 1.dp,
                                    color = if (previewTheme.themeId == saved.themeId) MaterialTheme.colorScheme.primary else Color.Black.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = saved.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color(saved.textColorHex)
                                    )
                                    Text(
                                        text = "Theme #${saved.themeId}",
                                        fontSize = 11.sp,
                                        color = Color(saved.textColorHex).copy(alpha = 0.7f),
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                Row {
                                    IconButton(onClick = { onDeleteSavedTheme(saved.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete saved theme",
                                            tint = Color.Red
                                        )
                                    }
                                    Button(onClick = { onCreateNoteWithTheme(savedStyle) }) {
                                        Text("Use")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
