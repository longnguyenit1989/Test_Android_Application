package com.example.testapplication.ui.tiktok

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.databinding.ItemVideoPageBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class TikTokAdapter(
    private val context: Context,
    private val videoIds: List<String>
) : RecyclerView.Adapter<TikTokAdapter.VideoViewHolder>() {

    private val players = mutableListOf<YouTubePlayer>()

    inner class VideoViewHolder(val binding: ItemVideoPageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var youTubePlayer: YouTubePlayer? = null
        private var isMuted = false
        private var isPlaying = true
        private var isLiked = false

        fun bind(videoId: String) {
            val lifecycleOwner = context as? LifecycleOwner
                ?: throw IllegalStateException("Context không phải LifecycleOwner")

            lifecycleOwner.lifecycle.addObserver(binding.youtubePlayerView)

            binding.youtubePlayerView.enableAutomaticInitialization = true

            binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    this@VideoViewHolder.youTubePlayer = youTubePlayer
                    if (!players.contains(youTubePlayer)) players.add(youTubePlayer)

                    youTubePlayer.loadVideo(videoId, 0f)
                    youTubePlayer.play()
                    isPlaying = true
                }
            })

            binding.btnPlayPause.setOnClickListener {
                youTubePlayer?.let { player ->
                    if (isPlaying) {
                        player.pause()
                        binding.btnPlayPause.setImageResource(R.drawable.ic_media_play)
                    } else {
                        player.play()
                        binding.btnPlayPause.setImageResource(R.drawable.ic_media_pause)
                    }
                    isPlaying = !isPlaying
                }
            }

            binding.btnVolume.setOnClickListener {
                youTubePlayer?.let { player ->
                    if (isMuted) {
                        player.unMute()
                        binding.btnVolume.setImageResource(R.drawable.ic_volume_on)
                    } else {
                        player.mute()
                        binding.btnVolume.setImageResource(R.drawable.ic_volume_off)
                    }
                    isMuted = !isMuted
                }
            }

            binding.btnLike.setOnClickListener {
                isLiked = !isLiked
                val color = if (isLiked) R.color.red else R.color.white
                binding.btnLike.setColorFilter(ContextCompat.getColor(context, color))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videoIds[position])
    }

    override fun getItemCount() = videoIds.size

    fun pauseAllExcept(currentPosition: Int) {
        players.forEachIndexed { index, player ->
            if (index != currentPosition) player.pause()
        }
    }
}
