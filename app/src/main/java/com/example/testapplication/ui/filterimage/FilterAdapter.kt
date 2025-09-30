package com.example.testapplication.ui.filterimage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.common.FilterItem
import com.example.testapplication.databinding.ItemFilterBinding
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

class FilterAdapter(
    private val filters: List<FilterItem>,
    private val onFilterClick: (GPUImageFilter?) -> Unit
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    inner class FilterViewHolder(val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FilterItem) {
            binding.tvFilterName.text = item.name
            if (item.isSelected == true) {
                binding.tvFilterName.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.light_blue)
                )
            } else {
                binding.tvFilterName.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.grey)
                )
            }

            binding.ivPreview.setImageResource(R.drawable.ic_launcher_foreground)
            binding.root.setOnClickListener {
                onFilterClick(item.filter)
                setSelected(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun getItemCount() = filters.size

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(filters[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelected(position: Int) {
        filters.forEachIndexed { pos, item ->
            item.isSelected = pos == position
        }
        notifyDataSetChanged()
    }
}
