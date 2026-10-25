data class Card(val suit: Suit, val rank: Rank) {
    override fun toString() = "${suit.symbol}${rank.symbol}"
}


