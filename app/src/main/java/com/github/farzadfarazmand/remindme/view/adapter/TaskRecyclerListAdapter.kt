package com.github.farzadfarazmand.remindme.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.farzadfarazmand.remindme.databinding.RowTaskListBinding
import com.github.farzadfarazmand.remindme.model.Task

class TaskRecyclerListAdapter(var items: ArrayList<Task>) :
    RecyclerView.Adapter<TaskRecyclerListAdapter.TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TaskViewHolder(RowTaskListBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class TaskViewHolder(private val binding: RowTaskListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task) {
            binding.task = item
            binding.executePendingBindings()
        }

    }

    fun addItems(newItems: List<Task>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addItem(newItem: Task) {
        items.add(newItem)
        notifyItemInserted(0)
    }
}