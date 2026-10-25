import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class GameController {
    private val _state = MutableStateFlow<GameState?>(null)
    val state = _state.asStateFlow()

    fun startGame(names: List<String>) {
        val outcome = GameState.initial(names)
        when (outcome) {
            is Outcome.Ok -> _state.value = outcome.state
            is Outcome.Err -> println("Error!")
        }
    }

    fun onAction(action: GameAction) {
        val currentState = _state.value ?: return
        val outcome = GameModel.process(currentState, action)

        when (outcome) {
            is Outcome.Ok -> _state.value = outcome.state
            is Outcome.Err -> TODO()
        }
    }

}