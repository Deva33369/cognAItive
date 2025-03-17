package visual.camp.sample.app.screen

import android.content.Intent
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import visual.camp.sample.app.Screen
import visual.camp.sample.app.activity.MazeGameScreen
import visual.camp.sample.app.customUI.CustomTextHeader
import visual.camp.sample.app.customUI.NormalBlueButton
import visual.camp.sample.app.theme.sky_blue

@Composable
fun HomeScreen(
    navController: NavController = rememberNavController()
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .background(sky_blue)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(0.6f)
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                CustomTextHeader(
                    text = "CognAltive",
                    fontSize = 64.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxSize()
            ) {
                NormalBlueButton(
                    text = "Play",
                    onClick = {
                        navController.navigate(Screen.ProfileSelectionScreen.route)
                    }
                )

                val context = LocalContext.current  // Get current context
                Button(onClick = {
                    val intent = Intent(context, MazeGameScreen::class.java)
                    context.startActivity(intent)  // Start MazeGameScreen Activity
                }) {
                    Text("Go to Maze Game")
                }
            }
        }
    }
}