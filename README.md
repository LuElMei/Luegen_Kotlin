An implementation of the popular card game Luegen in Kotlin, using Jetpack Compose and Multiplatform.

The project follows the MVI (Model-View-Intent) and redux architecture.

- All of the games data is stored in an immutable GameState data class, acting as a Single Source of Truth
- A sealed interface GameAction bundling all possible actions and events that could change the games state
- A stateless GameModel object with a reducer method following the redux pattern
- A GameError sealed interface that contains all possible user-based errors of the game
- An outcome sealed interface that acts as a Result type: On success it returns a new game state,
  on error a GameError
- Classes like player, card, or Deck are immutable data classes, transforming methods return a new instance

The popular card game Luegen (Bluff or Liar) is a game about lying and revealing lies.
It can be played from 2 to 8 players.
A 52 card deck is dealt evenly to the players.
Each round, the starting player decides a rank for the round. He plays cards of said rank,
removing them from his hand, placing them face down in the middle of the table.
The next player has to follow up: Either he plays cards of said rank, or he can play other cards,
lying that he played the required rank.
A the start of their turn, a player can decide to challenge the previous player, revealing their played cards.
If the previous player lied, they have to take all of the cards in the middle, including
all the other players played cards. if they didnt lie, the challenger has to take the cards.

The queens are a special rank: They are called the witch cards, they cant be claimed as a rank at the start
of a round, and if a player accumulates all four, they lose the game immediatly.
The only way to get rid of the queens is lying about playing the claimed rank, playing them silently.
  
