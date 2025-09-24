package com.example.testapplication.utils

import android.content.Context

object Converter {
    fun dpToPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density + 0.5f).toInt()
    }
}