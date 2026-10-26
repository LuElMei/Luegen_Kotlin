package view.running

import GameState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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