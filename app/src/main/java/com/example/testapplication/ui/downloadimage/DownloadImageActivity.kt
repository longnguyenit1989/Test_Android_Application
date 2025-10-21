package com.example.testapplication.ui.downloadimage

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.net.toUri
import com.example.testapplication.R
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityDownloadImageBinding
import com.example.testapplication.extension.beGone
import com.example.testapplication.extension.beVisible
import com.example.testapplication.ui.downloadimage.DownloadForegroundService.Companion.KEY_PROGRESS

class DownloadImageActivity : BaseActivity<ActivityDownloadImageBinding>() {

    companion object {
        private val RECEIVER_EXPORTED =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Context.RECEIVER_EXPORTED
            else 0

        fun newIntent(context: Context): Intent {
            return Intent(context, DownloadImageActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityDownloadImageBinding {
        return ActivityDownloadImageBinding.inflate(inflater)
    }

    private val downloadReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                DownloadForegroundService.ACTION_DOWNLOAD_COMPLETE -> {
                    val filePath = intent.getStringExtra(DownloadForegroundService.EXTRA_FILE_PATH)
                    binding.progressBar.beGone()
                    binding.tvProgress.beGone()
                    if (filePath != null) {
                        val imageUri = filePath.toUri()
                        binding.imgResult.setImageURI(imageUri)
                        Toast.makeText(
                            this@DownloadImageActivity,
                            getString(R.string.download_success_image),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@DownloadImageActivity,
                            getString(R.string.cannot_download_image),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                DownloadForegroundService.ACTION_DOWNLOAD_PROGRESS -> {
                    val progress = intent.getIntExtra(KEY_PROGRESS, 0)
                    binding.progressBar.progress = progress
                    binding.tvProgress.text = "$progress %"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnDownload.setOnClickListener {
            val url = binding.etUrl.text.toString().trim()
            if (url.isNotEmpty()) {
                binding.progressBar.beVisible()
                binding.tvProgress.beVisible()
                binding.progressBar.progress = 0
                binding.tvProgress.text = "0 %"

                val intent = Intent(this, DownloadForegroundService::class.java).apply {
                    putExtra(DownloadForegroundService.EXTRA_URL, url)
                }
                startForegroundService(intent)
            } else {
                Toast.makeText(this, getString(R.string.insert_url_image), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter().apply {
            addAction(DownloadForegroundService.ACTION_DOWNLOAD_COMPLETE)
            addAction(DownloadForegroundService.ACTION_DOWNLOAD_PROGRESS)
        }
        registerReceiver(downloadReceiver, filter, RECEIVER_EXPORTED)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(downloadReceiver)
    }
}
