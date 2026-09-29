package com.note.styles.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.note.styles.R
import com.note.styles.util.AppLanguages
import com.note.styles.util.AppStrings
import com.note.styles.util.LanguageItem

enum class OnboardingStep {
    WELCOME_FEATURES,
    LANGUAGE_SELECTION
}

@Composable
fun OnboardingScreen(
    initialLanguage: String = "id",
    onCompleteSetup: (selectedLanguage: String) -> Unit
) {
    var currentStep by remember { mutableStateOf(OnboardingStep.WELCOME_FEATURES) }
    var selectedLang by remember { mutableStateOf(initialLanguage) }
    val strings = remember(selectedLang) { AppStrings(selectedLang) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                if (targetState == OnboardingStep.LANGUAGE_SELECTION) {
                    (slideInHorizontally { it } + fadeIn()).togetherWith(
                        slideOutHorizontally { -it / 3 } + fadeOut()
                    )
                } else {
                    (slideInHorizontally { -it / 3 } + fadeIn()).togetherWith(
                        slideOutHorizontally { it } + fadeOut()
                    )
                }
            },
            label = "onboarding_step_transition"
        ) { step ->
            when (step) {
                OnboardingStep.WELCOME_FEATURES -> {
                    WelcomeAndFeaturesStep(
                        strings = strings,
                        onLetsGoClicked = {
                            currentStep = OnboardingStep.LANGUAGE_SELECTION
                        }
                    )
                }
                OnboardingStep.LANGUAGE_SELECTION -> {
                    LanguageSelectionStep(
                        strings = strings,
                        selectedLanguage = selectedLang,
                        onSelectLanguage = { lang ->
                            selectedLang = lang
                        },
                        onBack = {
                            currentStep = OnboardingStep.WELCOME_FEATURES
                        },
                        onConfirm = {
                            onCompleteSetup(selectedLang)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun WelcomeAndFeaturesStep(
    strings: AppStrings,
    onLetsGoClicked: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = onLetsGoClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("onboarding_lets_go_btn"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = strings.buttonLetsGo,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // App Icon Emblem
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF161B26))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "NoteS Logo",
                    modifier = Modifier.size(76.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                Color(0xFFEC4899).copy(alpha = 0.15f)
                            )
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "NoteS v2.0.0 • 2,000,000 Themes",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = strings.welcomeTitle,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = strings.welcomeSubtitle,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = strings.featuresHeader,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Feature Highlights Cards
            FeatureHighlightCard(
                icon = Icons.Default.Palette,
                iconTint = Color(0xFF8B5CF6),
                iconBg = Color(0xFF8B5CF6).copy(alpha = 0.12f),
                title = strings.feat1Title,
                description = strings.feat1Desc
            )

            Spacer(modifier = Modifier.height(10.dp))

            FeatureHighlightCard(
                icon = Icons.Default.Style,
                iconTint = Color(0xFFF97316),
                iconBg = Color(0xFFF97316).copy(alpha = 0.12f),
                title = strings.feat2Title,
                description = strings.feat2Desc
            )

            Spacer(modifier = Modifier.height(10.dp))

            FeatureHighlightCard(
                icon = Icons.Default.Casino,
                iconTint = Color(0xFFEC4899),
                iconBg = Color(0xFFEC4899).copy(alpha = 0.12f),
                title = strings.feat3Title,
                description = strings.feat3Desc
            )

            Spacer(modifier = Modifier.height(10.dp))

            FeatureHighlightCard(
                icon = Icons.Default.Checklist,
                iconTint = Color(0xFF10B981),
                iconBg = Color(0xFF10B981).copy(alpha = 0.12f),
                title = strings.feat4Title,
                description = strings.feat4Desc
            )

            Spacer(modifier = Modifier.height(10.dp))

            FeatureHighlightCard(
                icon = Icons.Default.AutoAwesome,
                iconTint = Color(0xFF3B82F6),
                iconBg = Color(0xFF3B82F6).copy(alpha = 0.12f),
                title = strings.feat5Title,
                description = strings.feat5Desc
            )

            Spacer(modifier = Modifier.height(10.dp))

            FeatureHighlightCard(
                icon = Icons.Default.Shield,
                iconTint = Color(0xFF14B8A6),
                iconBg = Color(0xFF14B8A6).copy(alpha = 0.12f),
                title = strings.feat6Title,
                description = strings.feat6Desc
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FeatureHighlightCard(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun LanguageSelectionStep(
    strings: AppStrings,
    selectedLanguage: String,
    onSelectLanguage: (String) -> Unit,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("onboarding_lang_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = strings.selectLanguageTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("onboarding_confirm_lang_btn"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = strings.buttonStartApp,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header icon badge
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = strings.selectLanguageTitle,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = strings.selectLanguageSubtitle,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Language cards
            AppLanguages.list.forEach { lang ->
                val isSelected = lang.code == selectedLanguage
                LanguageChoiceCard(
                    item = lang,
                    isSelected = isSelected,
                    onClick = { onSelectLanguage(lang.code) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun LanguageChoiceCard(
    item: LanguageItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    }

    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("lang_card_${item.code}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flag
            Text(
                text = item.flag,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.nativeName,
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.tag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.englishName,
                    fontSize = 12.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Radio / Checkmark icon
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(26.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                )
            }
        }
    }
}
