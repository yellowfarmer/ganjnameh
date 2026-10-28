package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_profile")
data class PlayerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val coins: Int = 350,
    val stars: Int = 0,
    val currentRealmId: String = "shahnameh",
    val currentStageIndex: Int = 1,
    val unlockedRealmsString: String = "shahnameh",
    val completedStagesString: String = "",
    val soundEnabled: Boolean = true,
    val hapticEnabled: Boolean = true
)

@Entity(tableName = "unlocked_cards")
data class UnlockedCardEntity(
    @PrimaryKey val cardId: Int,
    val realmId: String,
    val unlockedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "stage_progress")
data class StageProgressEntity(
    @PrimaryKey val stageNumber: Int,
    val realmId: String,
    val starsEarned: Int,
    val isCompleted: Boolean,
    val foundWordsString: String
)
