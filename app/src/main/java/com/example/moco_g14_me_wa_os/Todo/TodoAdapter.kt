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
import android.widget.ImageButton
import androidx.core.content.ContextCompat


class TodoAdapter: ListAdapter<Task, TodoAdapter.TodoViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            val taskNameTextView = itemView.findViewById<TextView>(R.id.todo_name)
            val completeButton = itemView.findViewById<ImageButton>(R.id.completeButton)

            completeButton.setImageResource(if (task.completed) R.drawable.checked_24 else R.drawable.unchecked_24)
            completeButton.setColorFilter(
                ContextCompat.getColor(
                    itemView.context,
                    if (task.completed) R.color.green else R.color.black
                )
            )
            taskNameTextView.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    if (task.completed) R.color.green else R.color.black
                )
            )

            //set click listener
            completeButton.setOnClickListener {
                task.completed = !task.completed

                completeButton.setImageResource(if (task.completed) R.drawable.checked_24 else R.drawable.unchecked_24)
                completeButton.setColorFilter(
                    ContextCompat.getColor(
                        itemView.context,
                        if (task.completed) R.color.green else R.color.black
                    )
                )
                taskNameTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        if (task.completed) R.color.green else R.color.black
                    )
                )
            }
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