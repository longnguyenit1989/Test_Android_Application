package com.example.testapplication.ui.facebook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityFacebookBinding
import com.example.testapplication.model.samplePosts

class FaceBookActivity : BaseActivity<ActivityFacebookBinding>() {

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, FaceBookActivity::class.java)
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityFacebookBinding {
        return ActivityFacebookBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = PostsAdapter(samplePosts) { post, pos ->
            val intent = Intent(this, PostDetailActivity::class.java).apply {
                putExtra("post", post)
                putExtra("position", pos)
            }
            startActivity(intent)
        }
    }
}