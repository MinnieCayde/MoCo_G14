package com.example.moco_g14_me_wa_os.Todo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import com.example.moco_g14_me_wa_os.R
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.runtime.key
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import com.example.moco_g14_me_wa_os.Timer.PomodoroTimerViewModel
import java.util.UUID

import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight


@Composable
fun TododScreen() {

    val todoViewModel: TodoViewModel = hiltViewModel()
    val timerViewModel: PomodoroTimerViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        todoViewModel.observerTimerViewModel(timerViewModel)
    }

    val tasks by todoViewModel.allTasks.collectAsState()
    val selectedTaskIds by todoViewModel.selectedTaskIds.collectAsState()

    // state to control visibility of the new task form
    var showNewTaskDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showNewTaskDialog = true
            }) {
                Icon(painter = painterResource(id = R.drawable.add_24), contentDescription = "New Todo")
            }
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                TaskList(
                    tasks = tasks,
                    onTaskClick = {task ->
                        if (task.completed) {
                            todoViewModel.delete(task)
                        }
                    },
                    onTaskRemove = { task ->
                        todoViewModel.delete(task)
                    },
                    onTaskSelect = { task ->
                        todoViewModel.toggleTaskSelection(task.taskID)
                    },
                    selectedTaskIds = selectedTaskIds
                )
            }

            // Show when the state is true
            if (showNewTaskDialog) {
                Dialog(
                    onDismissRequest = {
                        showNewTaskDialog = false
                    },
                    properties = DialogProperties(dismissOnClickOutside = true)
                ) {
                    Card(
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        NewTaskForm(onSaveClick = { name, description, sessions ->
                            todoViewModel.insert(Task(name = name, description = description, sessions = sessions, completed = false))
                            showNewTaskDialog = false
                        })
                    }
                }
            }
        }
    )
}

@Composable
fun TaskList(tasks: List<Task>,
             onTaskClick: (Task) -> Unit,
             onTaskRemove: (Task) -> Unit,
             onTaskSelect: (Task) -> Unit,
             selectedTaskIds: Set<UUID>,
             modifier: Modifier = Modifier) {

    LazyColumn(
        modifier = modifier.fillMaxSize() // Apply modifier to LazyColumn
    ) {
        items(tasks) { task ->
            key(task.taskID, task.sessions) {
                TaskCard(
                    task = task,
                    isSelected = task.taskID in selectedTaskIds,
                    onTaskClick = { updatedTask -> onTaskClick(updatedTask) },
                    onTaskRemove = { onTaskRemove(task) },
                    onTaskSelect = { onTaskSelect(task) }
                )
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    isSelected: Boolean,
    onTaskClick: (Task) -> Unit,
    onTaskRemove: (Task) -> Unit,
    onTaskSelect: (Task) -> Unit
) {
    val cardColor = when {
        task.sessions > 7 -> colorResource(id = R.color.purple_200)
        task.sessions in 5..7 -> colorResource(id = R.color.primary)
        task.sessions in 3..5 -> colorResource(id = R.color.purple_500)
        task.sessions in 1..2 -> colorResource(id = R.color.purple_700)
        else -> colorResource(id = R.color.green)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(5.dp)
            .clickable { if (task.completed){ onTaskRemove(task) } else onTaskSelect(task) },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (task.completed) {
                    onTaskRemove(task)
                } else {
                    onTaskSelect(task)
                } },
                modifier = Modifier.size(48.dp)
            ) {
                Image(
                    painter = painterResource(id = if (isSelected || task.completed) R.drawable.checked_24 else R.drawable.unchecked_24),
                    contentDescription = "Complete",
                    colorFilter = ColorFilter.tint(if (isSelected) Color.Green else Color.Black)
                )
            }
            Text(
                text = task.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Start),
                color = if (isSelected) Color.Cyan else MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = task.description,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                color = if (isSelected) Color.Cyan else MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (task.completed) "Completed:)" else  "Dodo's left: ${task.sessions}",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.End),
                color = if (isSelected) Color.Cyan else MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


@Composable
fun NewTaskForm(onSaveClick: (String, String, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var numberDurations by remember { mutableIntStateOf(1) }
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onSecondary) // Hintergrundfarbe für den gesamten Screen
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "New Task",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    focusedTextColor = MaterialTheme.colorScheme.secondary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.secondary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Dodo's: $numberDurations",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.SemiBold
            )

        NumberPickerDialog(initialNumber = numberDurations, onNumberSelected = { sessions -> numberDurations = sessions })


            Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSaveClick(name, description, numberDurations) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
        ) {
            Text("Save")
        }
    }
}

@Composable
fun NumberPickerDialog(initialNumber: Int, onNumberSelected: (Int) -> Unit) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var selectedNumber by remember { mutableStateOf(initialNumber) }

    // Numbers from 1 to 10
    val numbers = (1..10).toList()
    // Repeated list to simulate an infinite scroll
    val infiniteNumbers = List(1000) { numbers[it % numbers.size] }
    val middleIndex = infiniteNumbers.size / 2
    // LazyListState to handle the initial index
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = middleIndex + initialNumber - 1)

    Button(
        onClick = { isDialogOpen = true },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Do in Dodo's: $selectedNumber")
    }

    if (isDialogOpen) {
        Dialog(onDismissRequest = {
            isDialogOpen = false
            // Select the number based on the visible item in the middle
            val selectedIndex = (scrollState.firstVisibleItemIndex + 2) % numbers.size
            selectedNumber = numbers[selectedIndex]
            onNumberSelected(selectedNumber)
        }) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp)), // Ensure rounded corners
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary)
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp)) // Clip for rounded corners
                ) {
                    LazyColumn(
                        state = scrollState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Display the infinite number list
                        items(infiniteNumbers.size) { index ->
                            val number = infiniteNumbers[index]
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        selectedNumber = number
                                        onNumberSelected(number)
                                        isDialogOpen = false
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = number.toString(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (number == selectedNumber) Color.Black else Color.Gray
                                )
                            }
                        }
                    }
                    // Box for selecting, with top and bottom lines
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .align(Alignment.Center)
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .height(1.dp)
                                .fillMaxWidth(0.8f)
                                .background(Color.Gray)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .height(1.dp)
                                .fillMaxWidth(0.8f)
                                .background(Color.Gray)
                        )
                    }
                }
            }
        }
    }
}