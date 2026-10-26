import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import view.Screen


class GameController {
    private val _screenState = MutableStateFlow<Screen>(Screen.Startup)
    val screenState = _screenState.asStateFlow()

    fun onStartGame(names: List<String>) {
        val outcome = GameState.initial(names)
        when (outcome) {
            is Outcome.Ok -> _screenState.value = Screen.Running(
                state = outcome.state,
                error = null,
            )
            is Outcome.Err -> _screenState.value = Screen.Setup(
                error = outcome.error.toUserMessage()
            )
        }
    }
}