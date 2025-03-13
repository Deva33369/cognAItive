package com.example.cognaitive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.example.cognaitive.ui.theme.CognAItiveTheme
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewmodel.compose.viewModel

class GameViewModel : ViewModel() {
    private var _memoryCardList = MutableStateFlow(data.MemoryCardList.toList())
    val memoryCardList = _memoryCardList.asStateFlow()

    private var firstCard: MemoryCard? = null
    private var secondCard: MemoryCard? = null

    private var _elapsedTime = mutableIntStateOf(0)
    val elapsedTime = _elapsedTime

    private var _isPaused = mutableStateOf(false)
    val isPaused = _isPaused

    fun init() {}
    fun startNewGame() {}
    fun pause() {
        _isPaused.value = true
    }
    fun resume() {
        _isPaused.value = false
    }

    fun flipCard(card: MemoryCard) {
        if (firstCard == null) {
            firstCard = card
        }
        if (secondCard == null) {
            secondCard = card
        }

    }

    fun gameEnd() {}
}

@Composable
fun CardPairGameScreen(
    navController: NavController = rememberNavController(),
    viewModel: GameViewModel = viewModel()
)
{
    // Code Body
}

@Composable
fun memoryCardView(card: MemoryCard, onCardClick: (MemoryCard) -> Unit) {
    val cardSize = 100.dp

    val cardState = if (card.isFlipped) {
        Image(
            painter = painterResource(id = card.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clickable { onCardClick(card) }
        )
    } else {
        Box(
            modifier = Modifier
                .size(cardSize)
                .clickable { onCardClick(card) }
                .background(Color.Gray)
        )
    }
}