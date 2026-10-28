package com.example.ui.screens

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.data.model.QuizOption
import com.example.data.model.QuizQuestion
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ParchmentBg
import com.example.ui.theme.ParchmentBorder
import com.example.ui.theme.ParchmentSurface
import com.example.ui.theme.PersianAzure
import com.example.ui.theme.PersianAzureDark
import com.example.ui.theme.PersianAzureLight
import com.example.ui.theme.RubyAccent
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TurquoiseDark
import com.example.ui.theme.TurquoiseLight
import com.example.ui.theme.TurquoisePrimary
import kotlinx.coroutines.delay

@Composable
fun KnowledgeQuizScreen(
    questions: List<QuizQuestion>,
    onQuizCompleted: (earnedStars: Int, correctAnswersCount: Int) -> Unit,
    hapticEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var correctAnswersCount by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    val currentQuestion = questions.getOrNull(currentQuestionIndex) ?: questions.first()

    // 15 seconds timer
    var timeLeftSeconds by remember(currentQuestionIndex) { mutableIntStateOf(15) }
    var timerRunning by remember(currentQuestionIndex) { mutableStateOf(true) }

    LaunchedEffect(currentQuestionIndex, timerRunning) {
        timeLeftSeconds = 15
        while (timerRunning && timeLeftSeconds > 0) {
            delay(1000L)
            timeLeftSeconds--
        }
        if (timeLeftSeconds == 0 && !isAnswerSubmitted) {
            // Time out: reveal correct answer
            isAnswerSubmitted = true
            timerRunning = false
            if (hapticEnabled) {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            }
        }
    }

    val timerProgress by animateFloatAsState(
        targetValue = timeLeftSeconds / 15f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "timer_progress"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mode Header Badge
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(GoldPrimary.copy(alpha = 0.2f))
                .border(1.dp, GoldBright, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⚔️ میدان دانایی (الهام از کوییز آف کینگز)",
                color = GoldBright,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Progress & Timer Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Question counter pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(PersianAzureLight)
                    .border(1.dp, TurquoisePrimary, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "پرسش ${toPersianDigits(currentQuestionIndex + 1)} از ${toPersianDigits(questions.size)}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            // Circular Countdown Timer
            Box(
                modifier = Modifier.size(50.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { timerProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = when {
                        timeLeftSeconds > 8 -> TurquoiseLight
                        timeLeftSeconds > 4 -> GoldBright
                        else -> RubyAccent
                    },
                    trackColor = PersianAzureLight,
                    strokeWidth = 4.dp
                )
                Text(
                    text = toPersianDigits(timeLeftSeconds),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(16.dp))
                .border(1.5.dp, GoldPrimary, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ParchmentBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentQuestion.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PersianAzureDark,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 4 Multiple Choice Options
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            currentQuestion.options.forEachIndexed { optionIndex, option ->
                QuizOptionCard(
                    option = option,
                    optionIndex = optionIndex,
                    isSelected = selectedOptionIndex == optionIndex,
                    isAnswerSubmitted = isAnswerSubmitted,
                    onClick = {
                        if (!isAnswerSubmitted) {
                            selectedOptionIndex = optionIndex
                            isAnswerSubmitted = true
                            timerRunning = false
                            if (option.isCorrect) {
                                correctAnswersCount++
                                if (hapticEnabled) {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                }
                            } else {
                                if (hapticEnabled) {
                                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                                }
                            }
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Explanation Box & Next Button
        AnimatedVisibility(visible = isAnswerSubmitted) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, TurquoisePrimary, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = PersianAzureLight)
                ) {
                    Text(
                        text = "💡 نکته دانایی: ${currentQuestion.explanation}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.95f),
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                            selectedOptionIndex = null
                            isAnswerSubmitted = false
                            timerRunning = true
                        } else {
                            // Completed all questions
                            val stars = when (correctAnswersCount) {
                                3 -> 3
                                2 -> 2
                                else -> 1
                            }
                            onQuizCompleted(stars, correctAnswersCount)
                        }
                    },
                    modifier = Modifier
                        .testTag("quiz_next_button")
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = PersianAzureDark
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (currentQuestionIndex < questions.size - 1) "پرسش بعدی" else "مشاهده پاداش و پایان مرحله",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = PersianAzureDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizOptionCard(
    option: QuizOption,
    optionIndex: Int,
    isSelected: Boolean,
    isAnswerSubmitted: Boolean,
    onClick: () -> Unit
) {
    val labels = listOf("الف", "ب", "ج", "د")
    val label = labels.getOrElse(optionIndex) { "${optionIndex + 1}" }

    val bgColor = when {
        isAnswerSubmitted && option.isCorrect -> EmeraldGreen
        isAnswerSubmitted && isSelected && !option.isCorrect -> RubyAccent
        isSelected -> TurquoisePrimary
        else -> PersianAzureLight
    }

    val borderColor = when {
        isAnswerSubmitted && option.isCorrect -> EmeraldGreen
        isAnswerSubmitted && isSelected && !option.isCorrect -> RubyAccent
        else -> GoldPrimary.copy(alpha = 0.4f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("quiz_option_${optionIndex}")
            .clip(RoundedCornerShape(12.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(enabled = !isAnswerSubmitted) { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Option label indicator (الف، ب، ج، د)
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = option.text,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            if (isAnswerSubmitted) {
                if (option.isCorrect) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "پاسخ صحیح",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                } else if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "پاسخ نادرست",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}
