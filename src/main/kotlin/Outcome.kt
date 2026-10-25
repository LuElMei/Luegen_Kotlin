sealed interface Outcome {
    data class Ok(val state: GameState) : Outcome
    data class Err(val error: GameError) : Outcome
}