package com.example.testapplication.ui.downloadimage

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import com.example.testapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class DownloadForegroundService : Service() {

    companion object {
        const val EXTRA_URL = "extra_url"
        const val EXTRA_FILE_PATH = "extra_file_path"
        const val ACTION_DOWNLOAD_COMPLETE = "com.example.downloadservice.DOWNLOAD_COMPLETE"
        const val ACTION_DOWNLOAD_PROGRESS = "com.example.downloadservice.DOWNLOAD_PROGRESS"
        const val KEY_PROGRESS = "key_progress"
        private const val CHANNEL_ID = "download_channel"
        private const val NOTIF_ID = 1
    }

    private val client = OkHttpClient()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra(EXTRA_URL) ?: return START_NOT_STICKY
        createNotificationChannel()
        startForeground(NOTIF_ID, buildNotification(getString(R.string.downloading)))

        CoroutineScope(Dispatchers.IO).launch {
            val filePath = downloadFile(url)

            sendBroadcast(Intent(ACTION_DOWNLOAD_COMPLETE).apply {
                putExtra(EXTRA_FILE_PATH, filePath)
            })

            stopForeground(STOP_FOREGROUND_DETACH)
            stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun downloadFile(url: String): String? {
        return try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val body = response.body ?: return null
                val totalBytes = body.contentLength()
                val inputStream = body.byteStream()

                val fileName = "${System.currentTimeMillis()}_${url.substringAfterLast("/")}"

                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES + "/DownloadedImages"
                    )
                }

                val resolver = contentResolver
                val uri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                val outputStream = uri?.let { resolver.openOutputStream(it) } ?: return null

                val buffer = ByteArray(8192)
                var bytesRead: Int
                var downloaded = 0L
                var lastProgress = 0

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    downloaded += bytesRead

                    if (totalBytes > 0) {
                        val progress = ((downloaded * 100) / totalBytes).toInt()
                        if (progress != lastProgress) {
                            lastProgress = progress
                            sendBroadcast(Intent(ACTION_DOWNLOAD_PROGRESS).apply {
                                putExtra(KEY_PROGRESS, progress)
                            })
                        }
                    }
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()

                uri.toString()

            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Download Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun buildNotification(text: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Tải ảnh")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_download)
            .setOngoing(true)
            .build()
    }

    override fun onBind(intent: Intent?) = null
}



