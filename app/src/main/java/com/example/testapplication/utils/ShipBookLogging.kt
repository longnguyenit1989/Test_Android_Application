package com.example.testapplication.utils

import android.app.Application
import io.shipbook.shipbooksdk.Log
import io.shipbook.shipbooksdk.ShipBook

object ShipBookLogging {

  private var isStarted = false


  fun start(application: Application) {

    if (isStarted) {
      return
    }
    ShipBook.start(application, "68bff15f6cf3450011653f4b", "9c7465b413cc29a0e8f5c6f7e5d35b33")
    isStarted = true
  }

  fun logWithThreadName(tag: String, message: String, logType: ShipBookLogType = ShipBookLogType.INFO) {

    val messageLog = "Thread[${Thread.currentThread().name}] " + message
    log(tag, messageLog, logType)
  }

  fun log(tag: String, message: String, logType: ShipBookLogType = ShipBookLogType.INFO) {
    if (!isStarted) {
      return
    }

    when (logType) {
      ShipBookLogType.INFO -> Log.i(tag, message)
      ShipBookLogType.WARNING -> Log.w(tag, message)
      ShipBookLogType.ERROR -> Log.e(tag, message)
    }
  }
}

enum class ShipBookLogType {
  INFO, WARNING, ERROR
}
