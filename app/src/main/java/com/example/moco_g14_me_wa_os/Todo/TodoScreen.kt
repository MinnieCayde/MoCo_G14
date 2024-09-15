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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight


@Composable
fun TododScreen() {

    val todoViewModel: TodoViewModel = hiltViewModel()

    val tasks by todoViewModel.allTasks.collectAsState()

    // state to control visibility of the new task form
    var showNewTaskDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Show the NewTaskForm dialog
                showNewTaskDialog = true
            }, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(painter = painterResource(id = R.drawable.add_24), contentDescription = "New Task", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
        content = { paddingValues ->
            // Apply padding to the content
            Column(modifier = Modifier.padding(paddingValues)) {
                TaskList(
                    tasks = tasks,
                    onTaskClick = { task ->
                        todoViewModel.update(task.copy(completed = !task.completed))
                    },
                    onTaskRemove = { task ->
                        todoViewModel.delete(task)
                    }
                )
            }

            // Show when the state is true
            if (showNewTaskDialog) {
                Dialog(
                    onDismissRequest = {
                        // Hide the dialog when clicking outside or dismissing it
                        showNewTaskDialog = false
                    },
                    properties = DialogProperties(
                        dismissOnClickOutside = true
                    )
                ) {

                    Card(
                        shape = MaterialTheme.shapes.medium,  // Rounded edges
                        elevation = CardDefaults.cardElevation(8.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        NewTaskForm(onSaveClick = { name, description ->
                            // Handle task creation logic
                            todoViewModel.insert(Task(name = name, description = description, completed = false))
                            showNewTaskDialog = false
                        })
                    }
                }
            }
        }
    )
}

@Composable
fun TaskList(tasks: List<Task>, onTaskClick: (Task) -> Unit, onTaskRemove: (Task) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize() // Apply modifier to LazyColumn
    ) {
        items(tasks) { task ->
            TaskCard(
                task = task,
                taskName = task.name,
                description = task.description,
                sessions = task.sessions,
                completed = task.completed,
                onTaskClick = { updatedTask -> onTaskClick(updatedTask) },
                onTaskRemove = { onTaskRemove(task) }
            )
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    taskName: String,
    description: String,
    sessions: Int,
    completed: Boolean,
    onTaskClick: (Task) -> Unit,
    onTaskRemove: (Task) -> Unit
) {
    // Track task is clicked or not
   // var isClicked by remember { mutableStateOf(false) }

    if (completed) {
        LaunchedEffect(key1 = task) {
            kotlinx.coroutines.delay(1200) // Delay
            onTaskRemove(task)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(5.dp)
            .clickable {
                onTaskClick(task.copy(isClicked = !task.isClicked))
            },
        elevation = if (task.isClicked) CardDefaults.cardElevation(0.dp) else CardDefaults.cardElevation(
            8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Change the button color when clicked
            IconButton(
                onClick = {
                    onTaskClick(task.copy(isClicked = !task.isClicked))
                },
                modifier = Modifier.size(48.dp)  // Adjust button size
            ) {
                Image(
                    painter = painterResource(id = if (completed) R.drawable.checked_24 else R.drawable.unchecked_24),
                    contentDescription = "Complete",
                    colorFilter = ColorFilter.tint(if (task.isClicked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onPrimary)
                )
            }
                // Change text color when clicked
                Text(
                    text = taskName,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Start),
                    color = if (task.isClicked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = description,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                    color = if (task.isClicked) Color.Cyan else MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Dodo's left: ${task.sessions}",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.End),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }


@Composable
fun NewTaskForm(onSaveClick: (String, String) -> Unit) {
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

            NumberPickerDialog(
                initialNumber = numberDurations,
                onNumberSelected = { priority -> numberDurations = priority })


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onSaveClick(name, description) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            ) {
                Text("Save")
            }
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
        Text("Priority: $selectedNumber")
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
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
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



@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {

    val tasks = listOf(
        Task(name = "Mock Task 1", description = "This is a mock task", completed = false),
        Task(name = "Mock Task 2", description = "Another mock task", completed = true)
    )


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* No action for preview */ }) {
                Icon(painter = painterResource(id = R.drawable.add_24), contentDescription = "New Task")
            }
        }
    ) { paddingValues ->
        TaskList(
            tasks = tasks,
            onTaskClick = { /* No action for preview */ },
            onTaskRemove = { /* No action for preview */ },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    TaskCard(
        task = Task(name = "Mock Task", description = "This is a preview of a task card", completed = false),
        taskName = "Mock Task",
        description = "This is a preview of a task card",
        sessions = 1,
        completed = false,
        onTaskClick = { /* No action for preview */ },
        onTaskRemove = { /* No action for preview */ }
    )
}

@Preview(showBackground = true)
@Composable
fun NewTaskFormPreview() {
    NewTaskForm(onSaveClick = { name, description ->
        // No real action needed in the preview
    })
}

@Preview(showBackground = true)
@Composable
fun TaskListPreview() {

    val tasks = listOf(
        Task(name = "Mock Task 1", description = "This is the first task", completed = false),
        Task(name = "Mock Task 2", description = "This is the second task", completed = true),
        Task(name = "Mock Task 3", description = "This is the third task", completed = false)
    )

    TaskList(
        tasks = tasks,
        onTaskClick = { /* No action for preview */ },
        onTaskRemove = { /* No action for preview */ }
    )
}
