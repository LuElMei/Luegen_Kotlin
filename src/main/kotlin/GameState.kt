data class GameState(
    val players: List<Player>,
    val current: Player,
    val roundRank: Rank?,
    val pile: List<Card>,
    val phase: TurnPhase,
    val lastPlayed: List<Card>,
) {
    val currentIndex: Int
        get() = players.indexOf(current)

    val nextPlayer: Player
        get() = playerAfterIndex(currentIndex)

    val previousPlayer: Player
        get() {
            val index = (currentIndex - 1) % players.size
            return players[index]
        }

    fun playerAfterIndex(index: Int): Player {
        val nextIndex = (index + 1) % players.size
        return players[nextIndex]
    }
}