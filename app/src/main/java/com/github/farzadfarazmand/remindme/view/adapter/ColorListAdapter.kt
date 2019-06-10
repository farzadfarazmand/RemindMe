package com.github.farzadfarazmand.remindme.view.adapter

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.farzadfarazmand.remindme.R
import com.github.farzadfarazmand.remindme.view.activity.MainActivity
import kotlinx.android.synthetic.main.row_color_list.view.*

class ColorListAdapter(
    private val items: IntArray,
    private val colorChangeListener: MainActivity.TaskBackgroundColorChangeListener
) : RecyclerView.Adapter<ColorListAdapter.ColorViewHolder>() {

    var inflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.context)

        return ColorViewHolder(inflater!!.inflate(R.layout.row_color_list, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bindViews(items[position], position, colorChangeListener)

    }

    private var checkedPosition = 0

    inner class ColorViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {

        private val colorShape: View = rootView.colorShape
        private val checkIcon: View = rootView.checkIcon

        fun bindViews(color: Int, position: Int, colorChangeListener: MainActivity.TaskBackgroundColorChangeListener) {
            //set color
            val bgShape = colorShape.background as GradientDrawable
            bgShape.setColor(color)

            if (position == checkedPosition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    colorShape.elevation = 5f
                }
                checkIcon.visibility = View.VISIBLE
            } else {
                checkIcon.visibility = View.INVISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    colorShape.elevation = 2f
                }
            }

            rootView.setOnClickListener {
                if (checkedPosition != layoutPosition) {
                    //remove check
                    val lastCheckedPosition = checkedPosition
                    checkedPosition = layoutPosition
                    //update last position
                    notifyItemChanged(lastCheckedPosition)
                    //set new check position and show check
                    notifyItemChanged(checkedPosition)
                    colorChangeListener.onColorSelected(items[checkedPosition])
                }
            }
        }
    }

}