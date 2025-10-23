package com.example.testapplication.ui.tiktok

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityTiktokBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.Executors

class TikTokActivity : BaseActivity<ActivityTiktokBinding>() {

    private lateinit var adapter: TikTokAdapter
    private val executor = Executors.newSingleThreadExecutor()

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, TikTokActivity::class.java)

        private const val PLAYLIST_ID = "PLBCF2DAC6FFB574DE" // public playlist
        private const val API_GOOGLE_CLOUD_KEY = "AIzaSyAEl5ZaijPF55cj4sZ02RIXwswT_7InCgU"
    }

    override fun inflateBinding(inflater: android.view.LayoutInflater) =
        ActivityTiktokBinding.inflate(inflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

//        val videoIds = listOf(
//            "M7lc1UVf-VE",  // YouTube demo
//            "ScMzIvxBSi4"
//        )
//        adapter = TikTokAdapter(this, videoIds)
//        binding.viewPager.adapter = adapter

        fetchPlaylistVideos { videoIds ->
            if (videoIds.isEmpty()) return@fetchPlaylistVideos

            runOnUiThread {
                adapter = TikTokAdapter(this@TikTokActivity, videoIds)
                binding.viewPager.adapter = adapter
                binding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

                binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        adapter.pauseAllExcept(position)
                    }
                })
            }
        }
    }

    private fun fetchPlaylistVideos(callback: (List<String>) -> Unit) {
        executor.execute {
            try {
                val url = "https://www.googleapis.com/youtube/v3/playlistItems" +
                        "?part=snippet&maxResults=50&playlistId=$PLAYLIST_ID&key=$API_GOOGLE_CLOUD_KEY"

                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val body = response.body.string()


                val json = JSONObject(body)
                val items = json.optJSONArray("items") ?: run {
                    runOnUiThread { callback(emptyList()) }
                    return@execute
                }

                val videoIds = mutableListOf<String>()
                for (i in 0 until items.length()) {
                    val snippet = items.getJSONObject(i).getJSONObject("snippet")
                    val resourceId = snippet.getJSONObject("resourceId")
                    videoIds.add(resourceId.getString("videoId"))
                }

                runOnUiThread { callback(videoIds) }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { callback(emptyList()) }
            }
        }
    }

}
