package view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun GameSetupScreen(
    minPlayers: Int = 2,
    maxPlayers: Int = 8,
    error: String?,
    onStartGame: (List<String>) -> Unit
) {
    var playerNames by remember {
        mutableStateOf(List(3) { "Spieler ${it + 1}" })
    }

    val playerCount = playerNames.size.toFloat()

    val isInputValid = playerNames.size in minPlayers..maxPlayers &&
            playerNames.none { it.isBlank() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Spiel einrichten",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Spieleranzahl: ${playerNames.size}",
                style = MaterialTheme.typography.titleMedium
            )

            Slider(
                value = playerCount,
                onValueChange = { newCountFloat ->
                    val targetCount = newCountFloat.roundToInt()
                    if (targetCount != playerNames.size) {
                        playerNames = if (targetCount > playerNames.size) {
                            playerNames + List(targetCount - playerNames.size) { i ->
                                "Spieler ${playerNames.size + i + 1}"
                            }
                        } else {
                            playerNames.take(targetCount)
                        }
                    }
                },
                valueRange = minPlayers.toFloat()..maxPlayers.toFloat(),
                steps = maxPlayers - minPlayers - 1
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 2. Dynamische Eingabefelder ---
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(playerNames) { index, name ->
                OutlinedTextField(
                    value = name,
                    onValueChange = { updatedName ->
                        playerNames = playerNames.toMutableList().also {
                            it[index] = updatedName
                        }
                    },
                    label = { Text("Name Spieler ${index + 1}") },
                    singleLine = true,
                    isError = name.isBlank(),
                    supportingText = {
                        if (name.isBlank()) {
                            Text("Name darf nicht leer sein")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Fehler"
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. Start-Button ---
        Button(
            onClick = { onStartGame(playerNames.map { it.trim() }) },
            enabled = isInputValid,
            modifier = Modifier.fillMaxWidth(0.5f),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            Text(
                text = "Spiel starten",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}