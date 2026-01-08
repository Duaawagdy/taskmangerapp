package com.example.tasksmanager.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.tasksmanager.R
import com.example.tasksmanager.data.Task
import com.example.tasksmanager.data.TaskDatabase
import com.example.tasksmanager.databinding.ActivityMainBinding
import com.example.tasksmanager.databinding.DialogTaskBinding
import com.example.tasksmanager.repository.TaskRepository
import com.example.tasksmanager.ui.adaptor.TaskAdapter
import com.example.tasksmanager.viewmodel.TaskViewModel
import com.example.tasksmanager.viewmodel.TaskViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupRecyclerView()
        setupClickListeners()
        observeTasks()
    }

    private fun setupViewModel(){
        val database = TaskDatabase.getDatabase(this)
        val repository = TaskRepository(database.taskDao())

        val viewModelFactory = TaskViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory)[TaskViewModel::class.java]
    }

    private fun setupRecyclerView(){
        adapter = TaskAdapter(
            onItemClick = {task ->
                showTaskDialog(task)
            },
            onLongItemClick = {task ->
                showDeleteConfirmation(task)
            }
        )
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = adapter
    }

    private fun showDeleteConfirmation(task: Task) {
        AlertDialog.Builder(this)
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete '${task.title}'?")
            .setPositiveButton("Delete"){_,_->
                viewModel.deleteTask(task)
            }
            .setNegativeButton("Cancel", null)
            .show()


    }

    private fun observeTasks(){
        viewModel.allTasks.observe(this){tasks ->
            adapter.updateTasks(tasks)
            if(tasks.isEmpty()){
                binding.rvTasks.visibility = View.GONE
                binding.rvTasks.visibility = View.VISIBLE
            }else{
                binding.rvTasks.visibility = View.VISIBLE
                binding.emptyState.visibility = View.GONE
            }
        }
    }

    private fun setupClickListeners(){
        binding.btnAddTask.setOnClickListener{
            showTaskDialog()
        }
    }

    private fun showTaskDialog(existingTask: Task? = null) {
        //get dialog binding

        val dialogBinding = DialogTaskBinding.inflate(layoutInflater)
        //create alert dialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()
//send existing data to dialog if editing
        existingTask?.let { task ->
            dialogBinding.etTitle.setText(task.title)
            dialogBinding.etDescription.setText(task.description)
            dialogBinding.cbCompleted.isChecked = task.isCompleted

            when(task.priority){
                1L -> dialogBinding.rbLow.isChecked = true
                2L -> dialogBinding.rbMedium.isChecked = true
                3L -> dialogBinding.rbHigh.isChecked = true
            }

            dialogBinding.btnSave.text = "Update"


        }
//         handle dialog buttons
        dialogBinding.btnCancel.setOnClickListener{
            dialog.dismiss()
        }

        dialogBinding.btnSave.setOnClickListener{
            val title = dialogBinding.etTitle.text.toString().trim()
            val description = dialogBinding.etDescription.text.toString().trim()
            if (title.isEmpty()){
                dialogBinding.etTitle.error = "title is required"
                return@setOnClickListener
            }
            val priority = when(dialogBinding.rgPriority.checkedRadioButtonId){
                R.id.rbLow-> 1
                R.id.rbMedium-> 2
                R.id.rbHigh-> 3
                else -> 1
            }
            val isCompleted = dialogBinding.cbCompleted.isChecked

            val task = existingTask?.copy(
                title = title,
                description = description,
                priority = priority.toLong(),
                isCompleted = isCompleted
            ) ?: Task(

                title = title,
                description = description,
                priority = priority.toLong(),
                isCompleted = isCompleted
            )

            if (existingTask !=null){
                viewModel.updateTask(task)
            }else{
                viewModel.insertTask(task)
            }

            dialog.dismiss()

        }
        // show dialog
        dialog.show()

    }





}