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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.PersianAzureSurface
import com.example.ui.theme.TurquoiseDark
import com.example.ui.theme.TurquoiseLight
import com.example.ui.theme.TurquoisePrimary

@Composable
fun DailyChallengeScreen(
    onRewardClaimed: (Int) -> Unit,
    hapticEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val targetWord = "دانا"
    val proverb = "توانا بود هر که ... بود / ز دانش دل پیر برنا بود"

    val letters = listOf('د', 'ا', 'ن', 'ا', 'ب', 'و')
    val placed = remember { mutableStateListOf<Pair<Char, Int>?>().apply { repeat(4) { add(null) } } }
    val usedIndices = remember { mutableStateListOf<Int>() }

    var isSolved by remember { mutableStateOf(false) }
    var isClaimed by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Daily Badge
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(GoldPrimary.copy(alpha = 0.2f))
                .border(1.dp, GoldBright, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Today, contentDescription = null, tint = GoldBright, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "چالش روزانه گنجنامه • ضرب‌المثل و واژه روز",
                color = GoldBright,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Proverb Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(18.dp))
                .border(1.5.dp, GoldPrimary, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = ParchmentBg)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "واژه گمشده در بیت فردوسی را بیابید:",
                    style = MaterialTheme.typography.bodySmall,
                    color = PersianAzure,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "« $proverb »",
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = PersianAzureDark,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Placed Word Slots
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until 4) {
                val item = placed[i]
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSolved) Brush.verticalGradient(listOf(GoldBright, GoldDark))
                            else if (item != null) Brush.verticalGradient(listOf(TurquoiseLight, TurquoiseDark))
                            else Brush.verticalGradient(listOf(PersianAzureLight, PersianAzure))
                        )
                        .border(1.5.dp, if (isSolved) GoldBright else TurquoisePrimary, RoundedCornerShape(12.dp))
                        .clickable(enabled = item != null && !isSolved) {
                            if (item != null) {
                                usedIndices.remove(item.second)
                                placed[i] = null
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item?.first?.toString() ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = if (isSolved) PersianAzureDark else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Scrambled letters
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            letters.forEachIndexed { idx, char ->
                val isUsed = usedIndices.contains(idx)
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isUsed) Brush.verticalGradient(listOf(PersianAzureLight.copy(alpha = 0.3f), PersianAzureLight.copy(alpha = 0.3f)))
                            else Brush.verticalGradient(listOf(PersianAzureLight, PersianAzure))
                        )
                        .border(1.dp, if (isUsed) Color.Transparent else GoldPrimary, RoundedCornerShape(10.dp))
                        .clickable(enabled = !isUsed && !isSolved) {
                            val emptyIdx = placed.indexOfFirst { it == null }
                            if (emptyIdx != -1) {
                                placed[emptyIdx] = Pair(char, idx)
                                usedIndices.add(idx)
                                if (hapticEnabled) {
                                    view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                                }

                                val currentWord = placed.map { it?.first ?: ' ' }.joinToString("").trim()
                                if (currentWord == targetWord) {
                                    isSolved = true
                                    if (hapticEnabled) {
                                        view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                                    }
                                }
                            }
                        },
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
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Success state
        AnimatedVisibility(visible = isSolved) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(EmeraldGreen.copy(alpha = 0.25f))
                        .border(1.dp, EmeraldGreen, RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تبریک! چالش روزانه با موفقیت حل شد",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (!isClaimed) {
                            isClaimed = true
                            onRewardClaimed(100)
                        }
                    },
                    enabled = !isClaimed,
                    modifier = Modifier
                        .testTag("claim_daily_reward_button")
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = PersianAzureDark
                    )
                ) {
                    Text(
                        text = if (!isClaimed) "دریافت ۱۰۰ 🪙 سکه پاداش روزانه" else "پاداش دریافت شد ✓",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
