package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.GameScreen
import com.example.ui.GanjnamehViewModel
import com.example.ui.components.PersianHeader
import com.example.ui.screens.DailyChallengeScreen
import com.example.ui.screens.KnowledgeCardDialog
import com.example.ui.screens.KnowledgeQuizScreen
import com.example.ui.screens.RealmMapScreen
import com.example.ui.screens.StageCompleteModal
import com.example.ui.screens.VisualRiddleScreen
import com.example.ui.screens.WisdomLibraryScreen
import com.example.ui.screens.WordWorkshopScreen
import com.example.ui.theme.GanjnamehTheme
import com.example.ui.theme.PersianAzureDark

class MainActivity : ComponentActivity() {

    private val viewModel: GanjnamehViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GanjnamehTheme {
                // Persian RTL Direction
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    GanjnamehApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun GanjnamehApp(viewModel: GanjnamehViewModel) {
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentScreen = uiState.currentScreen

    // Handle Back Press across screens
    BackHandler(enabled = currentScreen != GameScreen.REALM_MAP) {
        when (currentScreen) {
            GameScreen.REALM_MAP -> { /* Default exit */ }
            GameScreen.WORD_WORKSHOP,
            GameScreen.VISUAL_RIDDLE,
            GameScreen.KNOWLEDGE_QUIZ,
            GameScreen.WISDOM_LIBRARY,
            GameScreen.DAILY_CHALLENGE -> {
                viewModel.navigateToScreen(GameScreen.REALM_MAP)
            }
            else -> viewModel.navigateToScreen(GameScreen.REALM_MAP)
        }
    }

    val headerTitle = when (currentScreen) {
        GameScreen.REALM_MAP -> "گنجنامه"
        GameScreen.WORD_WORKSHOP -> uiState.activeStage.stageTitle
        GameScreen.VISUAL_RIDDLE -> "معمای تصویری"
        GameScreen.KNOWLEDGE_QUIZ -> "میدان دانایی"
        GameScreen.WISDOM_LIBRARY -> "کتابخانه دانایی"
        GameScreen.DAILY_CHALLENGE -> "چالش روزانه"
        else -> "گنجنامه"
    }

    val headerSubtitle = when (currentScreen) {
        GameScreen.REALM_MAP -> "جهان بازی‌های کلمات و دانایی ایرانی"
        GameScreen.WORD_WORKSHOP -> "قلمرو ${uiState.activeRealm.title} • مرحله ${uiState.activeStage.stageNumber}"
        GameScreen.VISUAL_RIDDLE -> uiState.activeStage.stageTitle
        GameScreen.KNOWLEDGE_QUIZ -> "پرسش‌های شاهنامه و حکمت"
        GameScreen.WISDOM_LIBRARY -> "گنجینه مفاخر و سخنان نغز"
        GameScreen.DAILY_CHALLENGE -> "ضرب‌المثل و واژه روز"
        else -> null
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(PersianAzureDark),
        topBar = {
            PersianHeader(
                title = headerTitle,
                subtitle = headerSubtitle,
                coins = playerState.coins,
                stars = playerState.stars,
                showBackButton = currentScreen != GameScreen.REALM_MAP,
                onBackClick = { viewModel.navigateToScreen(GameScreen.REALM_MAP) },
                soundEnabled = playerState.soundEnabled,
                onToggleSound = { viewModel.toggleSound() },
                hapticEnabled = playerState.hapticEnabled,
                onToggleHaptic = { viewModel.toggleHaptic() },
                onCoinsClick = { viewModel.addCoins(50) } // Quick coin booster for fun testing
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding(),
            color = PersianAzureDark
        ) {
            when (currentScreen) {
                GameScreen.REALM_MAP -> {
                    RealmMapScreen(
                        realms = viewModel.getRealms(),
                        playerState = playerState,
                        onSelectRealm = { realm -> viewModel.selectRealm(realm) },
                        onOpenLibrary = { viewModel.navigateToScreen(GameScreen.WISDOM_LIBRARY) },
                        onOpenDailyChallenge = { viewModel.navigateToScreen(GameScreen.DAILY_CHALLENGE) }
                    )
                }

                GameScreen.WORD_WORKSHOP -> {
                    WordWorkshopScreen(
                        stage = uiState.activeStage,
                        coins = playerState.coins,
                        hapticEnabled = playerState.hapticEnabled,
                        onSpendCoins = { amount -> viewModel.spendCoins(amount) },
                        onAddCoins = { amount -> viewModel.addCoins(amount) },
                        onAllWordsFound = { viewModel.onWordWorkshopComplete() }
                    )
                }

                GameScreen.VISUAL_RIDDLE -> {
                    VisualRiddleScreen(
                        riddle = uiState.activeStage.visualRiddle,
                        onProceedToQuiz = { viewModel.onVisualRiddleComplete() },
                        onHintUsed = { viewModel.spendCoins(25) },
                        coins = playerState.coins,
                        hapticEnabled = playerState.hapticEnabled
                    )
                }

                GameScreen.KNOWLEDGE_QUIZ -> {
                    KnowledgeQuizScreen(
                        questions = uiState.activeStage.quizQuestions,
                        onQuizCompleted = { earnedStars, correctCount ->
                            viewModel.onKnowledgeQuizComplete(earnedStars, correctCount)
                        },
                        hapticEnabled = playerState.hapticEnabled
                    )
                }

                GameScreen.WISDOM_LIBRARY -> {
                    WisdomLibraryScreen(
                        unlockedCardIds = playerState.collectedCardIds
                    )
                }

                GameScreen.DAILY_CHALLENGE -> {
                    DailyChallengeScreen(
                        onRewardClaimed = { coins -> viewModel.addCoins(coins) },
                        hapticEnabled = playerState.hapticEnabled
                    )
                }

                else -> {
                    RealmMapScreen(
                        realms = viewModel.getRealms(),
                        playerState = playerState,
                        onSelectRealm = { realm -> viewModel.selectRealm(realm) },
                        onOpenLibrary = { viewModel.navigateToScreen(GameScreen.WISDOM_LIBRARY) },
                        onOpenDailyChallenge = { viewModel.navigateToScreen(GameScreen.DAILY_CHALLENGE) }
                    )
                }
            }

            // Knowledge Card Revelation Dialog
            if (uiState.showKnowledgeCardDialog) {
                KnowledgeCardDialog(
                    card = uiState.activeStage.knowledgeCard,
                    onProceedToNextStep = { viewModel.onKnowledgeCardProceed() }
                )
            }

            // Stage Completion & Celebration Modal
            if (uiState.stageCompleteModalVisible) {
                StageCompleteModal(
                    stageNumber = uiState.activeStage.stageNumber,
                    stageTitle = uiState.activeStage.stageTitle,
                    starsEarned = uiState.lastEarnedStars,
                    rewardCoins = uiState.lastEarnedCoins,
                    onNextStage = { viewModel.onNextStageClicked() },
                    onReturnToMap = { viewModel.navigateToScreen(GameScreen.REALM_MAP) }
                )
            }
        }
    }
}
