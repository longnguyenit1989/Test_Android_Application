package com.example.testapplication.ui.facebook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.testapplication.R
import com.example.testapplication.databinding.ItemImageBinding

class ImagesAdapter(
    private val images: List<String>,
    private val onClick: (position: Int) -> Unit
) : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url = images[position]
        val smallestCornerRadius =
            holder.binding.root.resources.getDimension(R.dimen.smallest_corner_radius).toInt()

        Glide.with(holder.itemView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .transform(
                        CenterCrop(),
                        RoundedCorners(smallestCornerRadius)
                    )
                    .placeholder(R.drawable.gray_placeholder)
                    .error(R.drawable.gray_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(holder.binding.img)

        holder.binding.img.setOnClickListener { onClick(position) }
    }

    override fun getItemCount() = images.size
}

