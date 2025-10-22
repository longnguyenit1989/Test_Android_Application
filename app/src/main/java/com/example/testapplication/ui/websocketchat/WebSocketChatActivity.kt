package com.example.testapplication.ui.websocketchat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapplication.R
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityWebsocketChatBinding
import com.example.testapplication.model.Message
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketChatActivity : BaseActivity<ActivityWebsocketChatBinding>() {

    private lateinit var adapter: MessageAdapter
    private lateinit var client: OkHttpClient
    private var webSocket: WebSocket? = null

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, WebSocketChatActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityWebsocketChatBinding {
        return ActivityWebsocketChatBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = MessageAdapter(mutableListOf())
        binding.rvMessages.layoutManager = LinearLayoutManager(this)
        binding.rvMessages.adapter = adapter
        binding.rvMessages.layoutAnimation = AnimationUtils.loadLayoutAnimation(
            this, R.anim.layout_animation_fall_down
        )

        setupWebSocket()

        binding.btnSend.setOnClickListener {
            val msg = binding.etMessage.text.toString()
            if (msg.isNotEmpty()) {
                webSocket?.send(msg)
                adapter.addMessage(Message(msg, true))
                binding.etMessage.text.clear()
                scrollToBottom()
            }
        }
    }

    private fun setupWebSocket() {
        client = OkHttpClient()

        val request = Request.Builder()
            .url("wss://ws.ifelse.io") // Echo server, auto response
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                runOnUiThread {
                    adapter.addMessage(
                        Message(
                            "✅ ${getString(R.string.connect_websocket_open)}",
                            false
                        )
                    )
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                runOnUiThread {
                    adapter.addMessage(Message(text, false))
                    scrollToBottom()
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                runOnUiThread {
                    adapter.addMessage(
                        Message(
                            "❌ ${getString(R.string.error)}: ${t.message}",
                            false
                        )
                    )
                }
            }
        })
    }


    private fun scrollToBottom() {
        binding.rvMessages.post {
            binding.rvMessages.smoothScrollToPosition(adapter.itemCount - 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket?.close(1000, "App đóng")
        client.dispatcher.executorService.shutdown()
    }
}

