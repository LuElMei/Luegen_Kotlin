sealed interface GameAction {
    data class ChooseRank(val rank: Rank) : GameAction
    data class SubmitCards(val cards: List<Card>) : GameAction
    data class DecideChallenge(val challenge: Boolean) : GameAction
}