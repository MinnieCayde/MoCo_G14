package com.example.moco_g14_me_wa_os.Todo

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun TododScreen(todoViewModel: TodoViewModel) {

    val todoViewModel: TodoViewModel = hiltViewModel()

    val tasks by todoViewModel.allTasks.collectAsState()

    // state to control visibility of the new task form
    var showNewTaskDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Show the NewTaskForm dialog
                showNewTaskDialog = true
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "New Task")
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
                completed = task.completed,
                onTaskClick = { onTaskClick(task)},
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
    completed: Boolean,
    onTaskClick: () -> Unit,
    onTaskRemove: (Task) -> Unit
) {
    // Track whether the task is clicked or not
    var isClicked by remember { mutableStateOf(false) }

    if (completed) {
        LaunchedEffect(key1 = task) {
            kotlinx.coroutines.delay(1200) // Delay for 15 seconds
            onTaskRemove(task)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(5.dp)
            .clickable {
                isClicked = !isClicked
                onTaskClick()  // Trigger the task click callback
            },
        elevation = if (isClicked) CardDefaults.cardElevation(0.dp) else CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary //.copy(alpha = 0.1f) else MaterialTheme.colorScheme.background
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
                    isClicked = !isClicked
                    onTaskClick()
                },
                modifier = Modifier.size(48.dp)  // Adjust button size
            ) {
                Image(
                    painter = painterResource(id = if (completed) R.drawable.checked_24 else R.drawable.unchecked_24),
                    contentDescription = "Complete",
                    colorFilter = ColorFilter.tint(if (isClicked) Color.Cyan else Color.Black)
                )
            }

            // Change text color when clicked
            Text(
                text = taskName,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = if (isClicked) Color.Cyan else MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isClicked) Color.Cyan else MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


@Composable
fun NewTaskForm(onSaveClick: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(text = "New Task", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(vertical = 16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSaveClick(name, description) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
        ) {
            Text("Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoScreenPreview() {
    // Sample static data for preview purposes
    val tasks = listOf(
        Task(name = "Mock Task 1", description = "This is a mock task", completed = false),
        Task(name = "Mock Task 2", description = "Another mock task", completed = true)
    )

    // Directly pass the sample tasks to the TaskList composable
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* No action for preview */ }) {
                Icon(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "New Task")
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
    // Sample static tasks for preview purposes
    val tasks = listOf(
        Task(name = "Mock Task 1", description = "This is the first task", completed = false),
        Task(name = "Mock Task 2", description = "This is the second task", completed = true),
        Task(name = "Mock Task 3", description = "This is the third task", completed = false)
    )

    // Display the TaskList with the static tasks
    TaskList(
        tasks = tasks,
        onTaskClick = { /* No action for preview */ },
        onTaskRemove = { /* No action for preview */ }
    )
}
