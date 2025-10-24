package com.example.testapplication.ui.facebook

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.testapplication.R
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityFacebookBinding
import com.example.testapplication.extension.beGone
import com.example.testapplication.extension.beVisible
import com.example.testapplication.model.Post
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

//                val offset = recyclerView.computeVerticalScrollOffset() // number of pixels you scrolled down
//                val extent = recyclerView.computeVerticalScrollExtent() // current display height
//                val range = recyclerView.computeVerticalScrollRange() // total scrollable content height
//                if (offset + extent >= range - 200) { // 200px from bottom up
//                    loadMorePosts()
//                }

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisible = layoutManager.findLastVisibleItemPosition()
                    if (lastVisible == adapter.itemCount - 1) {
                        loadMorePosts()
                    }
                }
            })

            // hide default swipeRefresh
            swipeRefresh.setColorSchemeColors(Color.TRANSPARENT)
            swipeRefresh.setProgressBackgroundColorSchemeColor(Color.TRANSPARENT)
            swipeRefresh.setProgressViewOffset(false, -200, -200) // push default drawable out of screen

            swipeRefresh.setOnRefreshListener {
                handleCustomRefresh()

                hasMoreData = true
                isLoading = false
                loadStep = 0
                adapter.updatePosts(mutableListOf())
                currentList = samplePosts.toMutableList()
                adapter.updatePosts(currentList)
            }
        }
    }

    private fun handleCustomRefresh() {
        binding.apply {
            imgCustomRefresh.beVisible()

            val rotation = ObjectAnimator.ofFloat(imgCustomRefresh, View.ROTATION, 0f, 360f)
            rotation.duration = 700
            rotation.repeatCount = ValueAnimator.INFINITE
            rotation.interpolator = LinearInterpolator()
            rotation.start()

            lifecycleScope.launch {
                delay(500)
                swipeRefresh.isRefreshing = false
                rotation.cancel()
                imgCustomRefresh.beGone()
            }
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


