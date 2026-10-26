package view.running

import GameState
import Player
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

private val SeatWidth = 120.dp
private val SeatHeight = 56.dp
private val SeatMargin = 12.dp // Abstand zwischen Tischkante und Sitz

@Composable
fun TableArea(state: GameState, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val tableWidth = (maxWidth * 0.5f).coerceAtMost(420.dp)
        val tableHeight = tableWidth * 0.5f

        Table(state, Modifier.size(tableWidth, tableHeight))

        val slots = remember(state.players.size) { seatSlots(state.players.size) }

        state.players.zip(slots) { player, slot ->
            val pos = seatOffset(slot, tableWidth, tableHeight)
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

private fun seatOffset(slot: SeatSlot, tableW: Dp, tableH: Dp): DpOffset {
    val (side, k, n) = slot
    val w = tableW.value
    val h = tableH.value

    // Verschiebung entlang der Kante (im Uhrzeigersinn), Plätze gleichmäßig auf die Kante verteilt
    val horizontal = side.ny != 0
    val edgeLen = if (horizontal) w else h
    val along = (k + 0.5f) / n * edgeLen - edgeLen / 2
    val dirX = -side.ny // Uhrzeigersinn-Richtung = Normale um 90° gedreht
    val dirY = side.nx

    // Punkt auf dem Tischrand
    val px = side.nx * w / 2 + dirX * along
    val py = side.ny * h / 2 + dirY * along

    // Sitzmitte so weit nach außen schieben, dass der Sitz den Tisch nicht überlappt
    val halfSeat = if (horizontal) SeatHeight.value / 2 else SeatWidth.value / 2
    val dist = halfSeat + SeatMargin.value

    return DpOffset(
        x = (px + side.nx * dist).dp,
        y = (py + side.ny * dist).dp,
    )
}