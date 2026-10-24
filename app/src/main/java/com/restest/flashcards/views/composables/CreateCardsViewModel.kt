package com.restest.flashcards.views.composables

import androidx.lifecycle.ViewModel
import com.restest.flashcards.data.model.FlashCard
import com.restest.flashcards.data.repository.CardsLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateCardsViewModel(
    private val repository: CardsLocalRepository = CardsLocalRepository.instance
) : ViewModel() {

    private val _events: MutableStateFlow<Event> = MutableStateFlow(Event.Idle)
    val events = _events.asStateFlow()

    fun addCard(topic: String, question: String, answer: String) {
        if (topic.isBlank() || question.isBlank() || answer.isBlank()) {
            return
        }
        repository.addCard(
            FlashCard(
                topic = topic.trim(),
                question = question.trim(),
                answer = answer.trim()
            )
        )
        _events.value = Event.CardAdded("New card added")
    }

    sealed class Event {
        data class CardAdded(val message: String) : Event()
        data object Idle : Event()
    }
}