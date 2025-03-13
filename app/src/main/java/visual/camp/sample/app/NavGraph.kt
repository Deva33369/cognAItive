package visual.camp.sample.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

//Insert Route here
@Composable
fun GameGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "profile_graph"
    ) {
        navigation(startDestination = Screen.ProfileSelectionScreen.route, route = "profile_graph") {
            composable(route = Screen.ProfileSelectionScreen.route) { ProfileSelectionScreen(navController = navController) }
            composable(route = Screen.CreateProfileScreen.route) { CreateProfileScreen(navController = navController) }
        }
        navigation(startDestination = Screen.ProfileLandingScreen.route, route = "main_graph") {
            composable(route = Screen.ProfileLandingScreen.route) { ProfileLandingScreen(navController = navController) }
            composable(route = Screen.CardPairGameScreen.route) { CardPairGameScreen(navController = navController) }
            //composable(route = Screen.MazeGameScreen.route) { MazeGameScreen(navController = navController) }
            composable(route = Screen.GameResultScreen.route) { GameResultScreen(navController = navController) }

        }
    }
}

sealed class Screen(val route: String) {
    object ProfileSelectionScreen: Screen(route = "ProfileSelectionScreen")
    object CreateProfileScreen: Screen(route = "CreateProfileScreen")
    object ProfileLandingScreen: Screen(route = "ProfileLandingScreen")
    object CardPairGameScreen: Screen(route = "CardPairGameScreen")
    object MazeGameScreen: Screen(route = "MazeGameScreen")
    object GameResultScreen: Screen(route = "GameResultScreen")
}