package com.example.moco_g14_me_wa_os.Todo

import com.example.moco_g14_me_wa_os.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.UUID


class TodoFragment : Fragment() {

    private lateinit var todoViewModel: TodoViewmodel
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.todo_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel
        todoViewModel = ViewModelProvider(this).get(TodoViewmodel::class.java)

        // Setup RecyclerView and Adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.todo_recycler_view)
        todoAdapter = TodoAdapter()
        recyclerView.adapter = todoAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe LiveData from ViewModel
        todoViewModel.allTasks.observe(viewLifecycleOwner, Observer { tasks ->
            todoAdapter.submitList(tasks)
        })

        // Setup Add Button to insert new task
        val taskInput = view.findViewById<EditText>(R.id.todo_task_input)
        val addButton = view.findViewById<Button>(R.id.todo_add_button)
        addButton.setOnClickListener {
            val taskName = taskInput.text.toString()
            if (taskName.isNotEmpty()) {
                val task = Task(taskID = UUID.randomUUID(), name = taskName)  // Creating new task with input name
                todoViewModel.insert(task)
                taskInput.text.clear()  // Clear input field after adding task
            }
        }
    }
}
