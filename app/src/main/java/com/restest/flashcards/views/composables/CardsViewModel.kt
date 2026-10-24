package com.restest.flashcards.views.composables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restest.flashcards.data.repository.CardsLocalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardsViewModel(
    private val repository: CardsLocalRepository = CardsLocalRepository.instance,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) :
    ViewModel() {
    val cardList = repository.cards
    val isLoading = repository.isLoading
    val error = repository.error

    init {
        loadCards()
    }

    fun retryLoading() {
        loadCards()
    }

    fun deleteCard(cardId: String) {
        repository.deleteCard(cardId)
    }

    private fun loadCards() {
        viewModelScope.launch(dispatcher) {
            repository.loadCards()
        }
    }
}