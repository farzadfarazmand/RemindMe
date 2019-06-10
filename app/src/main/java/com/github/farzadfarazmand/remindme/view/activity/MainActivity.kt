package com.github.farzadfarazmand.remindme.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.farzadfarazmand.remindme.R
import com.github.farzadfarazmand.remindme.databinding.ActivityMainBinding
import com.github.farzadfarazmand.remindme.model.Task
import com.github.farzadfarazmand.remindme.status.TaskStatus
import com.github.farzadfarazmand.remindme.view.adapter.TaskRecyclerListAdapter
import com.github.farzadfarazmand.remindme.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        val taskListAdapter = TaskRecyclerListAdapter(arrayListOf())
        tasksRecyclerView.adapter = taskListAdapter

        viewModel.getAllTasks()

        binding.addNewTaskButton.setOnClickListener {
            viewModel.insertNewTask("This a new task I want to dooooooooooo", R.color.card_bg_9)
        }

        viewModel.taskList.observe(this,
            Observer<List<Task>> { tasks ->
                tasks?.let { taskListAdapter.addItems(it) }
            })

        viewModel.newTask.observe(this,
            Observer<Task> { task ->
                task?.let { taskListAdapter.addItem(task) }
            })

        viewModel.getStatus().observe(this, Observer<TaskStatus> { taskStatus ->
            when (taskStatus) {
                TaskStatus.INSERT_SUCCESS -> Toast.makeText(this, "Task inserted!", Toast.LENGTH_SHORT).show()
                TaskStatus.INSERT_ERROR -> Toast.makeText(this, "Task not inserted :(", Toast.LENGTH_SHORT).show()
                TaskStatus.GET_ALL_ERROR -> Toast.makeText(this, "Cant get all task :(", Toast.LENGTH_SHORT).show()
                else -> {
                    //do nothing
                }
            }

        })
    }

}
