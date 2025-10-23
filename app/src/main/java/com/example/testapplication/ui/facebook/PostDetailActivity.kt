package com.example.testapplication.ui.facebook

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.testapplication.R
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityPostDetailBinding
import com.example.testapplication.model.Post

class PostDetailActivity : BaseActivity<ActivityPostDetailBinding>() {

    private lateinit var imagesAdapter: ImagesAdapter

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, PostDetailActivity::class.java)
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityPostDetailBinding {
        return ActivityPostDetailBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val post: Post? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("post", Post::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("post")
        }

        binding.username.text = post?.userName
        binding.content.text = post?.content

        Glide.with(this@PostDetailActivity)
            .load(post?.avatarUrl)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .circleCrop()
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .into(binding.avatar)

        imagesAdapter = ImagesAdapter(post?.images ?: mutableListOf()) {}

        binding.recycleViewImage.adapter = imagesAdapter
        binding.recycleViewImage.layoutManager = LinearLayoutManager(this@PostDetailActivity)
    }
}