package com.example.moco_g14_me_wa_os.Timer

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class TimerViewmodel (
    val name : String,
    val completed :Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val taskId : Int = 0

)