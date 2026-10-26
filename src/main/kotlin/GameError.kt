enum class GameError {
    InvalidAction,
    CardsNotInHand,
    NoCardsSubmitted,
    InvalidPlayerCount,
    BlankName,
    DuplicateName;

    fun toUserMessage(): String {
        return when (this) {
            GameError.InvalidAction -> "Aktion in diesem Zustand nicht erlaubt"
            GameError.CardsNotInHand -> "Nicht alle Karten in der Hand des Spielers"
            GameError.NoCardsSubmitted -> "Keine Karten ausgewaehlt"
            GameError.InvalidPlayerCount -> "Ungueltige Spieleranzahl"
            GameError.BlankName -> "Namen duerfen nicht leer sein"
            GameError.DuplicateName -> "Alle Namen muessen verschieden sein"
        }
    }
}