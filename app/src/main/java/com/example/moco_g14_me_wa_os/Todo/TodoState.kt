package com.example.moco_g14_me_wa_os.Todo

data class TodoState(val name : String = "",
                     val completed : Boolean = false,
                     val isAdding : Boolean = false)