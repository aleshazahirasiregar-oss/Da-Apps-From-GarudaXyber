package com.note.styles.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.note.styles.data.model.ChecklistItem
import com.note.styles.data.model.FontFamilies
import com.note.styles.data.model.NoteEntity
import com.note.styles.data.model.NoteStyle
import com.note.styles.data.model.Stamps
import com.note.styles.theme.ThemeEngine
import com.note.styles.ui.components.ThemeCustomizerSheet
import com.note.styles.ui.components.patternBackground

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NoteDetailScreen(
    initialNote: NoteEntity,
    onSaveNote: (NoteEntity) -> Unit,
    onDeleteNote: (NoteEntity) -> Unit,
    onSaveFavoriteTheme: (NoteStyle) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(initialNote.title) }
    var content by remember { mutableStateOf(initialNote.content) }
    var category by remember { mutableStateOf(initialNote.category) }
    var isPinned by remember { mutableStateOf(initialNote.isPinned) }
    var isFavorite by remember { mutableStateOf(initialNote.isFavorite) }
    var currentStyle by remember { mutableStateOf(initialNote.toNoteStyle()) }
    var showStyleSheet by remember { mutableStateOf(false) }

    val checklist = remember {
        mutableStateListOf<ChecklistItem>().apply {
            addAll(initialNote.getChecklistItems())
        }
    }
    var newChecklistInput by remember { mutableStateOf("") }

    val categories = listOf("General", "Ideas", "Work", "Personal", "Creative", "Study")

    // Helper to build updated note
    fun buildCurrentNote(): NoteEntity {
        return initialNote.copy(
            title = title,
            content = content,
            checklistRaw = NoteEntity.formatChecklist(checklist),
            category = category,
            isPinned = isPinned,
            isFavorite = isFavorite,
            updatedTimestamp = System.currentTimeMillis()
        ).applyStyle(currentStyle)
    }

    // Save on back
    BackHandler {
        onSaveNote(buildCurrentNote())
        onBack()
    }

    val bgColor = Color(currentStyle.bgColorHex)
    val bgEndColor = currentStyle.bgGradientEndHex?.let { Color(it) } ?: bgColor
    val textColor = Color(currentStyle.textColorHex)
    val accentColor = Color(currentStyle.accentColorHex)

    val composeFontFamily = when (currentStyle.fontFamily) {
        FontFamilies.SERIF -> FontFamily.Serif
        FontFamilies.MONO -> FontFamily.Monospace
        FontFamilies.CURSIVE -> FontFamily.Cursive
        else -> FontFamily.SansSerif
    }

    val backgroundBrush = Brush.verticalGradient(listOf(bgColor, bgEndColor))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (title.isBlank()) "Untitled Note" else title,
                            maxLines = 1,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            text = "Theme #${currentStyle.themeId}: ${currentStyle.themeName}",
                            fontSize = 11.sp,
                            color = textColor.copy(alpha = 0.6f),
                            fontFamily = FontFamily.Monospace
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onSaveNote(buildCurrentNote())
                            onBack()
                        },
                        modifier = Modifier.testTag("detail_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = textColor
                        )
                    }
                },
                actions = {
                    // Roulette Quick Spin
                    IconButton(
                        onClick = {
                            currentStyle = ThemeEngine.getRandomTheme()
                        },
                        modifier = Modifier.testTag("quick_roulette_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = "Random Theme Roulette",
                            tint = accentColor
                        )
                    }

                    // Open Full Style Sheet
                    IconButton(
                        onClick = { showStyleSheet = true },
                        modifier = Modifier.testTag("open_style_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Style Note",
                            tint = accentColor
                        )
                    }

                    // Pin toggle
                    IconButton(
                        onClick = { isPinned = !isPinned },
                        modifier = Modifier.testTag("pin_note_btn")
                    ) {
                        Icon(
                            imageVector = if (isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                            contentDescription = "Pin Note",
                            tint = if (isPinned) accentColor else textColor.copy(alpha = 0.5f)
                        )
                    }

                    // Delete Note
                    IconButton(
                        onClick = {
                            onDeleteNote(initialNote)
                            onBack()
                        },
                        modifier = Modifier.testTag("delete_note_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Note",
                            tint = textColor.copy(alpha = 0.7f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .patternBackground(
                    patternType = currentStyle.patternType,
                    patternColor = if (currentStyle.isDark) Color.White else Color.Black,
                    isDark = currentStyle.isDark
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                // Category Pills Row & Stamp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSelected = category == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) accentColor else textColor.copy(alpha = 0.08f)
                                    )
                                    .clickable { category = cat }
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSelected) Color.White else textColor.copy(alpha = 0.7f),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    if (currentStyle.stamp != Stamps.NONE) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .rotate(-5f)
                                .border(1.5.dp, accentColor, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = currentStyle.stamp,
                                color = accentColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Title Input
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    textStyle = TextStyle(
                        color = textColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = composeFontFamily
                    ),
                    cursorBrush = SolidColor(accentColor),
                    decorationBox = { innerTextField ->
                        if (title.isEmpty()) {
                            Text(
                                text = "Title...",
                                color = textColor.copy(alpha = 0.4f),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = composeFontFamily
                            )
                        }
                        innerTextField()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("note_title_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                HorizontalDivider(
                    color = textColor.copy(alpha = 0.12f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Checklist Section (if any items or option to add)
                if (checklist.isNotEmpty()) {
                    Text(
                        text = "TASKS & CHECKLIST",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    checklist.forEachIndexed { index, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    checklist[index] = item.copy(isCompleted = !item.isCompleted)
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = if (item.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircleOutline,
                                    contentDescription = null,
                                    tint = if (item.isCompleted) accentColor else textColor.copy(alpha = 0.5f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.text,
                                fontSize = 14.sp,
                                color = if (item.isCompleted) textColor.copy(alpha = 0.5f) else textColor,
                                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                fontFamily = composeFontFamily,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { checklist.removeAt(index) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove item",
                                    tint = textColor.copy(alpha = 0.4f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Add Checklist item row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BasicTextField(
                        value = newChecklistInput,
                        onValueChange = { newChecklistInput = it },
                        textStyle = TextStyle(
                            color = textColor,
                            fontSize = 14.sp,
                            fontFamily = composeFontFamily
                        ),
                        cursorBrush = SolidColor(accentColor),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            if (newChecklistInput.isNotBlank()) {
                                checklist.add(ChecklistItem(id = checklist.size, text = newChecklistInput.trim(), isCompleted = false))
                                newChecklistInput = ""
                            }
                        }),
                        decorationBox = { inner ->
                            if (newChecklistInput.isEmpty()) {
                                Text(
                                    text = "+ Add checklist item...",
                                    color = textColor.copy(alpha = 0.4f),
                                    fontSize = 13.sp,
                                    fontFamily = composeFontFamily
                                )
                            }
                            inner()
                        },
                        modifier = Modifier.weight(1f)
                    )

                    if (newChecklistInput.isNotBlank()) {
                        IconButton(
                            onClick = {
                                checklist.add(ChecklistItem(id = checklist.size, text = newChecklistInput.trim(), isCompleted = false))
                                newChecklistInput = ""
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Item",
                                tint = accentColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Content Input (Multiline)
                BasicTextField(
                    value = content,
                    onValueChange = { content = it },
                    textStyle = TextStyle(
                        color = textColor,
                        fontSize = 16.sp,
                        fontFamily = composeFontFamily,
                        lineHeight = 24.sp
                    ),
                    cursorBrush = SolidColor(accentColor),
                    decorationBox = { innerTextField ->
                        if (content.isEmpty()) {
                            Text(
                                text = "Start writing your note here...\n\nTap the 🎨 palette icon to change patterns, paper finish, card styles, or explore 2 Million Themes!",
                                color = textColor.copy(alpha = 0.35f),
                                fontSize = 16.sp,
                                fontFamily = composeFontFamily,
                                lineHeight = 24.sp
                            )
                        }
                        innerTextField()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .testTag("note_content_input")
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    if (showStyleSheet) {
        ThemeCustomizerSheet(
            currentStyle = currentStyle,
            onStyleChange = { currentStyle = it },
            onSaveFavoriteTheme = onSaveFavoriteTheme,
            onDismiss = { showStyleSheet = false }
        )
    }
}
