package com.example.testapplication.extension

import android.view.View

fun View.beInvisible() {
    visibility = View.INVISIBLE
}

fun View.beVisible() {
    visibility = View.VISIBLE
}

fun View.beGone() {
    visibility = View.GONE
}

fun View.setOnSingleClickListener(delayMillis: Int = 1000, listener: () -> Unit) {
    var pressedTime = 0L
    setOnClickListener {
        if (System.currentTimeMillis() - pressedTime < delayMillis) return@setOnClickListener
        pressedTime = System.currentTimeMillis()
        listener()
    }
}