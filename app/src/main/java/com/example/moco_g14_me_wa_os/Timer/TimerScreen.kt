import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.example.moco_g14_me_wa_os.R
import com.example.moco_g14_me_wa_os.Timer.PomodoroTimerViewModel
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable.ArrowDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.compose.rememberAsyncImagePainter// Für das Laden des GIFs oder Bildes
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.moco_g14_me_wa_os.Timer.State

@Composable
fun TimerScreen() {

    val viewModel: PomodoroTimerViewModel = hiltViewModel()
    val remainingTime by viewModel.remainingTime.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val totalTime by viewModel.totalTime.collectAsState()
    val progress = 1f - (remainingTime.toFloat() / totalTime)
    var currentSliderValue by remember { mutableStateOf((totalTime / (60 * 1000)).toFloat()) }
    var isFullScreenMode by remember { mutableStateOf(false) }
    val completedPomodoros by viewModel.completedPomodoros.collectAsState()

    // Observing the current state (Work, Shortbreak, Longbreak)
    val currentState by viewModel.state.collectAsState()

    // Check if it's break time
    val isBreakTime = currentState != State.Work

    // Create an ImageLoader with GIF support
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory()) // For API 28+
            } else {
                add(GifDecoder.Factory()) // For older devices
            }
        }
        .build()

    // Select the GIF resource based on break status and progress
    val gifResource = when {
        isBreakTime -> {
            if (progress < 0.01f) R.drawable.egg_hatching // Schlüpfendes Ei-GIF für den Übergang zur Pause
            else R.drawable.dodo // Dodo-GIF während der Pausenzeit
        }
        else -> {
            // Arbeitszeit: wähle das GIF abhängig vom Fortschritt
            when {
                progress < 0.25f -> R.drawable.egg// 0-25% des Fortschritts
                progress < 0.5f -> R.drawable.egg_cracked // 25-50% des Fortschritts
                progress < 0.75f -> R.drawable.egg_more_cracked // 50-75% des Fortschritts
                else -> R.drawable.egg_very_cracked // 75-100% des Fortschritts
            }
        }
    }



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
            modifier = Modifier.fillMaxWidth().height(500.dp).background(MaterialTheme.colorScheme.onSecondary, RoundedCornerShape(16.dp))
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
                // Load the GIF using Coil
                Image(
                    painter = rememberAsyncImagePainter(
                        model = gifResource,
                        imageLoader = imageLoader
                    ),
                    contentDescription = "Pomodoro GIF",
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .padding(start = 25.dp)
                        .size(200.dp), // Set the size of the GIF
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Time Display
            Text(
                text = formatSeconds(remainingTime),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp),
                color = MaterialTheme.colorScheme.onPrimary,
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
//        if (isFullScreenMode) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(500.dp)
//                    .background(MaterialTheme.colorScheme.onSecondary, RoundedCornerShape(16.dp))
//                    .pointerInput(Unit) {
//                        detectTransformGestures { _, pan, _, _ ->
//                            val sensitivity = 0.05f
//                            val adjustment = (pan.y * sensitivity).toFloat()
//                            val newValue = (currentSliderValue - adjustment).coerceIn(0.5f, 60f)
//                            currentSliderValue = newValue
//                            viewModel.setWorkDuration(newValue.roundToInt())
//                        }
//                    }
//                    .clickable {
//                        isFullScreenMode = false
//                        viewModel.setWorkDuration(currentSliderValue.roundToInt())
//                    }
//            ) {
//                Icon(modifier = Modifier.align(Alignment.TopCenter).padding(top = 60.dp).size(56.dp),painter = painterResource(id = R.drawable.timer_scroll_up), contentDescription = "Go up", tint = MaterialTheme.colorScheme.onPrimary)
//
//                Text(
//                    text = formatMinutes(currentSliderValue),
//                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 72.sp),
//                    color = MaterialTheme.colorScheme.onPrimary,
//                    modifier = Modifier.align(Alignment.Center)
//                )
//
//                Icon(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 60.dp).size(56.dp), painter = painterResource(id = R.drawable.timer_scroll_down), contentDescription = "Go up", tint = MaterialTheme.colorScheme.onPrimary )
//            }
//        }
        if (isFullScreenMode) {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            val sensitivity = 0.02f
            val minValue = 1f
            val maxValue = 60f
            var lastVibratedValue by remember { mutableStateOf(0) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .background(MaterialTheme.colorScheme.onSecondary, RoundedCornerShape(16.dp))
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, _, _ ->
                            val adjustment = pan.y * sensitivity
                            val newValue = ((currentSliderValue - adjustment - minValue) % (maxValue - minValue) + (maxValue - minValue)) % (maxValue - minValue) + minValue

                            if (newValue.roundToInt() != lastVibratedValue) {
                                // Haptisches Feedback nur bei Wertänderung
                                val vibrationEffect = VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE)
                                vibrator.vibrate(vibrationEffect)
                                lastVibratedValue = newValue.roundToInt()
                            }

                            currentSliderValue = newValue
                            viewModel.setWorkDuration(currentSliderValue.roundToInt())
                        }
                    }
                    .clickable {
                        isFullScreenMode = false
                        viewModel.setWorkDuration(currentSliderValue.roundToInt())
                    }
            ) {
                Icon(modifier = Modifier.align(Alignment.TopCenter).padding(top = 60.dp).size(56.dp),painter = painterResource(id = R.drawable.timer_scroll_up), contentDescription = "Go up", tint = MaterialTheme.colorScheme.onPrimary)
                Text(
                    text = formatMinutes(currentSliderValue),
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 72.sp),
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
                Icon(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 60.dp).size(56.dp), painter = painterResource(id = R.drawable.timer_scroll_down), contentDescription = "Go up", tint = MaterialTheme.colorScheme.onPrimary )
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