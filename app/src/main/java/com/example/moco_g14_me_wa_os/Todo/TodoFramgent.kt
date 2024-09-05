package com.example.moco_g14_me_wa_os.Todo

import com.example.moco_g14_me_wa_os.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatImageButton
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
        val recyclerView = view.findViewById<RecyclerView>(R.id.todoListRecyclerView)
        todoAdapter = TodoAdapter()
        recyclerView.adapter = todoAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe LiveData from ViewModel
        todoViewModel.allTasks.observe(viewLifecycleOwner, Observer { tasks ->
            todoAdapter.submitList(tasks)
        })

        // Handle the "New Task" button click
        val newTaskButton = view.findViewById<AppCompatImageButton>(R.id.newTaskButton)
        newTaskButton.setOnClickListener {
            showNewTaskDialog()
        }
    }
    private fun showNewTaskDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.new_task_, null)
        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            dialogBuilder.setView(dialogView)

        val taskNameEditText = dialogView.findViewById<EditText>(R.id.name)
        val taskDescriptionEditText = dialogView.findViewById<EditText>(R.id.desc)
        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        saveButton.setOnClickListener {
            val taskName = taskNameEditText.text.toString()
            val taskDescription = taskDescriptionEditText.text.toString()

            if (taskName.isNotEmpty()) {
                val newTask = Task(name = taskName, description = taskDescription, false, UUID.randomUUID())
                todoViewModel.insert(newTask)
                alertDialog.dismiss()

                todoViewModel.insert(newTask)
            }

            alertDialog.dismiss()
        }
    }
}
