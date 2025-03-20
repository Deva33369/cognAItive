package visual.camp.sample.app

import android.os.Bundle
import android.text.format.DateUtils.formatElapsedTime
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private var _memoryCardList = MutableStateFlow<List<MemoryCard>>(emptyList())
    val memoryCardList = _memoryCardList.asStateFlow()

    private var firstCard: MemoryCard? = null
    private var secondCard: MemoryCard? = null

    private var _elapsedTime = mutableLongStateOf(0)
    val elapsedTime = _elapsedTime

    private var _isPaused = mutableStateOf(false)
    val isPaused = _isPaused

    private var _isGameStarted = mutableStateOf(false)
    private var _elapsedJob: Job? = null

    fun init() {}

    private fun startTimer() {
        _elapsedJob?.cancel()
        _elapsedJob = viewModelScope.launch {
            while (!_isPaused.value) {
                delay(1000)
                if (!_isPaused.value) {
                    _elapsedTime.value += 1
                }
            }
        }
    }

    private fun generateCards(difficulty: String): List<MemoryCard> {
        val numCards = when (difficulty.lowercase()) {
            "easy" -> 12
            "normal" -> 16
            "hard" -> 20
            "insane" -> 28
            "1" -> 12
            "2" -> 16
            "3" -> 20
            "4" -> 28
            else -> 12
        }

        val numPairs = numCards / 2

        val cardList = mutableListOf<MemoryCard>()

        for (i in 0 until numPairs) {
            val id = i + 1

            val imageResId = data.MemoryCardList[i].imageResId

            cardList.add(MemoryCard(id = id, imageResId = imageResId, isFlipped = false))
            cardList.add(MemoryCard(id = id, imageResId = imageResId, isFlipped = false))
        }

        cardList.shuffle()

        return cardList
    }

    fun startNewGame(difficulty: String) {
        _memoryCardList.value = generateCards(difficulty)
        _isPaused.value = false
        _elapsedTime.longValue = 0
        startTimer()
    }
    fun pause() {
        _isPaused.value = true
    }
    fun resume() {
        _isPaused.value = false
        startTimer()
    }

    fun flipCard(card: MemoryCard) {
        Log.d("Flipcard","Attempting to flip card with ID: ${card.id}, currently flipped: ${card.isFlipped}")
        if (card.isFlipped) return

        val updatedList = _memoryCardList.value.map {
            if (it === card && !it.isFlipped) {
                it.copy(isFlipped = true)
            } else it
        }
        _memoryCardList.value = updatedList

        if (firstCard == null) {
            firstCard = card
        }
        else if (secondCard == null) {
            secondCard = card

            if (firstCard?.id == secondCard?.id) {
                firstCard = null
                secondCard = null
            } else {
                Log.d("Flipcard", "Cards don't match, will unflip after delay")
                viewModelScope.launch {
                    delay(1000)

                    val unflipList = _memoryCardList.value.map {
                        if ((it.id == firstCard?.id && it.isFlipped) || (it.id == secondCard?.id && it.isFlipped)) {
                            Log.d("Flipcard", "Unflipping card with ID: ${it.id}")
                            it.copy(isFlipped = false)
                        } else {
                            it
                        }
                    }
                    _memoryCardList.value = unflipList

                    Log.d("Flipcard", "firstCard after unflipping: ${firstCard?.id}, secondCard after unflipping: ${secondCard?.id}")
                    firstCard = null
                    secondCard = null
                    Log.d("Flipcard", "firstCard after unflipping: ${firstCard?.id}, secondCard after unflipping: ${secondCard?.id}")
                    Log.d("Flipcard", "firstCard and secondCard reset to null after unflipping")
                }
            }
        }
    }

    fun gameEnd() {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPairGameScreen(
    navController: NavController = rememberNavController(),
    viewModel: GameViewModel = viewModel()
) {
    val isPaused = viewModel.isPaused.value

    LaunchedEffect(Unit) {
        viewModel.startNewGame("easy")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Memory Card Game") },
                actions = {
                    IconButton(onClick = { viewModel.pause() }) {
                        Icon(Icons.Filled.Menu, "Pause Game")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            Column(modifier = Modifier.fillMaxSize()) {
                ElapsedTimeHeader(viewModel)

                MemoryCardGrid(viewModel, isPaused)
            }

            if (isPaused) {
                PauseOverlay(viewModel)
            }
        }
    }
}

@Composable
private fun ElapsedTimeHeader(viewModel: GameViewModel) {
    val elapsedTime by viewModel.elapsedTime
    Column() {
        Text(
            text = "Elapsed Time: ${formatElapsedTime(elapsedTime)}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun MemoryCardGrid(
    viewModel: GameViewModel,
    isPaused: Boolean
) {
    val memoryCardList by viewModel.memoryCardList.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .padding(32.dp)
    ) {
        items(memoryCardList) { card ->
            MemoryCardView(card = card, onCardClick = {
                if (!isPaused) {
                    viewModel.flipCard(it)
                }
            })
        }
    }
}

@Composable
private fun PauseOverlay(viewModel: GameViewModel) {
    val elapsedTime by viewModel.elapsedTime
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Game Paused",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = { viewModel.resume() }) {
                Text("Resume Game")
            }
            Button(onClick = { viewModel.startNewGame("easy") }) {
                Text("Restart Game")
            }
        }
    }
}

@Composable
fun MemoryCardView(card: MemoryCard, onCardClick: (MemoryCard) -> Unit) {
    val cardSize = 100.dp

    if (card.isFlipped) {
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