package com.restest.flashcards.views.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.restest.flashcards.data.model.FlashCard
import com.restest.flashcards.views.navigation.Screen

@Composable
fun FlashCardList(
    viewModel: CardsViewModel,
    navController: NavController
) {
    val cards = viewModel.cardList.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val error = viewModel.error.collectAsState()
    val cardToDelete = remember { mutableStateOf<FlashCard?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading.value && cards.value.isEmpty() -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            error.value != null && cards.value.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(error.value ?: "Unable to load flashcards.")
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = viewModel::retryLoading) {
                        Text("Retry")
                    }
                }
            }

            cards.value.isEmpty() -> {
                Text(
                    text = "No flashcards yet. Add one to get started.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .padding(top = 48.dp, bottom = 80.dp)
                ) {
                    if (isLoading.value) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                    error.value?.let { message ->
                        item {
                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                Text(message)
                                Button(onClick = viewModel::retryLoading) {
                                    Text("Retry")
                                }
                            }
                        }
                    }
                    items(cards.value, key = { it.id }) { flashCard ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = flashCard.topic,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = flashCard.question,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 18.sp
                                    )
                                }
                                IconButton(
                                    onClick = { cardToDelete.value = flashCard }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete ${flashCard.topic}"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        IconButton(
            onClick = {
                navController.navigate(Screen.CreateCard.route)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Create")
        }

        if (cards.value.isNotEmpty()) {
            Button(
                onClick = {
                    navController.navigate(Screen.FlashCards.route)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Text("Start")
            }
        }
    }

    cardToDelete.value?.let { flashCard ->
        AlertDialog(
            onDismissRequest = { cardToDelete.value = null },
            title = { Text("Delete flashcard?") },
            text = { Text("Delete \"${flashCard.topic}\" from your collection?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCard(flashCard.id)
                        cardToDelete.value = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { cardToDelete.value = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun NavGraphBuilder.homeScreenRoute(navController: NavController) {
    composable(Screen.Home.route) {
        FlashCardList(viewModel = viewModel(), navController = navController)
    }
}
