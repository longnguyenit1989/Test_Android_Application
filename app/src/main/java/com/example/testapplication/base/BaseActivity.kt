package com.example.testapplication.base

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.example.testapplication.utils.LocaleHelper

abstract class BaseActivity<T : ViewBinding>  : AppCompatActivity() {

    protected lateinit var binding: T

    override fun attachBaseContext(newBase: Context) {
        val context = LocaleHelper.loadLocale(newBase)
        super.attachBaseContext(context)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateBinding(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setupWindowInsets()
    }

    private fun setupWindowInsets() {
        val rootView = findViewById<View>(R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemWindowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemWindowInsets.top, 0, systemWindowInsets.bottom)
            insets
        }
    }

    protected abstract fun inflateBinding(inflater: LayoutInflater): T
}