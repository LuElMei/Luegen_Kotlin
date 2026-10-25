data class Player(val name: String, val hand: List<Card> = emptyList()) {

    fun addCards(cards: List<Card>) = copy(hand = hand + cards)
    fun removeCards(cards: List<Card>) = copy(hand = hand - cards)

    fun hasCards() = hand.isNotEmpty()
    fun cardCount() = hand.size

    fun discardQuartets(): Pair<Player, List<Card>> {
        val groupedByRank = hand.groupBy { it.rank }
        val quartetCards = groupedByRank
            .filter { it.value.size == 4 }
            .values
            .flatten()

        return copy(hand = hand - quartetCards) to quartetCards
    }
}
