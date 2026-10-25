import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import view.MainGameScreen

fun main() = application {
    val viewController = remember { GameController() }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Luegen - Das Kartenspiel",
        state = rememberWindowState(width = 800.dp, height = 700.dp),
    ) {
        MaterialTheme {
            MainGameScreen(viewController)
        }
    }
}