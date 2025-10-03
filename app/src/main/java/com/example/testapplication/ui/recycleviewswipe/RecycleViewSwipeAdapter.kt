package com.example.testapplication.ui.recycleviewswipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.databinding.ItemViewSwipeBinding

class RecycleViewSwipeAdapter(
    private val items: MutableList<String>,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<RecycleViewSwipeAdapter.SwipeViewHolder>() {

    inner class SwipeViewHolder(val binding: ItemViewSwipeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeViewHolder {
        val binding = ItemViewSwipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SwipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SwipeViewHolder, position: Int) {
        val item = items[position]
        holder.binding.textView.text = item
        holder.binding.btnDelete.setOnClickListener {
            onDelete(holder.adapterPosition)
        }
    }

    override fun getItemCount() = items.size

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}

