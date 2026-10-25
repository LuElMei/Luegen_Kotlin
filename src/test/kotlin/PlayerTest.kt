import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class PlayerTest {

    val player = Player("Alice")
    val cards = listOf(
        Card(Suit.SPADES, Rank.SIX),
        Card(Suit.DIAMONDS, Rank.SEVEN),
        Card(Suit.CLUBS, Rank.EIGHT)
    )

    @Test
    @DisplayName("cards get added to hand")
    fun addCards() {
        val result = player.addCards(cards)

        assert(result.hand.isNotEmpty())
        assertContentEquals(cards, result.hand)
    }

    @Nested
    inner class RemoveCardsTests {

        val withCards = player.addCards(cards)

        @Test
        @DisplayName("card contained in players hand removed")
        fun removeCardContained() {
            val toRemove = cards[0]

            val result = withCards.removeCards(listOf(toRemove))

            assert(result.hand.size == cards.size - 1)
            assert(!result.hand.contains(toRemove))
        }

        @Test
        @DisplayName("card not contained in player does not change player")
        fun removeCardNotContained() {
            val toRemove = Card(Suit.HEARTS, Rank.ACE)
            val result = withCards.removeCards(listOf(toRemove))

            assertContentEquals(cards, result.hand)
        }
    }

    @Test
    fun hasCards() {
        val withCards = player.addCards(cards)
        assert(withCards.hasCards())
    }

    @Test
    fun cardCount() {
        val withCards = player.addCards(cards)
        assert(withCards.cardCount() == 3)
    }

    @Test
    fun discardQuartets() {
        val quartetRank = Rank.NINE
        val quartet = Suit.entries.map { Card(it, quartetRank) }
        val withCards = player.addCards(cards)
        val withQuartet = withCards.addCards(quartet)

        val (result, discarded) = withQuartet.discardQuartets()

        assert(result.hand.isNotEmpty())
        assert(quartet.all { !player.hand.contains(it) })
        assertContentEquals(quartet, discarded)
    }

}