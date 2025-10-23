package com.example.testapplication.ui.facebook

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityFacebookBinding
import com.example.testapplication.extension.beGone
import com.example.testapplication.extension.beVisible
import com.example.testapplication.model.samplePosts
import com.example.testapplication.model.samplePosts1
import com.example.testapplication.model.samplePosts2
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FaceBookActivity : BaseActivity<ActivityFacebookBinding>() {

    private lateinit var adapter: PostsAdapter
    private var currentList = samplePosts.toMutableList()
    private var isLoading = false
    private var loadStep = 0
    private var hasMoreData = true

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, FaceBookActivity::class.java)
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityFacebookBinding {
        return ActivityFacebookBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = PostsAdapter(currentList) { post, pos ->
            val intent = Intent(this, PostDetailActivity::class.java).apply {
                putExtra("post", post)
                putExtra("position", pos)
            }
            startActivity(intent)
        }

        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@FaceBookActivity)
            recyclerView.adapter = adapter

            progressBar.beGone()
            progressText.beGone()

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy <= 0) return  // just run when scroll

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisible = layoutManager.findLastVisibleItemPosition()

                    if (lastVisible == adapter.itemCount - 1) {
                        loadMorePosts()
                    }
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadMorePosts() {
        if (isLoading || !hasMoreData) return
        isLoading = true

        binding.progressBar.beVisible()
        binding.progressText.beVisible()
        binding.progressBar.progress = 0

        lifecycleScope.launch {
            for (i in 1..100) {
                delay(25)

                ObjectAnimator.ofInt(binding.progressBar, "progress", i).apply {
                    duration = 100
                    interpolator = DecelerateInterpolator()
                    start()
                }

                binding.progressText.text = "$i%"
            }

            val newPosts = when (loadStep) {
                0 -> samplePosts1
                1 -> samplePosts2
                else -> null
            }

            if (newPosts != null) {
                val updatedList = currentList.toMutableList().apply { addAll(newPosts) }
                adapter.updatePosts(updatedList)
                currentList = updatedList
                loadStep++
            } else {
                Toast.makeText(
                    this@FaceBookActivity,
                    getString(R.string.all_post_have_been_loaded),
                    Toast.LENGTH_SHORT
                ).show()
                hasMoreData = false
            }

            binding.progressBar.beGone()
            binding.progressText.beGone()
            isLoading = false
        }
    }
}

