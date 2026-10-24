package com.restest.flashcards.data.repository

import com.restest.flashcards.data.model.FlashCard
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CardsLocalRepository(
    private val dataSource: FlashCardsDataSource = RestFlashCardsDataSource()
) {
    private val _cards = MutableStateFlow<List<FlashCard>>(emptyList())
    val cards = _cards.asStateFlow()

    private val addedCards = mutableListOf<FlashCard>()
    private val deletedCardIds = mutableSetOf<String>()
    private val sessionLock = Any()
    private val loadMutex = Mutex()
    private var hasLoadedCards = false

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    suspend fun loadCards() {
        loadMutex.withLock {
            if (!hasLoadedCards) {
                _isLoading.value = true
                _error.value = null
                try {
                    val apiCards = dataSource.fetchCards()
                    synchronized(sessionLock) {
                        _cards.value = apiCards.filterNot { it.id in deletedCardIds } + addedCards
                        hasLoadedCards = true
                    }
                } catch (exception: IOException) {
                    _error.value = exception.message ?: "Unable to load flashcards. Please try again."
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun addCard(newCard: FlashCard) {
        synchronized(sessionLock) {
            addedCards += newCard
            _cards.value = _cards.value + newCard
        }
    }

    fun deleteCard(cardId: String) {
        synchronized(sessionLock) {
            deletedCardIds += cardId
            addedCards.removeAll { it.id == cardId }
            _cards.value = _cards.value.filterNot { it.id == cardId }
        }
    }

    companion object {
        val instance: CardsLocalRepository by lazy { CardsLocalRepository() }
    }
}