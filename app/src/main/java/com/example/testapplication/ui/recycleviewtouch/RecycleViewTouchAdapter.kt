package com.example.testapplication.ui.recycleviewtouch

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.databinding.ItemViewTouchBinding
import java.util.Collections

class RecycleViewTouchAdapter(
    private val items: MutableList<String>,
    private val dragStartListener: (RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<RecycleViewTouchAdapter.ViewHolder>() {

    private var fromPosition = -1
    private var toPosition = -1

    inner class ViewHolder(val binding: ItemViewTouchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewTouchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView.text = items[position]

        holder.binding.root.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                dragStartListener(holder)
            }
            false
        }
    }

    fun onItemMove(from: Int, to: Int) {
        if (fromPosition == -1) fromPosition = from
        toPosition = to
        notifyItemMoved(from, to)
    }

    fun onItemDrop() {
        if (fromPosition != -1 && toPosition != -1 && fromPosition != toPosition) {
            Collections.swap(items, fromPosition, toPosition)
        }
        fromPosition = -1
        toPosition = -1
    }
}
