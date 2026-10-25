package view

import GameController
import androidx.compose.runtime.*

@Composable
fun MainGameScreen(controller: GameController) {
    val state by controller.state.collectAsState()
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Startup) }

    LaunchedEffect(state) {
        if (state != null) {
            currentScreen = Screen.Running
        }
    }

    when (currentScreen) {
        is Screen.Startup -> StartGameScreen {
            currentScreen = Screen.Setup
        }
        else -> println("not implemented yet")
    }
}