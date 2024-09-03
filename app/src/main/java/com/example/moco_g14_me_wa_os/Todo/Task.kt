package com.example.moco_g14_me_wa_os.Todo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task (val name: String,
    val completed: Boolean = false,
    @PrimaryKey(autoGenerate = true),
    val taskID: Int = 0)