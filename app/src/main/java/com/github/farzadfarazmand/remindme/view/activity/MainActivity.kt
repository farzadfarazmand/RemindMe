package com.github.farzadfarazmand.remindme.view.activity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.github.farzadfarazmand.remindme.R
import com.github.farzadfarazmand.remindme.databinding.ActivityMainBinding
import com.github.farzadfarazmand.remindme.model.Task
import com.github.farzadfarazmand.remindme.utils.ListHolder
import com.github.farzadfarazmand.remindme.view.adapter.ColorListAdapter
import com.github.farzadfarazmand.remindme.view.adapter.TaskRecyclerListAdapter
import com.github.farzadfarazmand.remindme.viewmodel.MainViewModel
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_new_task.view.*

class MainActivity : AppCompatActivity(), TaskRecyclerListAdapter.RecyclerViewActionsListener {

    override fun onItemClicked(position: Int) {
        Toast.makeText(this, "position $position clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onItemRemoved(position: Int) {
        viewModel.deleteTask(position)
    }

    private var selectedColor = 0
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        val taskListAdapter = TaskRecyclerListAdapter(viewModel.taskListHolder, this)
        tasksRecyclerView.adapter = taskListAdapter

        viewModel.getAllTasks()

        binding.addNewTaskButton.setOnClickListener {
            val dialog: MaterialDialog = MaterialDialog(this)
                .customView(R.layout.dialog_new_task)
                .cornerRadius(10f)

            initColorsList(dialog.getCustomView().colorsList)
            dialog.show()
            dialog.getCustomView().addTaskButton.setOnClickListener {
                val taskTitle = dialog.getCustomView().taskTitle.text.toString()
                if (!TextUtils.isEmpty(taskTitle)) {
                    viewModel.insertNewTask(taskTitle, selectedColor)
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "First add a task!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.taskListHolder.observe(this,
            Observer<ListHolder<Task>> { tasksListHolder ->
                tasksListHolder?.let { tasksListHolder.applyChange(taskListAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>) }
            })

//        viewModel.getStatus().observe(this, Observer<TaskStatus> { taskStatus ->
//            when (taskStatus) {
//                TaskStatus.INSERT_SUCCESS -> Toast.makeText(this, "Task inserted!", Toast.LENGTH_SHORT).show()
//                TaskStatus.INSERT_ERROR -> Toast.makeText(this, "Task not inserted :(", Toast.LENGTH_SHORT).show()
//                TaskStatus.GET_ALL_ERROR -> Toast.makeText(this, "Cant get all task :(", Toast.LENGTH_SHORT).show()
//                TaskStatus.DELETE_SUCCESS -> Toast.makeText(this, "Cant get all task :(", Toast.LENGTH_SHORT).show()
//                else -> {
//                    //do nothing
//                }
//            }
//
//        })
    }

    private fun initColorsList(colorsList: RecyclerView) {
        colorsList.layoutManager = GridLayoutManager(this, 5)
        colorsList.setHasFixedSize(true)
        colorsList.isNestedScrollingEnabled = false
        val colors = resources.getIntArray(R.array.card_bg_color_array)
        val adapter = ColorListAdapter(colors, object : TaskBackgroundColorChangeListener {
            override fun onColorSelected(color: Int) {
                Logger.d("new color selected, %s", color)
                selectedColor = color

            }
        })
        colorsList.adapter = adapter
    }

    interface TaskBackgroundColorChangeListener {
        fun onColorSelected(color: Int)
    }

}
