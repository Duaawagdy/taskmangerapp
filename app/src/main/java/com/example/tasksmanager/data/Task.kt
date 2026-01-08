package com.example.tasksmanager.data

import java.util.Date
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Long=1,
    val isCompleted: Boolean = false,
    val  createdAt: Long =Date().time
){
    fun getPriorityColor(): Long {
        return when (priority) {

            2L -> 0xFFFFB74D // Amber for medium priority
            3L -> 0xFFFF5252 // Red for high priority
            else -> 0xFF4CAF50 // Default to green
        }
    }
    fun getPriorityText(): String {
        return when (priority) {
            2L -> "Medium"
            3L -> "High"
            else -> "Low"
        }
    }
}
