package com.example.testapplication.ui.broadcast_receiver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityScreenABinding

class ScreenAActivity : BaseActivity<ActivityScreenABinding>() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ScreenAActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityScreenABinding {
        return ActivityScreenABinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.apply {
            btnBack.setOnClickListener { this@ScreenAActivity.finish() }
            btnBackToMain.setOnClickListener {
                sendBackToMainBroadcast()
                this@ScreenAActivity.finish()
            }
        }
    }

    private fun sendBackToMainBroadcast() {
        val intent = Intent()
        intent.action = BroadcastReceiverActivity.BACK_TO_MAIN_ACTIVITY
        intent.setPackage(this@ScreenAActivity.packageName)
        sendBroadcast(intent)
    }
}