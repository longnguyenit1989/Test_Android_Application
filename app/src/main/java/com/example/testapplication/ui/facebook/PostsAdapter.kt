package com.example.testapplication.ui.facebook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.testapplication.R
import com.example.testapplication.databinding.ItemPostBinding
import com.example.testapplication.model.Post

class PostsAdapter(
    private val posts: List<Post>,
    private val onClick: (Post, Int) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    inner class PostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        holder.binding.apply {
            tvName.text = post.userName
            tvDate.text = post.date
            tvContent.text = post.content
            Glide.with(root.context)
                .load(post.avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .circleCrop()
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(imgAvatar)

            recyclerImages.layoutManager = GridLayoutManager(holder.itemView.context, 2)
            recyclerImages.adapter = ImageGridAdapter(post.images) {
                onClick(post, position)
            }
        }


    }

    override fun getItemCount() = posts.size
}

