import GameState.Companion.dealCards
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

import Outcome
import kotlin.test.assertIs

class GameStateTest {

    val players = listOf(
        Player("Alice"),
        Player("Bob"),
        Player("Luca"),
    )

    @Nested
    @DisplayName("initial method of game state")
    inner class InitialTests {

        @Nested
        @DisplayName("should return the correct error when")
        inner class InitialErrorTests {

            @Test
            fun `playerCount below 2`() {
                val name = listOf("Alice")
                val result = GameState.initial(name)
                val err = assertIs<Outcome.Err>(result)
                assertEquals(GameError.InvalidPlayerCount, err.error)
            }

            @Test
            fun `playerCount over 8`() {
                val names = listOf("Alice", "Bob", "Luca", "Hans", "Gisela", "Jonas", "Ben", "Karla", "Tom")
                val result = GameState.initial(names)
                val err = assertIs<Outcome.Err>(result)
                assertEquals(GameError.InvalidPlayerCount, err.error)
            }

            @Test
            fun `any Name is blank`() {
                val names = listOf("Alice", "")
                val result = GameState.initial(names)

                val err = assertIs<Outcome.Err>(result)
                assertEquals(GameError.BlankName, err.error)
            }

            @Test
            fun `any name is duplicate`() {
                val names = listOf("Alice", "Alice")
                val result = GameState.initial(names)
                val err = assertIs<Outcome.Err>(result)
                assertEquals(GameError.DuplicateName, err.error)
            }
        }

        @Test
        fun `should return a correct initial game state`() {
            val names = listOf("Alice", "Bob", "Karla")
            val result = GameState.initial(names)

            assertIs<Outcome.Ok>(result)
        }
    }

    @Test
    fun `dealCards should return emptyHands for empty deck`() {
        val deck = Deck().copy(cards = emptyList())
        val resultPlayers = players.dealCards(deck)

        assert(players.all { it.hand.isEmpty()} )
    }


    @Test
    fun getCurrentIndex() {

        val state = GameState(
            players = players,
            current = players[1]
        )

        assertEquals(1, state.currentIndex)
    }

    @Test
    fun getNextPlayer() {

        val state = GameState(
            players = players,
            current = players[1]
        )

        val result = state.nextPlayer

        assertEquals(players[2], result)
    }

    @Test
    fun getPreviousPlayer() {

        val state = GameState(players = players, current = players[1])
        val result = state.previousPlayer
        assertEquals(players[0], result)
    }

    @Test
    fun playerAfterIndex() {
        val state = GameState(players = players, current = players[0])
        val result = state.playerAfterIndex(1)
        assertEquals(players[2], result)
    }

}