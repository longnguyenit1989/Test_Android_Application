package com.example.testapplication.ui.broadcast_receiver

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.testapplication.R
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityBroadcastReceiverBinding

class BroadcastReceiverActivity : BaseActivity<ActivityBroadcastReceiverBinding>() {

    private lateinit var wifiManager: WifiManager
    private var isReceiverRegistered = false

    private val broadcastReceiver = object : android.content.BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                    updateWifiStatus(wifiManager.isWifiEnabled)
                }

                BACK_TO_MAIN_ACTIVITY -> {
                    finish()
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    val filter = IntentFilter().apply {
        // Listen for Wi-Fi state changes
        addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)

        // Listen for network connectivity changes (Wi-Fi, mobile data, etc.)
        addAction(ConnectivityManager.CONNECTIVITY_ACTION)

        // Listen for airplane mode being toggled
        addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)

        // Listen for Bluetooth being turned on or off
        addAction(BluetoothAdapter.ACTION_STATE_CHANGED)

        // Listen for low battery warning
        addAction(Intent.ACTION_BATTERY_LOW)

        addAction(BACK_TO_MAIN_ACTIVITY)
    }

    companion object {
        const val BACK_TO_MAIN_ACTIVITY = "BACK_TO_MAIN_ACTIVITY"

        fun newIntent(context: Context): Intent {
            return Intent(context, BroadcastReceiverActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityBroadcastReceiverBinding {
        return ActivityBroadcastReceiverBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpWifi()
        binding.apply {
            btnGoToA.setOnClickListener {
                startActivity(ScreenAActivity.Companion.newIntent(this@BroadcastReceiverActivity))
            }
            waveView.startWave()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun setUpWifi() {
        wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

        binding.btnToggleWifi.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val panelIntent = Intent(android.provider.Settings.Panel.ACTION_WIFI)
                startActivity(panelIntent)
            } else {
                val isEnabled = wifiManager.isWifiEnabled
                wifiManager.isWifiEnabled = !isEnabled
            }
        }
    }

    private fun updateWifiStatus(isEnabled: Boolean) {
        val status = if (isEnabled) getString(R.string.is_on) else getString(R.string.is_off)
        binding.tvStatus.text = getString(R.string.wifi_status, status)
        Toast.makeText(
            this@BroadcastReceiverActivity,
            getString(R.string.wifi_status, status),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onResume() {
        super.onResume()
        registerBroadcastReceiverIfNeeded()
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerBroadcastReceiverIfNeeded() {
        if (!isReceiverRegistered) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(broadcastReceiver, filter, RECEIVER_NOT_EXPORTED)
            } else {
                registerReceiver(broadcastReceiver, filter)
            }
            isReceiverRegistered = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isReceiverRegistered) {
            unregisterReceiver(broadcastReceiver)
            isReceiverRegistered = false
        }
    }
}