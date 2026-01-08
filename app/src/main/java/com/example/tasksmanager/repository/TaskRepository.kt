package com.example.tasksmanager.repository

import androidx.lifecycle.LiveData
import com.example.tasksmanager.data.Task
import com.example.tasksmanager.data.TaskDao

class TaskRepository (private val taskDao: TaskDao){
val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()
suspend fun insertTask(task: Task): Long {
    return taskDao.insertTask(task)}
    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }
    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }
    suspend fun deleteTaskById(taskId:Int){
        taskDao.deleteTaskById(taskId)
    }
    suspend fun getTaskById(taskId: Int): Task? {
        return taskDao.getTaskById(taskId)}
}