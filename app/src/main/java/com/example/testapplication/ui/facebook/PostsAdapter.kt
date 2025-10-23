package com.example.testapplication.ui.facebook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testapplication.databinding.ItemPostBinding
import com.example.testapplication.model.Post

class PostsAdapter(
    private var posts: MutableList<Post>,
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
                .circleCrop()
                .into(imgAvatar)

            recyclerImages.layoutManager = GridLayoutManager(holder.itemView.context, 2)
            recyclerImages.adapter = ImageGridAdapter(post.images) {
                onClick(post, position)
            }
        }
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<Post>) {
        val oldListCopy = posts.toList()
        val diffCallback = PostDiffCallback(oldListCopy, newPosts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        posts.clear()
        posts.addAll(newPosts)
        diffResult.dispatchUpdatesTo(this)
    }

    class PostDiffCallback(
        private val oldList: List<Post>,
        private val newList: List<Post>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}





