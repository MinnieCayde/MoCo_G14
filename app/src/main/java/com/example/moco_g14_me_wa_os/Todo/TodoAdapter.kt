package com.example.moco_g14_me_wa_os.Todo

import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import com.example.moco_g14_me_wa_os.R
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import android.widget.CheckBox



class TodoAdapter: ListAdapter<Task, TodoAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            itemView.findViewById<TextView>(R.id.todo_task_name).text = task.name
            itemView.findViewById<CheckBox>(R.id.todo_task_completed).isChecked = task.completed
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.taskID == newItem.taskID
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}