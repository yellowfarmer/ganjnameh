package com.example.data.model

data class RealmInfo(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val requiredStars: Int,
    val iconSymbol: String,
    val accentColorHex: Long,
    val stagesCount: Int
)

data class KnowledgeCard(
    val id: Int,
    val title: String,
    val realmTitle: String,
    val summary: String,
    val wisdomQuote: String,
    val era: String,
    val iconSymbol: String
)

data class VisualRiddle(
    val id: Int,
    val title: String,
    val clueText: String,
    val answerWord: String,
    val visualType: String, // e.g. "SIMURGH", "ROSTAM", "DAMAVAND", "FERDOWSI"
    val scrambledLetters: List<Char>
)

data class QuizOption(
    val text: String,
    val isCorrect: Boolean
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<QuizOption>,
    val explanation: String
)

data class GameStage(
    val stageNumber: Int,
    val realmId: String,
    val stageTitle: String,
    val targetWords: List<String>, // Target words to fill in the grid
    val bonusWords: List<String>,  // Extra valid words for coin rewards
    val letters: List<Char>,       // Letters on the wheel
    val knowledgeCard: KnowledgeCard,
    val visualRiddle: VisualRiddle,
    val quizQuestions: List<QuizQuestion>,
    val rewardCoins: Int = 50
)

data class PlayerState(
    val coins: Int = 350,
    val stars: Int = 0,
    val currentRealmId: String = "shahnameh",
    val currentStageIndex: Int = 1,
    val unlockedRealms: Set<String> = setOf("shahnameh"),
    val completedStages: Set<Int> = emptySet(),
    val collectedCardIds: Set<Int> = emptySet(),
    val bonusWordsFound: Set<String> = emptySet(),
    val soundEnabled: Boolean = true,
    val hapticEnabled: Boolean = true
)

enum class GameScreen {
    REALM_MAP,
    STAGE_SELECT,
    WORD_WORKSHOP,
    KNOWLEDGE_CARD,
    VISUAL_RIDDLE,
    KNOWLEDGE_QUIZ,
    WISDOM_LIBRARY,
    DAILY_CHALLENGE
}
