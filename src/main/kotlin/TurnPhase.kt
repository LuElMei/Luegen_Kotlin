sealed interface TurnPhase {
    data object AwaitingRank : TurnPhase
    data object AwaitingCards : TurnPhase
    data object AwaitingChallenge : TurnPhase
}