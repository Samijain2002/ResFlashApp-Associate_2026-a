package com.restest.flashcards.data.model

import java.util.UUID

data class FlashCard(
    val topic: String,
    val question: String,
    val answer: String,
    val id: String = UUID.randomUUID().toString()
)