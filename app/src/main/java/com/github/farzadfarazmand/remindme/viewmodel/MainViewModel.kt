package com.github.farzadfarazmand.remindme.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.github.farzadfarazmand.remindme.model.Task
import com.github.farzadfarazmand.remindme.repository.TaskRepository
import com.github.farzadfarazmand.remindme.utils.ListLiveData
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel(context: Application) : AndroidViewModel(context) {

    private val compositeDisposable = CompositeDisposable()
    private val taskRepository = TaskRepository(context)

    var taskListHolder = ListLiveData<Task>()

    fun getAllTasks() {
        Logger.d("MainViewModel, getAll Task called")
        compositeDisposable.add(
            taskRepository.getTaskList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<MutableList<Task>>() {
                    override fun onComplete() {
                        Logger.d("getAll Task, onComplete")
                    }

                    override fun onNext(t: MutableList<Task>) {
                        Logger.d("getAll Task, On next")
                        taskListHolder.addItems(t)
                        dispose() //because each time table change , it called onNext
                    }

                    override fun onError(e: Throwable) {
                        Logger.e("getAll Task, error => %s", e.message)
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
                        Logger.d("MainViewModel, insert Task On next")
                        taskListHolder.addItem(task, 0)

                    }

                    override fun onError(e: Throwable) {
                        Logger.e("MainViewModel, insert error => %s", e.message)
                    }
                })

        )
    }

    fun deleteTask(index:Int) {
        taskListHolder[index]?.let {
            compositeDisposable.add(
                taskRepository.deleteTask(it)
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
    }

    override fun onCleared() {
        super.onCleared()

        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()

    }

}