package com.example.testapplication.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeHelper {
    fun formatTimeHHMM(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}