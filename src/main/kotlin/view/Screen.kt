package view

import GameState

sealed interface Screen {
    object Startup : Screen
    data class Setup(val error: String? = null) : Screen
    class Running(
        val state: GameState,
        val error: String?
    ) : Screen
}