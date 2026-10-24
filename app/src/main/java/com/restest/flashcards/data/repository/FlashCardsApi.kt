package com.restest.flashcards.data.repository

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.restest.flashcards.data.model.FlashCard
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun interface FlashCardsDataSource {
    suspend fun fetchCards(): List<FlashCard>
}

class RestFlashCardsDataSource(
    private val gson: Gson = Gson()
) : FlashCardsDataSource {

    override suspend fun fetchCards(): List<FlashCard> = withContext(Dispatchers.IO) {
        val connection = URL(FLASHCARDS_URL).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.connectTimeout = 15_000
            connection.readTimeout = 15_000

            if (connection.responseCode !in 200..299) {
                throw IOException("Flashcard request failed with HTTP ${connection.responseCode}")
            }

            val response = connection.inputStream.bufferedReader().use { it.readText() }
            parseFlashCards(response, gson)
        } finally {
            connection.disconnect()
        }
    }

    private companion object {
        const val FLASHCARDS_URL = "https://resflash.free.beeceptor.com/v1/flashcards"
    }
}

internal fun parseFlashCards(
    response: String,
    gson: Gson = Gson()
): List<FlashCard> {
    val flashcards = try {
        gson.fromJson(response, FlashCardsResponse::class.java)
            ?.flashcards
            ?: throw IOException("Flashcard response did not contain a flashcards list")
    } catch (exception: JsonParseException) {
        throw IOException("Flashcard response was not valid JSON", exception)
    }

    return flashcards.map { card ->
        val id = card.id?.takeIf(String::isNotBlank)
            ?: throw IOException("Flashcard response contained a card without an ID")
        val slug = card.slug?.takeIf(String::isNotBlank)
            ?: throw IOException("Flashcard response contained a card without a slug")
        val question = card.question?.takeIf(String::isNotBlank)
            ?: throw IOException("Flashcard response contained a card without a question")
        val answer = card.answer?.takeIf(String::isNotBlank)
            ?: throw IOException("Flashcard response contained a card without an answer")

        FlashCard(
            topic = slug.split('-').joinToString(" ") { word ->
                word.replaceFirstChar(Char::uppercase)
            },
            question = question,
            answer = answer,
            id = id
        )
    }
}

private data class FlashCardsResponse(
    val flashcards: List<FlashCardResponse>?
)

private data class FlashCardResponse(
    val id: String?,
    val slug: String?,
    val question: String?,
    val answer: String?
)
