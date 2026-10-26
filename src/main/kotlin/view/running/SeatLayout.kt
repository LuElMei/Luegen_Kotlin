package view.running

// SeatLayout.kt
enum class Side(val nx: Int, val ny: Int) {
    Bottom(0, 1), Left(-1, 0), Top(0, -1), Right(1, 0)
}

private val DoubleSeatOrder = listOf(Side.Top, Side.Bottom, Side.Left, Side.Right)

data class SeatSlot(val side: Side, val index: Int, val count: Int)

fun seatSlots(playerCount: Int): List<SeatSlot> {
    val perSide = seatsPerSide(playerCount)
    return Side.entries.flatMap { side ->
        val n = perSide[side] ?: 0
        List(n) { SeatSlot(side, it, n) }
    }
}

private fun seatsPerSide(count: Int): Map<Side, Int> = when (count) {
    2 -> mapOf(Side.Bottom to 1, Side.Top to 1)
    3 -> mapOf(Side.Bottom to 1, Side.Left to 1, Side.Right to 1)
    else -> {
        val doubled = DoubleSeatOrder.take(count - 4)
        Side.entries.associateWith { if (it in doubled) 2 else 1 }
    }
}