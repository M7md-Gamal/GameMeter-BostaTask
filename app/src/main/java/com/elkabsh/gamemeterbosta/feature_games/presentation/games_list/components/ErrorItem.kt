package com.elkabsh.gamemeterbosta.feature_games.presentation.games_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elkabsh.gamemeterbosta.ui.theme.GameMeterBostaTheme

@Composable
fun ErrorItem(message: String, modifier: Modifier = Modifier, onRetry: () -> Unit) {
    Column(
            modifier = modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
        )
        Button(onClick = onRetry, modifier = Modifier.padding(top = 8.dp)) { Text(text = "Retry") }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorItemPreview() {
    GameMeterBostaTheme { ErrorItem(message = "Failed to load games", onRetry = {}) }
}
