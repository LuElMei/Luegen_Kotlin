package view

import GameController
import androidx.compose.runtime.*

@Composable
fun MainGameScreen(controller: GameController) {
    val screenState by controller.screenState.collectAsState()

    when (val state = screenState) {
        is Screen.Startup -> GameStartScreen {
            controller.onStartPressed()
        }

        is Screen.Setup -> GameSetupScreen(
            error = state.error,
        )  {
            controller.onStartGame(it)
        }

        is Screen.Running -> TODO()
    }

}