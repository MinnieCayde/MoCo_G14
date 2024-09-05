package com.example.moco_g14_me_wa_os.Todo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class Task (
    val name: String,
    val description: String,
    var completed: Boolean = false,
    @PrimaryKey val taskID: UUID = UUID.randomUUID())