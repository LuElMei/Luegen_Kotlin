package view

sealed interface Screen {
    object Startup : Screen
    object Setup : Screen
    object Running : Screen
}