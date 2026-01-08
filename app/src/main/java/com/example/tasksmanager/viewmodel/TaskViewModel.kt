package com.example.tasksmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksmanager.data.Task
import com.example.tasksmanager.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(val taskRepository: TaskRepository): ViewModel (){
    val allTasks = taskRepository.allTasks

    fun insertTask(task: Task)=viewModelScope.launch{
          taskRepository.insertTask(task)
    }
    fun updateTask(task: Task)=viewModelScope.launch{
        taskRepository.updateTask(task)
    }
    fun deleteTask(task: Task)=viewModelScope.launch{
        taskRepository.deleteTask(task)}
    fun deleteTaskById(taskId:Int)=viewModelScope.launch{
        taskRepository.deleteTaskById(taskId)
    }
    fun getTaskById(
        taskId: Int,
        onResult: (Task?) -> Unit
    ) {
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId)
            onResult(task)
        }
    }


}