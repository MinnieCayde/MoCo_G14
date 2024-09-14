import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moco_g14_me_wa_os.Timer.PomodoroTimerViewModel
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moco_g14_me_wa_os.Timer.State
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@Composable
fun TimerScreen(
    viewModel: PomodoroTimerViewModel = hiltViewModel()
) {
    val remainingTime by viewModel.remainingTime.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val totalTime by viewModel.totalTime.collectAsState()
    val progress = 1f - (remainingTime.toFloat() / totalTime)
    val completedPomodoros by viewModel.completedPomodoros.collectAsState()

    var currentSliderValue by remember { mutableStateOf((totalTime / (60 * 1000)).toFloat()) }
    var isFullScreenMode by remember { mutableStateOf(false) }

    val currentState by viewModel.state.collectAsState()
    val isWorkState = currentState == State.Work

    Box(
        modifier = Modifier
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
            Text(
                text = formatSeconds(remainingTime),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { if (!isRunning && isWorkState) isFullScreenMode = true }
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Completed Pomodoros: $completedPomodoros",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 8.dp)
            )




            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        viewModel.startTimer()
                        currentSliderValue = (remainingTime / (60 * 1000)).toFloat()
                    },
                    enabled = !isRunning
                ) {
                    Text("Start")
                }
                Button(
                    onClick = { viewModel.pauseTimer() },
                    enabled = isRunning
                ) {
                    Text("Pause")
                }
//                Button(
//                    onClick = {
//                        viewModel.resetTimer()
//                        currentSliderValue = (totalTime / (60 * 1000)).toFloat()
//                    },
//                    enabled = !isRunning
//                ) {
//                    Text("Reset")
//                }
            }
        }

        // Vollbild-Modus für die Zeitauswahl
        if (isFullScreenMode) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, _, _ ->
                            val sensitivity = 0.05f
                            val adjustment = (pan.y * sensitivity).toFloat()
                            val newValue = (currentSliderValue - adjustment).coerceIn(0.5f, 60f)
                            currentSliderValue = newValue
                            viewModel.setWorkDuration(newValue.roundToInt())
                        }
                    }
                    .clickable { isFullScreenMode = false
                    viewModel.setWorkDuration(currentSliderValue.roundToInt())
                    }
            ) {
                Text(
                    text = formatMinutes(currentSliderValue),
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 72.sp),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

fun formatMinutes(minutes: Float): String {
    return String.format("%02d:00", minutes.roundToInt())
}

fun formatSeconds(timeInMillis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60
    return String.format("%02d:%02d", minutes, seconds)
}