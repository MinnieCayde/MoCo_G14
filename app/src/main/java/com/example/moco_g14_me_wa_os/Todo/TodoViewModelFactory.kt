package com.example.moco_g14_me_wa_os.Todo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory

class TodoViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(TodoRepository(TodoDatabase.getDatabase(application).todoDao())) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}