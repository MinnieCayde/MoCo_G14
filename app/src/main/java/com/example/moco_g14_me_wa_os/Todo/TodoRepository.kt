package com.example.moco_g14_me_wa_os.Todo

import androidx.lifecycle.LiveData


class TodoRepository(private val todoDao: TodoDao) {

    val allTasks: LiveData<List<Task>> = todoDao.getAllTasks()

    suspend fun insert(task: Task) {
        todoDao.insertTask(task)
    }

    suspend fun update(task: Task) {
        todoDao.updateTask(task)
    }

    suspend fun delete(task: Task) {
        todoDao.deleteTask(task)
    }

    suspend fun deleteTaskById(taskId: Int) {
        todoDao.deleteTaskById(taskId)
    }
}