package com.example.moco_g14_me_wa_os.Todo

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.moco_g14_me_wa_os.Timer.PomodoroTimerViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {
    val allTasks: StateFlow<List<Task>> = repository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

    fun deleteTaskById(taskId: UUID) = viewModelScope.launch {
        repository.deleteTaskById(taskId)
    }

    fun onTaskClick(task: Task) = viewModelScope.launch {
        update(task.copy(isClicked = !task.isClicked))
    }

    fun observerTimerViewModel(timerViewModel: PomodoroTimerViewModel) {
        viewModelScope.launch {
            timerViewModel.onSessionCompleted.collect { completedTask ->

                if (completedTask != null) {
                    // Decrement sessions for all tasks with sessions > 0
                    allTasks.value.forEach { task ->
                        if (task.sessions > 0) {
                            update(task.copy(sessions = task.sessions - 1))
                        }
                    }
                    timerViewModel._onSessionCompleted.value = null

                }
            }
        }
    }
}