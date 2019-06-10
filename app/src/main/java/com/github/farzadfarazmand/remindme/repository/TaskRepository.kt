package com.github.farzadfarazmand.remindme.repository

import android.app.Application
import com.github.farzadfarazmand.remindme.core.DatabaseHelper
import com.github.farzadfarazmand.remindme.dao.TaskDao
import com.github.farzadfarazmand.remindme.model.Task
import io.reactivex.Completable
import io.reactivex.Flowable

class TaskRepository(application: Application) {

    private val taskDao: TaskDao = DatabaseHelper.getInstance(application).taskDao()

    fun getTaskList(): Flowable<List<Task>> {
        return taskDao.getAllTasks()
    }

    fun insertTask(task: Task): Completable {
        return taskDao.insert(task)
    }

}