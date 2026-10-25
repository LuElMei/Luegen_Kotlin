data class GameState(
    val players: List<Player>,
    val current: Player,
    val roundRank: Rank?,
    val pile: List<Card>,
    val phase: TurnPhase,
    val lastPlayed: List<Card>,
) {
    companion object {
        fun initial(names: List<String>): Outcome {

            if (names.size !in 2..8) {
                return Outcome.Err(GameError.InvalidPlayerCount)
            }

            if (names.any { it.isBlank() }) {
                return Outcome.Err(GameError.BlankName)
            }

            if (names.distinct().size != names.size) {
                return Outcome.Err(GameError.DuplicateName)
            }

            val players = names.map { Player(it) }

            val deck = Deck().shuffle()
            val withCards = players.dealCards(deck)

            return Outcome.Ok(GameState(
                players = withCards,
                current = withCards.random(),
                roundRank = null,
                pile = emptyList(),
                lastPlayed = emptyList(),
                phase = TurnPhase.AwaitingRank
            ))
        }

        fun List<Player>.dealCards(deck: Deck): List<Player> {
            val hands = deck.cards
                .withIndex()
                .groupBy(
                    keySelector = { it.index % size},
                    valueTransform = { it.value },
                )
            return mapIndexed { index, player ->
                player.copy(hand = hands[index] ?: emptyList())
            }
        }
    }

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