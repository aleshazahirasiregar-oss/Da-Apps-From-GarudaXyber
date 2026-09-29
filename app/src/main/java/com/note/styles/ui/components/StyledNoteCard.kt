package com.note.styles.ui.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.note.styles.data.model.CardShapes
import com.note.styles.data.model.FontFamilies
import com.note.styles.data.model.NoteEntity
import com.note.styles.data.model.NoteStyle
import com.note.styles.data.model.PinTypes
import com.note.styles.data.model.Stamps

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StyledNoteCard(
    note: NoteEntity,
    onClick: () -> Unit,
    onToggleFavorite: (() -> Unit)? = null,
    onTogglePin: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val style = note.toNoteStyle()
    val bgColor = Color(style.bgColorHex)
    val bgEndColor = style.bgGradientEndHex?.let { Color(it) } ?: bgColor
    val textColor = Color(style.textColorHex)
    val accentColor = Color(style.accentColorHex)

    val composeFontFamily = when (style.fontFamily) {
        FontFamilies.SERIF -> FontFamily.Serif
        FontFamilies.MONO -> FontFamily.Monospace
        FontFamilies.CURSIVE -> FontFamily.Cursive
        else -> FontFamily.SansSerif
    }

    val cardShape = when (style.cardShape) {
        CardShapes.BRUTALIST -> RoundedCornerShape(4.dp)
        CardShapes.STICKY -> RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 4.dp, bottomEnd = 24.dp)
        CardShapes.GLASS -> RoundedCornerShape(20.dp)
        CardShapes.DECKLE -> RoundedCornerShape(8.dp)
        CardShapes.MINIMAL -> RoundedCornerShape(12.dp)
        else -> RoundedCornerShape(16.dp)
    }

    val backgroundBrush = if (style.bgGradientEndHex != null) {
        Brush.verticalGradient(listOf(bgColor, bgEndColor))
    } else {
        Brush.verticalGradient(listOf(bgColor, bgColor))
    }

    Box(
        modifier = modifier
            .testTag("note_card_${note.id}")
            .padding(vertical = 6.dp)
    ) {
        // Brutalist background solid shadow block
        if (style.cardShape == CardShapes.BRUTALIST) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .offset(x = 4.dp, y = 4.dp)
                    .background(Color.Black.copy(alpha = 0.85f), RoundedCornerShape(4.dp))
            )
        }

        // Main Card Surface
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (style.cardShape == CardShapes.BRUTALIST) 0.dp else 4.dp,
                    shape = cardShape
                )
                .clip(cardShape)
                .background(backgroundBrush)
                .then(
                    if (style.cardShape == CardShapes.BRUTALIST) {
                        Modifier.border(2.5.dp, Color.Black, cardShape)
                    } else if (style.cardShape == CardShapes.GLASS) {
                        Modifier.border(1.5.dp, Color.White.copy(alpha = 0.5f), cardShape)
                    } else if (style.borderWidthDp > 0) {
                        Modifier.border(style.borderWidthDp.dp, accentColor.copy(alpha = 0.25f), cardShape)
                    } else {
                        Modifier
                    }
                )
                .patternBackground(
                    patternType = style.patternType,
                    patternColor = if (style.isDark) Color.White else Color.Black,
                    isDark = style.isDark
                )
                .clickable { onClick() }
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top row: Pin indicator, category tag, favorite button, theme badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (note.isPinned) {
                            Icon(
                                imageVector = Icons.Filled.PushPin,
                                contentDescription = "Pinned Note",
                                tint = accentColor,
                                modifier = Modifier
                                    .size(16.dp)
                                    .rotate(35f)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        // Category Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(accentColor.copy(alpha = if (style.isDark) 0.3f else 0.15f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = note.category,
                                color = accentColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = composeFontFamily
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Stamp if active
                        if (style.stamp != Stamps.NONE) {
                            Box(
                                modifier = Modifier
                                    .rotate(-6f)
                                    .border(1.2.dp, accentColor, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = style.stamp,
                                    color = accentColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                        }

                        if (onToggleFavorite != null) {
                            IconButton(
                                onClick = onToggleFavorite,
                                modifier = Modifier
                                    .size(28.dp)
                                    .testTag("fav_btn_${note.id}")
                            ) {
                                Icon(
                                    imageVector = if (note.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Toggle favorite",
                                    tint = if (note.isFavorite) Color(0xFFEF4444) else textColor.copy(alpha = 0.5f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Title
                if (note.title.isNotBlank()) {
                    Text(
                        text = note.title,
                        color = textColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = composeFontFamily,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // Content preview
                if (note.content.isNotBlank()) {
                    Text(
                        text = note.content,
                        color = textColor.copy(alpha = 0.85f),
                        fontSize = 14.sp,
                        fontFamily = composeFontFamily,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp
                    )
                }

                // Checklist snippet
                val checklistItems = note.getChecklistItems()
                if (checklistItems.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(textColor.copy(alpha = 0.05f))
                            .padding(8.dp)
                    ) {
                        checklistItems.take(3).forEach { item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = if (item.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircleOutline,
                                    contentDescription = null,
                                    tint = if (item.isCompleted) accentColor else textColor.copy(alpha = 0.4f),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = item.text,
                                    fontSize = 12.sp,
                                    color = if (item.isCompleted) textColor.copy(alpha = 0.5f) else textColor,
                                    textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                    fontFamily = composeFontFamily,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        if (checklistItems.size > 3) {
                            Text(
                                text = "+${checklistItems.size - 3} more items",
                                fontSize = 10.sp,
                                color = textColor.copy(alpha = 0.6f),
                                modifier = Modifier.padding(top = 2.dp, start = 20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Footer: Theme ID tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Theme #${style.themeId}: ${style.themeName}",
                        color = textColor.copy(alpha = 0.5f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    // Sticky note folded corner indicator
                    if (style.cardShape == CardShapes.STICKY) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(accentColor.copy(alpha = 0.4f), RoundedCornerShape(bottomEnd = 16.dp))
                        )
                    }
                }
            }
        }

        // Header Pin / Washi Tape Overlay
        when (style.pinType) {
            PinTypes.WASHI_TAPE -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-6).dp)
                        .rotate(-3f)
                        .width(70.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(accentColor.copy(alpha = 0.75f))
                        .border(0.5.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(2.dp))
                )
            }
            PinTypes.PUSHPIN_RED -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-6).dp)
                        .size(14.dp)
                        .shadow(2.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFFE11D48))
                        .border(1.dp, Color(0xFFBE123C), CircleShape)
                )
            }
            PinTypes.PUSHPIN_BRASS -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-6).dp)
                        .size(14.dp)
                        .shadow(2.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFFD97706))
                        .border(1.dp, Color(0xFFB45309), CircleShape)
                )
            }
            PinTypes.PAPERCLIP -> {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 18.dp, y = (-6).dp)
                        .width(10.dp)
                        .height(22.dp)
                        .border(1.8.dp, Color(0xFF94A3B8), RoundedCornerShape(5.dp))
                )
            }
        }
    }
}
