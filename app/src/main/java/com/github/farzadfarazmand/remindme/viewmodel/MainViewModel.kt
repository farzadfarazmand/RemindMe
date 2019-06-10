package com.github.farzadfarazmand.remindme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.farzadfarazmand.remindme.model.Task
import com.github.farzadfarazmand.remindme.repository.TaskRepository
import com.github.farzadfarazmand.remindme.status.SingleLiveEvent
import com.github.farzadfarazmand.remindme.status.TaskStatus
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

class MainViewModel(context: Application) : AndroidViewModel(context) {

    private val status = SingleLiveEvent<TaskStatus>()
    private val compositeDisposable = CompositeDisposable()
    private val taskRepository = TaskRepository(context)

    var taskList = MutableLiveData<List<Task>>()
    var newTask = MutableLiveData<Task>()

    fun getAllTasks() {
        compositeDisposable.add(
            taskRepository.getTaskList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<Task>>() {
                    override fun onComplete() {
                        Logger.d("MainViewModel, onComplete")
                        status.value = TaskStatus.GET_ALL_SUCCESS
                    }

                    override fun onNext(t: List<Task>) {
                        taskList.value = t
                    }

                    override fun onError(e: Throwable) {
                        Logger.e("MainViewModel, error => %s", e.message)
                        status.value = TaskStatus.GET_ALL_ERROR

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
                        newTask.value = task
                        status.value = TaskStatus.INSERT_SUCCESS

                    }

                    override fun onError(e: Throwable) {
                        Logger.e("MainViewModel, insert error => %s", e.message)
                        status.value = TaskStatus.INSERT_ERROR
                    }
                })

        )
    }

    fun getStatus(): LiveData<TaskStatus> {
        return status
    }

    override fun onCleared() {
        super.onCleared()

        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()

    }

}