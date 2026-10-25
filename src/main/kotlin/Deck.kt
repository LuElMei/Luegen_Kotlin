data class Deck(val cards: List<Card> = standard()) {
    companion object {
        fun standard(): List<Card> {
            return buildList {
                for (rank in Rank.entries) {
                    for (suit in Suit.entries)  {
                        add(Card(suit, rank))
                    }
                }
            }
        }
    }

    fun shuffle() = cards.shuffled()
}