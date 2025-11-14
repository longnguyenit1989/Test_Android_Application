package com.example.testapplication.ui

import android.app.Application
import com.example.testapplication.utils.ShipBookLogging

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ShipBookLogging.start(this)
    }
}