package com.github.farzadfarazmand.remindme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.github.farzadfarazmand.remindme.model.Task
import com.github.farzadfarazmand.remindme.repository.TaskRepository
import com.github.farzadfarazmand.remindme.status.TaskStatus
import com.github.farzadfarazmand.remindme.utils.ListLiveData
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

class MainViewModel(context: Application) : AndroidViewModel(context) {

    private val compositeDisposable = CompositeDisposable()
    private val taskRepository = TaskRepository(context)

    var taskListHolder = ListLiveData<Task>()

    fun getAllTasks() {
        compositeDisposable.add(
            taskRepository.getTaskList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<MutableList<Task>>() {
                    override fun onComplete() {
                        Logger.d("MainViewModel, onComplete")
                    }

                    override fun onNext(t: MutableList<Task>) {
                        taskListHolder.addItems(t)
                    }

                    override fun onError(e: Throwable) {
                        Logger.e("MainViewModel, error => %s", e.message)

                    }
                })
        )

    }

    fun insertNewTask(title: String, color: Int) {
        val task = Task(title, color, System.currentTimeMillis())
        compositeDisposable.add(
            taskRepository.insertTask(task)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        taskListHolder.addItem(task, 0)

                    }

                    override fun onError(e: Throwable) {
                        Logger.e("MainViewModel, insert error => %s", e.message)
                    }
                })

        )
    }

    fun deleteTask(task: Task, index:Int) {
        compositeDisposable.add(
            taskRepository.deleteTask(task)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        taskListHolder.removeItemAt(index)
                    }

                    override fun onError(e: Throwable) {
                        Logger.e("MainViewModel, delete error => %s", e.message)
                    }
                })
        )

    }

    override fun onCleared() {
        super.onCleared()

        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()

    }

}