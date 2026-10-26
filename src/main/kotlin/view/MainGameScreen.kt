package view

import GameController
import androidx.compose.runtime.*
import view.running.GameRunningScreen

@Composable
fun MainGameScreen(controller: GameController) {
    val screenState by controller.screenState.collectAsState()

    when (val screen = screenState) {
        is Screen.Startup -> GameStartScreen {
            controller.onStartPressed()
        }

        is Screen.Setup -> GameSetupScreen(
            error = screen.error,
        )  {
            controller.onStartGame(it)
        }

        is Screen.Running -> GameRunningScreen(
            state = screen.state,
            error = screen.error
        )
    }

}