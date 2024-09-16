
package com.example.moco_g14_me_wa_os.Todo

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.moco_g14_me_wa_os.Timer.PomodoroTimerViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {
    /*val allTasks: StateFlow<List<Task>> = repository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )*/

    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    val allTasks: StateFlow<List<Task>> = _allTasks.asStateFlow()

    private val _selectedTaskIds = MutableStateFlow<Set<UUID>>(emptySet())
    val selectedTaskIds: StateFlow<Set<UUID>> = _selectedTaskIds.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allTasks.collect {
                _allTasks.value = it
            }
        }
    }

    fun toggleTaskSelection(taskId: UUID) {
        _selectedTaskIds.value = if (_selectedTaskIds.value.contains(taskId)) {
            _selectedTaskIds.value - taskId
        } else {
            _selectedTaskIds.value + taskId
        }
    }

    fun observerTimerViewModel(timerViewModel: PomodoroTimerViewModel) {
        viewModelScope.launch {
            timerViewModel.onSessionCompleted.collect { completed ->
                if (completed) {
                    decrementSelectedTasksSessions()
                    timerViewModel.resetSessionCompleted()
                }
            }
        }
    }

    private fun decrementSelectedTasksSessions() {
        val updatedTasks = _allTasks.value.map { task ->
            if (task.taskID in _selectedTaskIds.value && task.sessions > 0) {
                val newSessions = task.sessions - 1
                task.copy(
                    sessions = newSessions,
                    completed = newSessions == 0
                )
            } else {
                task
            }
        }
        viewModelScope.launch {
            updatedTasks.forEach { repository.update(it) }
        }
        _allTasks.value = updatedTasks
    }

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


}
