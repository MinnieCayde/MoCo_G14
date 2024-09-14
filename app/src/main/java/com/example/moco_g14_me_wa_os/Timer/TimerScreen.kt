import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.example.moco_g14_me_wa_os.R
import com.example.moco_g14_me_wa_os.Timer.PomodoroTimerViewModel
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt
@Composable
fun TimerScreen(viewModel: PomodoroTimerViewModel = hiltViewModel()) {
    val remainingTime by viewModel.remainingTime.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val totalTime by viewModel.totalTime.collectAsState()
    val progress = 1f - (remainingTime.toFloat() / totalTime)
    var currentSliderValue by remember { mutableStateOf((totalTime / (60 * 1000)).toFloat()) }
    var isFullScreenMode by remember { mutableStateOf(false) }
    val completedPomodoros by viewModel.completedPomodoros.collectAsState()

    val context = LocalContext.current
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.eggblue_animation))
    val lottieAnimatable = rememberLottieAnimatable()

    LaunchedEffect(composition) {
        lottieAnimatable.animate(composition, iterations = LottieConstants.IterateForever)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val backgroundColor = MaterialTheme.colorScheme.background

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Circular Progress Indicator overlaying the animation
            Box(modifier = Modifier.size(250.dp)) {
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 8.dp,
                    trackColor = ProgressIndicatorDefaults.circularTrackColor,
                )
                // Animation centered within the circle
                AnimatedPreloader(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Time Display
            Text(
                text = formatSeconds(remainingTime),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clickable { if (!isRunning) isFullScreenMode = true }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons Row
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { viewModel.startTimer() }, enabled = !isRunning) {
                    Text("Start")
                }
                Button(onClick = { viewModel.pauseTimer() }, enabled = isRunning) {
                    Text("Pause")
                }
            }
        }

        // Fullscreen Mode for setting the timer
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
                    .clickable {
                        isFullScreenMode = false
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

// Utility functions to format the time
fun formatMinutes(minutes: Float): String {
    return String.format("%02d:00", minutes.roundToInt())
}

fun formatSeconds(timeInMillis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60
    return String.format("%02d:%02d", minutes, seconds)
}
@Composable
fun AnimatedPreloader(modifier: Modifier = Modifier) {
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.eggblue_animation
        )
    )

    val preloaderProgress by animateLottieCompositionAsState(
        preloaderLottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )


    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier
    )
}