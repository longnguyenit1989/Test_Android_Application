package com.example.testapplication.utils

import android.content.Context
import android.util.TypedValue
import android.view.View

object Converter {
    fun dpToPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density + 0.5f).toInt()
    }

    fun dpToPx(view: View, dp: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, view.resources.displayMetrics)

    fun spToPx(view: View, sp: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, view.resources.displayMetrics)
}