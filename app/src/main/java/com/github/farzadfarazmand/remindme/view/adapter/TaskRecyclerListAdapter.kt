package com.github.farzadfarazmand.remindme.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.farzadfarazmand.remindme.databinding.RowTaskListBinding
import com.github.farzadfarazmand.remindme.model.Task
import com.github.farzadfarazmand.remindme.utils.ListLiveData

class TaskRecyclerListAdapter(var item: ListLiveData<Task>) :
    RecyclerView.Adapter<TaskRecyclerListAdapter.TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(RowTaskListBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(item[position])
    }

    class TaskViewHolder(private val binding: RowTaskListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task?) {
            item?.let {
                binding.task = it
                binding.executePendingBindings()
            }
        }

    }
}