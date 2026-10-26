package view

import Card
import GameState
import Player
import Suit
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val SeatWidth = 120.dp
private val SeatHeight = 56.dp
private val SeatMargin = 12.dp // Abstand zwischen Tischkante und Sitz

@Composable
fun GameRunningScreen(state: GameState, error: String?) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TableArea(
            state = state,
            modifier = Modifier.weight(1f).fillMaxWidth(),
        )

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            )
        }

        Spacer(Modifier.height(12.dp))

        HandRow(player = state.current, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun TableArea(state: GameState, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val tableWidth = (maxWidth * 0.5f).coerceAtMost(420.dp)
        val tableHeight = tableWidth * 0.5f

        Table(state, Modifier.size(tableWidth, tableHeight))

        state.players.forEachIndexed { index, player ->
            val pos = seatPosition(index, state.players.size, tableWidth, tableHeight)
            PlayerSeat(
                player = player,
                isCurrent = player == state.current,
                modifier = Modifier.offset(pos.x, pos.y),
            )
        }
    }
}

@Composable
private fun Table(state: GameState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color(0xFF2E7D32), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Stapel: ${state.pile.size}",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Rang: ${state.roundRank?.symbol ?: "–"}",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun PlayerSeat(player: Player, isCurrent: Boolean, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(SeatWidth, SeatHeight),
        shape = RoundedCornerShape(12.dp),
        color = if (isCurrent) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        border = if (isCurrent) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        tonalElevation = 2.dp,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 8.dp),
        ) {
            Text(
                text = player.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${player.cardCount()} Karten",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun HandRow(player: Player, modifier: Modifier = Modifier) {
    val sortedHand = remember(player.hand) {
        player.hand.sortedWith(compareBy({ it.rank }, { it.suit }))
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Hand von ${player.name}",
            style = MaterialTheme.typography.titleSmall,
        )

        Spacer(Modifier.height(8.dp))

        // Zentriert, solange alles passt; sonst horizontal scrollbar
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        ) {
            items(sortedHand) { card -> CardView(card) }
        }
    }
}

@Composable
private fun CardView(card: Card, modifier: Modifier = Modifier) {
    val color = when (card.suit) {
        Suit.HEARTS, Suit.DIAMONDS -> Color(0xFFC62828)
        else -> Color.Black
    }

    Surface(
        modifier = modifier.size(52.dp, 76.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color.LightGray),
        shadowElevation = 2.dp,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = card.rank.symbol,
                color = color,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = card.suit.symbol,
                color = color,
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

// Reihenfolge = Uhrzeigersinn, beginnend unten
private enum class Side(val nx: Int, val ny: Int) {
    Bottom(0, 1), Left(-1, 0), Top(0, -1), Right(1, 0)
}

/** Reihenfolge, in der Kanten einen zweiten Platz bekommen (ab 5 Spielern). */
private val DoubleSeatOrder = listOf(Side.Top, Side.Bottom, Side.Left, Side.Right)

/** Wie viele Spieler an welcher Kante sitzen. */
private fun seatsPerSide(count: Int): Map<Side, Int> = when (count) {
    2 -> mapOf(Side.Bottom to 1, Side.Top to 1)
    3 -> mapOf(Side.Bottom to 1, Side.Left to 1, Side.Right to 1)
    else -> {
        val doubled = DoubleSeatOrder.take(count - 4)
        Side.entries.associateWith { if (it in doubled) 2 else 1 }
    }
}

private data class SeatPos(val x: Dp, val y: Dp)

/** Position (relativ zur Tischmitte) des Sitzes [index] von [count], im Uhrzeigersinn ab unten. */
private fun seatPosition(index: Int, count: Int, tableW: Dp, tableH: Dp): SeatPos {
    val w = tableW.value
    val h = tableH.value

    // Alle Plätze im Uhrzeigersinn: (Kante, Platz auf der Kante, Plätze auf der Kante)
    val perSide = seatsPerSide(count)
    val (side, k, n) = Side.entries
        .flatMap { s -> val n = perSide[s] ?: 0; List(n) { Triple(s, it, n) } }[index]

    // Verschiebung entlang der Kante (im Uhrzeigersinn), Plätze gleichmäßig auf die Kante verteilt
    val horizontal = side.ny != 0
    val edgeLen = if (horizontal) w else h
    val along = (k + 0.5f) / n * edgeLen - edgeLen / 2
    val dirX = -side.ny // Uhrzeigersinn-Richtung = Normale um 90° gedreht
    val dirY = side.nx

    val px = side.nx * w / 2 + dirX * along
    val py = side.ny * h / 2 + dirY * along

    // Sitzmitte so weit nach außen schieben, dass der Sitz den Tisch nicht überlappt
    val halfSeat = if (horizontal) SeatHeight.value / 2 else SeatWidth.value / 2
    val dist = halfSeat + SeatMargin.value

    return SeatPos(
        x = (px + side.nx * dist).dp,
        y = (py + side.ny * dist).dp,
    )
}