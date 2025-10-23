package com.example.testapplication.ui.facebook

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapplication.databinding.ItemImageGridBinding
import com.example.testapplication.extension.beGone
import com.example.testapplication.extension.beVisible

class ImageGridAdapter(
    private val images: List<String>,
    private val onClick: (position: Int) -> Unit
) : RecyclerView.Adapter<ImageGridAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemImageGridBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = images[position]
        Glide.with(holder.binding.imgPhoto.context)
            .load(imageUrl)
            .into(holder.binding.imgPhoto)

        if (position == 3 && images.size > 4) {
            val remain = images.size - 4
            holder.binding.tvOverlay.text = "+$remain"
            holder.binding.tvOverlay.beVisible()
        } else {
            holder.binding.tvOverlay.beGone()
        }

        holder.itemView.setOnClickListener { onClick(position) }
    }

    override fun getItemCount(): Int = if (images.size > 4) 4 else images.size
}
