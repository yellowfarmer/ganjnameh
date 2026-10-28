package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PersianAzure
import com.example.ui.theme.PersianAzureDark
import com.example.ui.theme.PersianAzureLight
import com.example.ui.theme.TurquoiseLight
import com.example.ui.theme.TurquoisePrimary
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun WordWheel(
    letters: List<Char>,
    onWordCompleted: (String) -> Unit,
    onShuffle: () -> Unit,
    onHintClick: () -> Unit,
    coins: Int,
    hapticEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()
    val rotationAnim = remember { Animatable(0f) }

    val selectedIndices = remember { mutableStateListOf<Int>() }
    var currentTouchPos by remember { mutableStateOf<Offset?>(null) }
    var currentWord by remember { mutableStateOf("") }

    val letterPositions = remember(letters.size) { mutableListOf<Offset>() }

    BoxWithConstraints(
        modifier = modifier
            .size(280.dp)
            .testTag("word_wheel_container"),
        contentAlignment = Alignment.Center
    ) {
        val density = LocalDensity.current
        val wheelRadiusPx = with(density) { 100.dp.toPx() }
        val centerPx = with(density) { 140.dp.toPx() }
        val letterRadiusPx = with(density) { 26.dp.toPx() }

        // Compute letter node positions in circle
        LaunchedEffect(letters.size, centerPx, wheelRadiusPx) {
            letterPositions.clear()
            val count = letters.size
            val startAngle = -Math.PI / 2.0 // Start from 12 o'clock
            for (i in 0 until count) {
                val angle = startAngle + (2 * Math.PI * i / count)
                val x = (centerPx + wheelRadiusPx * cos(angle)).toFloat()
                val y = (centerPx + wheelRadiusPx * sin(angle)).toFloat()
                letterPositions.add(Offset(x, y))
            }
        }

        // Circular background disc
        Box(
            modifier = Modifier
                .size(270.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(PersianAzureLight, PersianAzure, PersianAzureDark)
                    )
                )
                .border(2.5.dp, GoldPrimary.copy(alpha = 0.6f), CircleShape)
                .shadow(8.dp, CircleShape)
        )

        // Canvas for drawing connection strokes
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(letters) {
                    detectDragGestures(
                        onDragStart = { startOffset ->
                            selectedIndices.clear()
                            val hit = findHitIndex(startOffset, letterPositions, letterRadiusPx * 1.3f)
                            if (hit != -1) {
                                selectedIndices.add(hit)
                                currentWord = letters[hit].toString()
                                if (hapticEnabled) {
                                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                                }
                            }
                            currentTouchPos = startOffset
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            val pos = change.position
                            currentTouchPos = pos

                            val hit = findHitIndex(pos, letterPositions, letterRadiusPx * 1.3f)
                            if (hit != -1) {
                                val alreadyIdx = selectedIndices.indexOf(hit)
                                if (alreadyIdx == -1) {
                                    // New letter added
                                    selectedIndices.add(hit)
                                    currentWord = selectedIndices.map { letters[it] }.joinToString("")
                                    if (hapticEnabled) {
                                        view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                                    }
                                } else if (alreadyIdx == selectedIndices.size - 2) {
                                    // User backtracked to previous letter: pop last
                                    selectedIndices.removeAt(selectedIndices.lastIndex)
                                    currentWord = selectedIndices.map { letters[it] }.joinToString("")
                                    if (hapticEnabled) {
                                        view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                                    }
                                }
                            }
                        },
                        onDragEnd = {
                            if (currentWord.length >= 2) {
                                onWordCompleted(currentWord)
                            }
                            selectedIndices.clear()
                            currentWord = ""
                            currentTouchPos = null
                        },
                        onDragCancel = {
                            selectedIndices.clear()
                            currentWord = ""
                            currentTouchPos = null
                        }
                    )
                }
        ) {
            // Draw connecting path between selected letters
            if (selectedIndices.isNotEmpty()) {
                val path = Path()
                val first = letterPositions[selectedIndices[0]]
                path.moveTo(first.x, first.y)

                for (i in 1 until selectedIndices.size) {
                    val pt = letterPositions[selectedIndices[i]]
                    path.lineTo(pt.x, pt.y)
                }

                // Draw path to current finger position
                currentTouchPos?.let { touch ->
                    path.lineTo(touch.x, touch.y)
                }

                // Outer glow stroke
                drawPath(
                    path = path,
                    color = TurquoiseLight.copy(alpha = 0.4f),
                    style = Stroke(width = 24f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // Inner bright stroke
                drawPath(
                    path = path,
                    color = TurquoiseLight,
                    style = Stroke(width = 10f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }
        }

        // Letter Nodes rendered on circle
        letters.forEachIndexed { index, letter ->
            if (index < letterPositions.size) {
                val pos = letterPositions[index]
                val isSelected = selectedIndices.contains(index)

                val nodeBg = when {
                    isSelected -> Brush.radialGradient(listOf(GoldBright, GoldDark))
                    else -> Brush.radialGradient(listOf(PersianAzureLight, PersianAzure))
                }

                val nodeBorder = if (isSelected) GoldBright else TurquoisePrimary.copy(alpha = 0.8f)
                val textColor = if (isSelected) PersianAzureDark else Color.White

                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                (pos.x - letterRadiusPx).toInt(),
                                (pos.y - letterRadiusPx).toInt()
                            )
                        }
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(nodeBg)
                        .border(2.dp, nodeBorder, CircleShape)
                        .shadow(if (isSelected) 6.dp else 2.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = letter.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = textColor
                    )
                }
            }
        }

        // Center Action Pill / Word Preview
        if (currentWord.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.horizontalGradient(listOf(GoldDark, GoldPrimary)))
                    .border(1.5.dp, GoldBright, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .shadow(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentWord,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = PersianAzureDark
                )
            }
        } else {
            // Shuffle Button in the center
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        rotationAnim.animateTo(
                            targetValue = rotationAnim.value + 360f,
                            animationSpec = tween(durationMillis = 350, easing = LinearEasing)
                        )
                    }
                    onShuffle()
                },
                modifier = Modifier
                    .testTag("shuffle_letters_button")
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(PersianAzure)
                    .border(1.5.dp, GoldPrimary, CircleShape)
                    .rotate(rotationAnim.value)
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "درهم‌ریزی حروف",
                    tint = GoldBright,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Hint Button (Top-Right / Corner)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 4.dp, bottom = 4.dp)
        ) {
            Surface(
                modifier = Modifier
                    .testTag("hint_button")
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, GoldBright.copy(alpha = 0.7f), RoundedCornerShape(18.dp))
                    .clickable { onHintClick() },
                color = PersianAzureDark.copy(alpha = 0.9f)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💡 ۳۰ 🪙",
                        color = GoldBright,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun findHitIndex(pos: Offset, centers: List<Offset>, radius: Float): Int {
    for (i in centers.indices) {
        val center = centers[i]
        val dx = pos.x - center.x
        val dy = pos.y - center.y
        if (sqrt((dx * dx + dy * dy).toDouble()) <= radius) {
            return i
        }
    }
    return -1
}
