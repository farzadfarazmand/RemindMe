package com.github.farzadfarazmand.remindme.dao

import androidx.room.*
import com.github.farzadfarazmand.remindme.model.Task


@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: Task)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(task: Task)

    @Query("DELETE FROM task_table")
    fun deleteAll()

    @Query("SELECT * FROM task_table ORDER BY task_timestamp ASC")
    fun getAllTasks(): MutableList<Task>

}