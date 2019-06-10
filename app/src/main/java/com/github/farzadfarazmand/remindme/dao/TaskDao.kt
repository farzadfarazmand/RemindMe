package com.github.farzadfarazmand.remindme.dao

import androidx.room.*
import com.github.farzadfarazmand.remindme.model.Task
import io.reactivex.Completable
import io.reactivex.Flowable


@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task: Task): Completable

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(task: Task): Completable

    @Query("SELECT * FROM task_table ORDER BY task_timestamp ASC")
    fun getAllTasks(): Flowable<List<Task>>

}