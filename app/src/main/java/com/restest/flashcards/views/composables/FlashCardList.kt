package com.restest.flashcards.views.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.restest.flashcards.views.navigation.Screen


@Composable
fun FlashCardList(
    viewModel : CardsViewModel,
    navController: NavController
) {
    val cards = viewModel.cardList.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .padding(top = 32.dp)
        ) {
            items(cards.value) { flashCard ->

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
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
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
    }
}


fun NavGraphBuilder.homeScreenRoute(navController: NavController) {
    composable(Screen.Home.route) {
        FlashCardList(viewModel = viewModel(), navController = navController)
    }
}
