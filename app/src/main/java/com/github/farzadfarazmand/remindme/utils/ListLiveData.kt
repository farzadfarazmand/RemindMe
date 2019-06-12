package com.github.farzadfarazmand.remindme.utils

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.github.farzadfarazmand.remindme.model.Task

class ListLiveData<T> : LiveData<ListHolder<T>>() {

    val size: Int
        get() {
            return value?.size() ?: -1
        }

    fun addItems(items: MutableList<T>) {
        value?.let {
            it.addItems(items)
        } ?: let {
            value = ListHolder()
            it.value?.addItems(items)
        }

        value = value
    }

    fun addItem(item: T, position: Int = value?.size() ?: 0) {
        value?.let {
            it.addItem(position, item)
        } ?: let {
            value = ListHolder()
            it.value?.addItem(position, item)
        }
        value = value

    }

    fun removeItemAt(position: Int) {
        value?.let {
            it.removeItemAt(position)
        } ?: let {
            value = ListHolder()
            it.value?.removeItemAt(position)
        }
        value = value
    }


    fun setItem(position: Int, item: T) {
        value?.let {
            it.setItem(position, item)
        } ?: let {
            it.value?.setItem(position, item)
        }
        value = value
    }

    operator fun get(position: Int): T? {
        return value?.getItem(position)
    }

}

data class ListHolder<T>(private val list: MutableList<T> = mutableListOf()) {
    var indexChanged: Int = -1
    private var updateType: UpdateType? = null

    fun getItem(position: Int): T {
        return list[position]
    }

    fun getItemPosition(item: Task): Int {
        list.forEachIndexed { index, task ->
            if (task == item)
                return index
        }
        return -1
    }

    fun addItems(items: MutableList<T>) {
        list.addAll(items)
        updateType = UpdateType.ADD_ALL
    }

    fun addItem(position: Int, item: T) {
        list.add(position, item)
        indexChanged = position
        updateType = UpdateType.INSERT
    }

    fun removeItemAt(position: Int) {
        val item = list[position]
        list.remove(item)
        indexChanged = position
        updateType = UpdateType.REMOVE
    }

    fun setItem(position: Int, item: T) {
        list[position] = item
        indexChanged = position
        updateType = UpdateType.CHANGE
    }

    fun size(): Int {
        return list.size
    }

    fun applyChange(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        updateType?.notifyChange(adapter, indexChanged)
    }

    private enum class UpdateType {
        ADD_ALL {
            override fun notifyChange(
                adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
                indexChanged: Int
            ) = adapter.notifyDataSetChanged()
        },
        INSERT {
            override fun notifyChange(
                adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
                indexChanged: Int
            ) = adapter.notifyItemInserted(indexChanged)
        },
        REMOVE {
            override fun notifyChange(
                adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
                indexChanged: Int
            ) = adapter.notifyItemRemoved(indexChanged)
        },
        CHANGE {
            override fun notifyChange(
                adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
                indexChanged: Int
            ) = adapter.notifyItemChanged(indexChanged)
        };

        abstract fun notifyChange(
            adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
            indexChanged: Int = 0
        )
    }
}