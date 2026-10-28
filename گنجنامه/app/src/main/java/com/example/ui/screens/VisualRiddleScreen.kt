package com.example.ui.screens

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VisualRiddle
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PersianAzure
import com.example.ui.theme.PersianAzureDark
import com.example.ui.theme.PersianAzureLight
import com.example.ui.theme.PersianAzureSurface
import com.example.ui.theme.TurquoiseDark
import com.example.ui.theme.TurquoiseLight
import com.example.ui.theme.TurquoisePrimary

@Composable
fun VisualRiddleScreen(
    riddle: VisualRiddle,
    onProceedToQuiz: () -> Unit,
    onHintUsed: () -> Unit,
    coins: Int,
    hapticEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val answerLength = riddle.answerWord.length

    // State of placed letters: slot index -> (char, originScrambleIndex)
    val placedSlots = remember { mutableStateListOf<Pair<Char, Int>?>().apply {
        repeat(answerLength) { add(null) }
    }}

    // Track which scrambled buttons are disabled
    val usedButtonIndices = remember { mutableStateListOf<Int>() }

    val currentAnswer = placedSlots.map { it?.first ?: ' ' }.joinToString("").trim()
    val isSolved = currentAnswer == riddle.answerWord

    val scaleAnim by animateFloatAsState(
        targetValue = if (isSolved) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "solved_scale"
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
                .background(TurquoisePrimary.copy(alpha = 0.2f))
                .border(1.dp, TurquoiseLight.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🎨 معمای تصویری (الهام از بابامیرزا)",
                color = TurquoiseLight,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Visual Illustration Canvas Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .shadow(8.dp, RoundedCornerShape(18.dp))
                .border(2.dp, GoldPrimary, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = PersianAzureSurface)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRiddleArt(riddle.visualType, size.width, size.height)
                }

                // Clue watermark / title overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, PersianAzureDark.copy(alpha = 0.9f))
                            )
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = riddle.title,
                        color = GoldBright,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Clue text description
        Text(
            text = riddle.clueText,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Answer Slots (Tap to remove)
        Row(
            modifier = Modifier
                .scale(scaleAnim)
                .testTag("riddle_answer_slots")
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until answerLength) {
                val slot = placedSlots[i]
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isSolved) Brush.verticalGradient(listOf(GoldBright, GoldDark))
                            else if (slot != null) Brush.verticalGradient(listOf(TurquoiseLight, TurquoiseDark))
                            else Brush.verticalGradient(listOf(PersianAzureLight, PersianAzure))
                        )
                        .border(
                            1.5.dp,
                            if (isSolved) GoldBright else TurquoisePrimary,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable(enabled = slot != null && !isSolved) {
                            if (slot != null) {
                                usedButtonIndices.remove(slot.second)
                                placedSlots[i] = null
                                if (hapticEnabled) {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = slot?.first?.toString() ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = if (isSolved) PersianAzureDark else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Scrambled Letter Tiles below
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val half = (riddle.scrambledLetters.size + 1) / 2
                val row1 = riddle.scrambledLetters.take(half)
                val row2 = riddle.scrambledLetters.drop(half)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row1.forEachIndexed { idx, char ->
                        val isUsed = usedButtonIndices.contains(idx)
                        ScrambleLetterButton(
                            char = char,
                            isUsed = isUsed,
                            onClick = {
                                if (!isUsed && !isSolved) {
                                    val emptyIdx = placedSlots.indexOfFirst { it == null }
                                    if (emptyIdx != -1) {
                                        placedSlots[emptyIdx] = Pair(char, idx)
                                        usedButtonIndices.add(idx)
                                        if (hapticEnabled) {
                                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                        }
                                    }
                                }
                            }
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row2.forEachIndexed { relIdx, char ->
                        val actualIdx = half + relIdx
                        val isUsed = usedButtonIndices.contains(actualIdx)
                        ScrambleLetterButton(
                            char = char,
                            isUsed = isUsed,
                            onClick = {
                                if (!isUsed && !isSolved) {
                                    val emptyIdx = placedSlots.indexOfFirst { it == null }
                                    if (emptyIdx != -1) {
                                        placedSlots[emptyIdx] = Pair(char, actualIdx)
                                        usedButtonIndices.add(actualIdx)
                                        if (hapticEnabled) {
                                            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Helper Tools (Clear / Hint)
        if (!isSolved) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        placedSlots.fill(null)
                        usedButtonIndices.clear()
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(PersianAzureLight)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "پاک کردن",
                        tint = GoldBright
                    )
                }

                Button(
                    onClick = {
                        // Hint reveals the first empty correct letter
                        val emptyIdx = placedSlots.indexOfFirst { it == null }
                        if (emptyIdx != -1 && coins >= 25) {
                            val correctChar = riddle.answerWord[emptyIdx]
                            val matchScrambleIdx = riddle.scrambledLetters.indices.firstOrNull {
                                riddle.scrambledLetters[it] == correctChar && !usedButtonIndices.contains(it)
                            }
                            if (matchScrambleIdx != null) {
                                placedSlots[emptyIdx] = Pair(correctChar, matchScrambleIdx)
                                usedButtonIndices.add(matchScrambleIdx)
                                onHintUsed()
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PersianAzureLight,
                        contentColor = GoldBright
                    )
                ) {
                    Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "راهنما (۲۵ 🪙)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Solved State & Proceed Button
        AnimatedVisibility(visible = isSolved) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(EmeraldGreen.copy(alpha = 0.25f))
                        .border(1.dp, EmeraldGreen, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "آفرین! معمای تصویری با موفقیت کشف شد (+۲۰ سکه)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onProceedToQuiz,
                    modifier = Modifier
                        .testTag("proceed_to_quiz_button")
                        .fillMaxWidth()
                        .height(52.dp),
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
                            text = "گام پایانی: میدان دانایی (کوییز آف کینگز)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
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
fun ScrambleLetterButton(
    char: Char,
    isUsed: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isUsed) Brush.verticalGradient(listOf(PersianAzureLight.copy(alpha = 0.3f), PersianAzureLight.copy(alpha = 0.3f)))
                else Brush.verticalGradient(listOf(PersianAzureLight, PersianAzure))
            )
            .border(
                1.5.dp,
                if (isUsed) Color.Transparent else GoldPrimary.copy(alpha = 0.8f),
                RoundedCornerShape(10.dp)
            )
            .clickable(enabled = !isUsed) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isUsed) "" else char.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = if (isUsed) Color.Gray else Color.White
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRiddleArt(
    visualType: String,
    w: Float,
    h: Float
) {
    // Beautiful Persian Canvas Art for riddles
    when (visualType) {
        "SIMURGH" -> {
            // Mountain peak silhouette
            val mountainPath = Path().apply {
                moveTo(0f, h)
                lineTo(w * 0.35f, h * 0.45f)
                lineTo(w * 0.5f, h * 0.25f)
                lineTo(w * 0.65f, h * 0.45f)
                lineTo(w, h)
                close()
            }
            drawPath(mountainPath, color = Color(0xFF102842))

            // Golden radiant sun/moon behind mountain
            drawCircle(
                color = Color(0xFFF6C845).copy(alpha = 0.7f),
                radius = 35.dp.toPx(),
                center = Offset(w * 0.5f, h * 0.25f)
            )

            // Simurgh mythical bird wings & feathers
            val wingLeft = Path().apply {
                moveTo(w * 0.5f, h * 0.28f)
                quadraticBezierTo(w * 0.3f, h * 0.1f, w * 0.15f, h * 0.18f)
                quadraticBezierTo(w * 0.32f, h * 0.24f, w * 0.5f, h * 0.3f)
            }
            val wingRight = Path().apply {
                moveTo(w * 0.5f, h * 0.28f)
                quadraticBezierTo(w * 0.7f, h * 0.1f, w * 0.85f, h * 0.18f)
                quadraticBezierTo(w * 0.68f, h * 0.24f, w * 0.5f, h * 0.3f)
            }
            drawPath(wingLeft, color = Color(0xFF00A896), style = Stroke(width = 6f))
            drawPath(wingRight, color = Color(0xFF00A896), style = Stroke(width = 6f))
            drawCircle(color = Color(0xFFD4AF37), radius = 8.dp.toPx(), center = Offset(w * 0.5f, h * 0.22f))
        }
        "ROSTAM" -> {
            // Heroic shield & crossed sword motif
            drawCircle(color = Color(0xFF16375E), radius = 55.dp.toPx(), center = Offset(w * 0.5f, h * 0.5f))
            drawCircle(color = Color(0xFFD4AF37), radius = 55.dp.toPx(), center = Offset(w * 0.5f, h * 0.5f), style = Stroke(width = 4f))
            drawCircle(color = Color(0xFF00A896), radius = 35.dp.toPx(), center = Offset(w * 0.5f, h * 0.5f))

            // Swords
            drawLine(
                color = Color(0xFFF6C845),
                start = Offset(w * 0.3f, h * 0.25f),
                end = Offset(w * 0.7f, h * 0.75f),
                strokeWidth = 6f
            )
            drawLine(
                color = Color(0xFFF6C845),
                start = Offset(w * 0.7f, h * 0.25f),
                end = Offset(w * 0.3f, h * 0.75f),
                strokeWidth = 6f
            )
        }
        "DAMAVAND" -> {
            // Mount Damavand towering peak with snowy apex
            val peak = Path().apply {
                moveTo(w * 0.1f, h)
                lineTo(w * 0.5f, h * 0.15f)
                lineTo(w * 0.9f, h)
                close()
            }
            drawPath(peak, color = Color(0xFF153354))

            // Snow cap
            val snow = Path().apply {
                moveTo(w * 0.42f, h * 0.32f)
                lineTo(w * 0.5f, h * 0.15f)
                lineTo(w * 0.58f, h * 0.32f)
                quadraticBezierTo(w * 0.5f, h * 0.36f, w * 0.42f, h * 0.32f)
                close()
            }
            drawPath(snow, color = Color(0xFFFAF6ED))
            // Radiant glow
            drawCircle(color = Color(0xFFD4AF37).copy(alpha = 0.5f), radius = 25.dp.toPx(), center = Offset(w * 0.5f, h * 0.15f))
        }
        else -> {
            // Illuminated Persian medallion
            drawCircle(color = Color(0xFF16375E), radius = 50.dp.toPx(), center = Offset(w * 0.5f, h * 0.5f))
            drawCircle(color = Color(0xFF00A896), radius = 40.dp.toPx(), center = Offset(w * 0.5f, h * 0.5f), style = Stroke(width = 5f))
            drawCircle(color = Color(0xFFF6C845), radius = 20.dp.toPx(), center = Offset(w * 0.5f, h * 0.5f))
        }
    }
}
