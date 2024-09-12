import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moco_g14_me_wa_os.Timer.PomodoroTimerViewModel
import androidx.compose.ui.unit.sp

@Composable
fun TimerScreen(timerViewModel: PomodoroTimerViewModel = viewModel()) {

    val modifier: Modifier = Modifier
    val timerValue by timerViewModel.timerValue.collectAsState()
    val isRunning by timerViewModel.isRunning.collectAsState()
    val totalTime = 25 * 60 * 1000L // 25 minutes in milliseconds wichtig für progressbar
    val progress = 1f - (timerValue.toFloat() / totalTime)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(250.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 8.dp,
            trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Timer Dauer: ${totalTime} Minuten")
            Slider(
                value = timerValue.toFloat(),
                onValueChange = { newValue ->
                    timerViewModel.changeTime(newValue.toInt())
                },
                valueRange = 5f..60f, // Slider-Bereich von 5 bis 60 Minuten
                steps = 11, // Anzahl der Schritte zwischen 5 und 60 Minuten
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp) // Slider-Höhe anpassen
                    .padding(vertical = 8.dp) // Optional: Vertikale Polsterung
                    .alpha(0.5f) // Optional: Sichtbarkeit reduzieren
            )

            Text(
                text = formatTime(timerValue),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { timerViewModel.startTimer() },
                    enabled = !isRunning
                ) {
                    Text("Start")
                }
                Button(
                    onClick = { timerViewModel.pauseTimer() },
                    enabled = isRunning
                ) {
                    Text("Pause")
                }
                Button(
                    onClick = { timerViewModel.resetTimer() },
                    enabled = !isRunning
                ) {
                    Text("Reset")
                }
            }
        }
    }
}

@Composable
@Preview(name = "TimerScreen")
fun TimerScreenPreview() {
    MaterialTheme {
        TimerScreen()
    }
}

fun formatTime(timeInMillis: Long): String {
    val minutes = (timeInMillis / 1000) / 60
    val seconds = (timeInMillis / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}