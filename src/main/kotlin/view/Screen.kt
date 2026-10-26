package view

sealed interface Screen {
    object Startup : Screen
    data class Setup(val error: String? = null) : Screen
    object Running : Screen
}