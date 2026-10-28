package com.example.ui.screens

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameStage
import com.example.ui.components.WordGrid
import com.example.ui.components.WordWheel
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PersianAzure
import com.example.ui.theme.PersianAzureDark
import com.example.ui.theme.PersianAzureLight
import com.example.ui.theme.RubyAccent
import com.example.ui.theme.TurquoiseDark
import com.example.ui.theme.TurquoiseLight
import com.example.ui.theme.TurquoisePrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WordWorkshopScreen(
    stage: GameStage,
    coins: Int,
    hapticEnabled: Boolean,
    onSpendCoins: (Int) -> Boolean,
    onAddCoins: (Int) -> Unit,
    onAllWordsFound: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()

    // Found words in current stage
    val foundWords = remember(stage.stageNumber) { mutableStateListOf<String>() }
    val bonusWordsFound = remember(stage.stageNumber) { mutableStateListOf<String>() }
    val revealedHints = remember(stage.stageNumber) { mutableStateMapOf<Int, Char>() }

    // Shuffled letters
    var wheelLetters by remember(stage.stageNumber) { mutableStateOf(stage.letters) }

    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var feedbackIsPositive by remember { mutableStateOf(true) }

    // Check if stage word grid is complete
    val isAllTargetWordsFound = stage.targetWords.all { foundWords.contains(it) }

    LaunchedEffect(isAllTargetWordsFound) {
        if (isAllTargetWordsFound) {
            delay(600)
            onAllWordsFound()
        }
    }

    fun showFeedback(msg: String, isPositive: Boolean) {
        feedbackMessage = msg
        feedbackIsPositive = isPositive
        coroutineScope.launch {
            delay(2000)
            if (feedbackMessage == msg) {
                feedbackMessage = null
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Stage Info Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(PersianAzureLight)
                        .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "مرحله ${toPersianDigits(stage.stageNumber)}: ${stage.stageTitle}",
                        color = GoldBright,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Bonus words chest
                Row(
                    modifier = Modifier
                        .testTag("bonus_words_chest")
                        .clip(RoundedCornerShape(12.dp))
                        .background(TurquoisePrimary.copy(alpha = 0.2f))
                        .border(1.dp, TurquoiseLight.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = "صندوق واژه‌های فرعی",
                        tint = TurquoiseLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "واژه‌های فرعی: ${toPersianDigits(bonusWordsFound.size)}",
                        color = TurquoiseLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Word Grid (carved tiles)
            WordGrid(
                targetWords = stage.targetWords,
                foundWords = foundWords.toSet(),
                revealedHints = revealedHints
            )

            // Feedback Toast / Alert
            AnimatedVisibility(visible = feedbackMessage != null) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (feedbackIsPositive) EmeraldGreen else RubyAccent)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = feedbackMessage ?: "",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Word Wheel (Gesture Letter Wheel)
        WordWheel(
            letters = wheelLetters,
            coins = coins,
            hapticEnabled = hapticEnabled,
            onWordCompleted = { word ->
                when {
                    stage.targetWords.contains(word) -> {
                        if (!foundWords.contains(word)) {
                            foundWords.add(word)
                            if (hapticEnabled) {
                                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                            }
                            showFeedback("آفرین! واژه «$word» پیدا شد ✨", true)
                        } else {
                            showFeedback("واژه «$word» را قبلاً پیدا کرده‌اید!", false)
                        }
                    }
                    stage.bonusWords.contains(word) -> {
                        if (!bonusWordsFound.contains(word)) {
                            bonusWordsFound.add(word)
                            onAddCoins(5)
                            if (hapticEnabled) {
                                view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                            }
                            showFeedback("واژه اضافه «$word»! +۵ سکه پاداش 🪙", true)
                        } else {
                            showFeedback("این واژه فرعی را قبلاً ثبت کرده‌اید!", false)
                        }
                    }
                    else -> {
                        if (hapticEnabled) {
                            view.performHapticFeedback(HapticFeedbackConstants.REJECT)
                        }
                        showFeedback("واژه در جدول وجود ندارد", false)
                    }
                }
            },
            onShuffle = {
                wheelLetters = wheelLetters.shuffled()
            },
            onHintClick = {
                // Find first uncompleted word and reveal its first letter
                val uncompletedIdx = stage.targetWords.indexOfFirst { !foundWords.contains(it) }
                if (uncompletedIdx != -1) {
                    if (onSpendCoins(30)) {
                        revealedHints[uncompletedIdx] = stage.targetWords[uncompletedIdx].first()
                        showFeedback("یک حرف با راهنما آشکار شد 💡", true)
                    } else {
                        showFeedback("سکه‌های شما کافی نیست! (نیاز به ۳۰ سکه)", false)
                    }
                }
            }
        )
    }
}
