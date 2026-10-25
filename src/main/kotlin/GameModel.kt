object GameModel {

    fun process(state: GameState, action: GameAction): Outcome {
        return when (action) {
            is GameAction.ChooseRank -> chooseRank(state, action)
            is GameAction.SubmitCards -> submitCards(state, action)
            is GameAction.DecideChallenge -> decideChallenge(state, action)
        }
    }
    fun chooseRank(state: GameState, action: GameAction.ChooseRank): Outcome {
        return when (state.phase) {
            is TurnPhase.AwaitingRank -> Outcome.Ok(state.copy(
                roundRank = action.rank,
                phase = TurnPhase.AwaitingCards,
            ))
            else -> Outcome.Err(GameError.InvalidAction)
        }
    }

    fun submitCards(state: GameState, action: GameAction.SubmitCards): Outcome {
        when (state.phase) {
            is TurnPhase.AwaitingCards -> {
                if (action.cards.isEmpty()) {
                    return Outcome.Err(GameError.NoCardsSubmitted)
                }

                val current = state.current

                if (!current.hand.containsAll(action.cards)) {
                    return Outcome.Err(GameError.CardsNotInHand)
                }

                val newPlayer = current.removeCards(action.cards)
                val updated = state.players.updated(state.currentIndex, newPlayer)

                return Outcome.Ok(state.copy(
                    players = updated,
                    current = state.nextPlayer,
                    pile = state.pile + action.cards,
                    phase = TurnPhase.AwaitingChallenge,
                ))
            }
            else -> {
                return Outcome.Err(GameError.InvalidAction)
            }
        }
    }

    fun decideChallenge(state: GameState, action: GameAction.DecideChallenge): Outcome {
        when (state.phase) {
            is TurnPhase.AwaitingChallenge -> {
                if (!action.challenge) {
                    return Outcome.Ok(state.copy(
                        phase = TurnPhase.AwaitingCards
                    ))
                }

                val isLying = state.lastPlayed.any {
                    it.rank != state.roundRank
                }

                val loser = if (isLying) state.previousPlayer else state.current
                val loserIndex = state.players.indexOf(loser)

                val newLoser = loser.addCards(state.pile)
                val newPlayers = state.players.updated(loserIndex, newLoser)

                return Outcome.Ok(state.copy(
                    players = newPlayers,
                    current = state.playerAfterIndex(loserIndex),
                    pile = emptyList(),
                    roundRank = null,
                    lastPlayed = emptyList(),
                    phase = TurnPhase.AwaitingRank,
                ))


            }
            else -> {
                return Outcome.Err(GameError.InvalidAction)
            }
        }
    }


    fun <T> List<T>.updated(index: Int, item: T): List<T> {
        val mut = toMutableList()
        mut[index] = item
        return mut
    }
}