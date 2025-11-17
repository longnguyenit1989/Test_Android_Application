package com.example.testapplication.ui.recycleviewtouch

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.databinding.ItemViewTouchHorizontalBinding
import java.util.Collections
import android.view.LayoutInflater

class RecycleViewTouchHorizontalAdapter(
    private val items: MutableList<String>,
    private val dragStart: (RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<RecycleViewTouchHorizontalAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemViewTouchHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewTouchHorizontalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView.text = items[position]

        holder.binding.dragHandle.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                dragStart(holder)
            }
            false
        }
    }

    fun onItemMove(from: Int, to: Int) {
        Collections.swap(items, from, to)
        notifyItemMoved(from, to)
    }
}

