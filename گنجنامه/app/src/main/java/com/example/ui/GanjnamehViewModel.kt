package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.catalog.ContentCatalogProvider
import com.example.data.local.GanjnamehDatabase
import com.example.data.model.GameScreen
import com.example.data.model.GameStage
import com.example.data.model.PlayerState
import com.example.data.model.RealmInfo
import com.example.data.repository.GanjnamehRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GanjnamehUiState(
    val currentScreen: GameScreen = GameScreen.REALM_MAP,
    val activeRealm: RealmInfo = ContentCatalogProvider.realms.first(),
    val activeStage: GameStage = ContentCatalogProvider.stages[1]!!,
    val stageCompleteModalVisible: Boolean = false,
    val lastEarnedStars: Int = 3,
    val lastEarnedCoins: Int = 50,
    val showKnowledgeCardDialog: Boolean = false
)

class GanjnamehViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GanjnamehRepository

    val playerState: StateFlow<PlayerState>

    private val _uiState = MutableStateFlow(GanjnamehUiState())
    val uiState: StateFlow<GanjnamehUiState> = _uiState.asStateFlow()

    init {
        val database = GanjnamehDatabase.getInstance(application)
        repository = GanjnamehRepository(database.dao())

        playerState = repository.playerStateFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlayerState()
        )

        viewModelScope.launch {
            repository.initializeIfEmpty()
        }
    }

    fun selectRealm(realm: RealmInfo) {
        val stageNum = when (realm.id) {
            "shahnameh" -> playerState.value.currentStageIndex.coerceIn(1, 5)
            else -> 1
        }
        val stage = ContentCatalogProvider.stages[stageNum] ?: ContentCatalogProvider.stages[1]!!
        _uiState.update {
            it.copy(
                activeRealm = realm,
                activeStage = stage,
                currentScreen = GameScreen.WORD_WORKSHOP
            )
        }
    }

    fun startStage(stageNumber: Int) {
        val stage = ContentCatalogProvider.stages[stageNumber] ?: return
        _uiState.update {
            it.copy(
                activeStage = stage,
                currentScreen = GameScreen.WORD_WORKSHOP
            )
        }
    }

    fun onWordWorkshopComplete() {
        // Words discovered -> Open Knowledge Card Dialog!
        _uiState.update {
            it.copy(showKnowledgeCardDialog = true)
        }
    }

    fun onKnowledgeCardProceed() {
        // Knowledge Card closed -> Proceed to Visual Riddle!
        _uiState.update {
            it.copy(
                showKnowledgeCardDialog = false,
                currentScreen = GameScreen.VISUAL_RIDDLE
            )
        }
    }

    fun onVisualRiddleComplete() {
        // Visual Riddle solved -> Add 20 coins & Proceed to Knowledge Quiz!
        viewModelScope.launch {
            repository.addCoins(20, playerState.value)
        }
        _uiState.update {
            it.copy(currentScreen = GameScreen.KNOWLEDGE_QUIZ)
        }
    }

    fun onKnowledgeQuizComplete(earnedStars: Int, correctCount: Int) {
        val stage = _uiState.value.activeStage
        viewModelScope.launch {
            repository.completeStage(
                stageNumber = stage.stageNumber,
                earnedStars = earnedStars,
                rewardCoins = stage.rewardCoins,
                cardId = stage.knowledgeCard.id,
                realmId = stage.realmId,
                currentState = playerState.value
            )
        }
        _uiState.update {
            it.copy(
                lastEarnedStars = earnedStars,
                lastEarnedCoins = stage.rewardCoins,
                stageCompleteModalVisible = true
            )
        }
    }

    fun onNextStageClicked() {
        _uiState.update { it.copy(stageCompleteModalVisible = false) }
        val nextStageNum = _uiState.value.activeStage.stageNumber + 1
        val nextStage = ContentCatalogProvider.stages[nextStageNum]
        if (nextStage != null) {
            _uiState.update {
                it.copy(
                    activeStage = nextStage,
                    currentScreen = GameScreen.WORD_WORKSHOP
                )
            }
        } else {
            // Finished all current realm stages! Return to realm map
            _uiState.update {
                it.copy(currentScreen = GameScreen.REALM_MAP)
            }
        }
    }

    fun navigateToScreen(screen: GameScreen) {
        _uiState.update {
            it.copy(
                currentScreen = screen,
                stageCompleteModalVisible = false,
                showKnowledgeCardDialog = false
            )
        }
    }

    fun spendCoins(amount: Int): Boolean {
        var success = false
        if (playerState.value.coins >= amount) {
            viewModelScope.launch {
                repository.spendCoins(amount, playerState.value)
            }
            success = true
        }
        return success
    }

    fun addCoins(amount: Int) {
        viewModelScope.launch {
            repository.addCoins(amount, playerState.value)
        }
    }

    fun toggleSound() {
        viewModelScope.launch {
            repository.toggleSound(playerState.value)
        }
    }

    fun toggleHaptic() {
        viewModelScope.launch {
            repository.toggleHaptic(playerState.value)
        }
    }

    fun getRealms(): List<RealmInfo> = repository.getRealms()
}
