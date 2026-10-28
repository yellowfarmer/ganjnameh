package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.ParchmentBorder
import com.example.ui.theme.PersianAzure
import com.example.ui.theme.PersianAzureDark
import com.example.ui.theme.PersianAzureLight
import com.example.ui.theme.TurquoiseLight
import com.example.ui.theme.TurquoisePrimary

@Composable
fun WordGrid(
    targetWords: List<String>,
    foundWords: Set<String>,
    revealedHints: Map<Int, Char>, // index in stage words -> revealed char
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        targetWords.forEachIndexed { wordIndex, word ->
            val isFound = foundWords.contains(word)
            WordRow(
                word = word,
                isFound = isFound,
                revealedHint = revealedHints[wordIndex]
            )
        }
    }
}

@Composable
fun WordRow(
    word: String,
    isFound: Boolean,
    revealedHint: Char?
) {
    val scaleAnim by animateFloatAsState(
        targetValue = if (isFound) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "word_scale"
    )

    Row(
        modifier = Modifier
            .scale(scaleAnim)
            .testTag("word_row_${word}")
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        word.forEachIndexed { charIndex, char ->
            val showChar = isFound || (charIndex == 0 && revealedHint != null && revealedHint == char)
            LetterTile(
                char = char,
                isRevealed = showChar,
                isWordFound = isFound
            )
        }
    }
}

@Composable
fun LetterTile(
    char: Char,
    isRevealed: Boolean,
    isWordFound: Boolean
) {
    val bgBrush = when {
        isWordFound -> Brush.verticalGradient(listOf(GoldBright, GoldPrimary))
        isRevealed -> Brush.verticalGradient(listOf(TurquoiseLight, TurquoisePrimary))
        else -> Brush.verticalGradient(listOf(PersianAzureLight, PersianAzure))
    }

    val borderColor = when {
        isWordFound -> GoldBright
        isRevealed -> TurquoiseLight
        else -> ParchmentBorder.copy(alpha = 0.3f)
    }

    val textColor = when {
        isWordFound -> PersianAzureDark
        isRevealed -> Color.White
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .size(44.dp)
            .shadow(if (isWordFound) 4.dp else 2.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(bgBrush)
            .border(1.5.dp, borderColor, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (isRevealed) {
            Text(
                text = char.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = textColor
            )
        } else {
            // Subtle embossed dot in empty tile
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.White.copy(alpha = 0.2f))
            )
        }
    }
}
