package com.example.data.repository

import com.example.data.catalog.ContentCatalogProvider
import com.example.data.local.GanjnamehDao
import com.example.data.local.PlayerProfileEntity
import com.example.data.local.StageProgressEntity
import com.example.data.local.UnlockedCardEntity
import com.example.data.model.GameStage
import com.example.data.model.PlayerState
import com.example.data.model.RealmInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GanjnamehRepository(private val dao: GanjnamehDao) {

    val playerStateFlow: Flow<PlayerState> = combine(
        dao.getPlayerProfile(),
        dao.getAllUnlockedCards(),
        dao.getAllStageProgress()
    ) { profile, cards, progressList ->
        if (profile == null) {
            PlayerState()
        } else {
            val unlockedRealms = profile.unlockedRealmsString
                .split(",")
                .filter { it.isNotBlank() }
                .toSet()
                .ifEmpty { setOf("shahnameh") }

            val completedStages = progressList
                .filter { it.isCompleted }
                .map { it.stageNumber }
                .toSet()

            val cardIds = cards.map { it.cardId }.toSet()

            PlayerState(
                coins = profile.coins,
                stars = profile.stars,
                currentRealmId = profile.currentRealmId,
                currentStageIndex = profile.currentStageIndex,
                unlockedRealms = unlockedRealms,
                completedStages = completedStages,
                collectedCardIds = cardIds,
                soundEnabled = profile.soundEnabled,
                hapticEnabled = profile.hapticEnabled
            )
        }
    }

    suspend fun initializeIfEmpty() {
        // Ensure default player profile exists
        val defaultProfile = PlayerProfileEntity(
            id = 1,
            coins = 350,
            stars = 0,
            currentRealmId = "shahnameh",
            currentStageIndex = 1,
            unlockedRealmsString = "shahnameh",
            completedStagesString = "",
            soundEnabled = true,
            hapticEnabled = true
        )
        dao.savePlayerProfile(defaultProfile)
    }

    suspend fun addCoins(amount: Int, currentState: PlayerState) {
        val newCoins = (currentState.coins + amount).coerceAtLeast(0)
        dao.savePlayerProfile(
            PlayerProfileEntity(
                id = 1,
                coins = newCoins,
                stars = currentState.stars,
                currentRealmId = currentState.currentRealmId,
                currentStageIndex = currentState.currentStageIndex,
                unlockedRealmsString = currentState.unlockedRealms.joinToString(","),
                soundEnabled = currentState.soundEnabled,
                hapticEnabled = currentState.hapticEnabled
            )
        )
    }

    suspend fun spendCoins(amount: Int, currentState: PlayerState): Boolean {
        if (currentState.coins < amount) return false
        val newCoins = currentState.coins - amount
        dao.savePlayerProfile(
            PlayerProfileEntity(
                id = 1,
                coins = newCoins,
                stars = currentState.stars,
                currentRealmId = currentState.currentRealmId,
                currentStageIndex = currentState.currentStageIndex,
                unlockedRealmsString = currentState.unlockedRealms.joinToString(","),
                soundEnabled = currentState.soundEnabled,
                hapticEnabled = currentState.hapticEnabled
            )
        )
        return true
    }

    suspend fun completeStage(
        stageNumber: Int,
        earnedStars: Int,
        rewardCoins: Int,
        cardId: Int,
        realmId: String,
        currentState: PlayerState
    ) {
        val newStars = currentState.stars + earnedStars
        val newCoins = currentState.coins + rewardCoins
        val nextStageIndex = (stageNumber + 1).coerceAtMost(ContentCatalogProvider.stages.size)

        // Check if new realms should be unlocked based on new stars
        val newUnlockedRealms = currentState.unlockedRealms.toMutableSet()
        ContentCatalogProvider.realms.forEach { realm ->
            if (newStars >= realm.requiredStars) {
                newUnlockedRealms.add(realm.id)
            }
        }

        dao.saveStageProgress(
            StageProgressEntity(
                stageNumber = stageNumber,
                realmId = realmId,
                starsEarned = earnedStars,
                isCompleted = true,
                foundWordsString = ""
            )
        )

        dao.unlockCard(
            UnlockedCardEntity(
                cardId = cardId,
                realmId = realmId
            )
        )

        dao.savePlayerProfile(
            PlayerProfileEntity(
                id = 1,
                coins = newCoins,
                stars = newStars,
                currentRealmId = realmId,
                currentStageIndex = nextStageIndex,
                unlockedRealmsString = newUnlockedRealms.joinToString(","),
                soundEnabled = currentState.soundEnabled,
                hapticEnabled = currentState.hapticEnabled
            )
        )
    }

    suspend fun toggleSound(currentState: PlayerState) {
        dao.savePlayerProfile(
            PlayerProfileEntity(
                id = 1,
                coins = currentState.coins,
                stars = currentState.stars,
                currentRealmId = currentState.currentRealmId,
                currentStageIndex = currentState.currentStageIndex,
                unlockedRealmsString = currentState.unlockedRealms.joinToString(","),
                soundEnabled = !currentState.soundEnabled,
                hapticEnabled = currentState.hapticEnabled
            )
        )
    }

    suspend fun toggleHaptic(currentState: PlayerState) {
        dao.savePlayerProfile(
            PlayerProfileEntity(
                id = 1,
                coins = currentState.coins,
                stars = currentState.stars,
                currentRealmId = currentState.currentRealmId,
                currentStageIndex = currentState.currentStageIndex,
                unlockedRealmsString = currentState.unlockedRealms.joinToString(","),
                soundEnabled = currentState.soundEnabled,
                hapticEnabled = !currentState.hapticEnabled
            )
        )
    }

    fun getRealms(): List<RealmInfo> = ContentCatalogProvider.realms

    fun getStage(stageNumber: Int): GameStage? = ContentCatalogProvider.stages[stageNumber]
}
