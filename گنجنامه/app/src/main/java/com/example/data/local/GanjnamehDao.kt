package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GanjnamehDao {
    @Query("SELECT * FROM player_profile WHERE id = 1")
    fun getPlayerProfile(): Flow<PlayerProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlayerProfile(profile: PlayerProfileEntity)

    @Query("SELECT * FROM unlocked_cards")
    fun getAllUnlockedCards(): Flow<List<UnlockedCardEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockCard(card: UnlockedCardEntity)

    @Query("SELECT * FROM stage_progress")
    fun getAllStageProgress(): Flow<List<StageProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStageProgress(progress: StageProgressEntity)
}
