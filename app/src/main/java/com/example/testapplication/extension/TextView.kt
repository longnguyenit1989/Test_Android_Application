package com.example.testapplication.extension

import android.util.TypedValue
import android.widget.TextView

fun TextView.setTextSizeFloat(float: Float) {
    this.setTextSize(TypedValue.COMPLEX_UNIT_PX, float)
}