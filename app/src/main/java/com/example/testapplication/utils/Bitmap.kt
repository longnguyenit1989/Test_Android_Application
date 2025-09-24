package com.example.testapplication.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.util.TypedValue
import androidx.core.graphics.scale
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object Bitmap {
    fun getBitmapDescriptor(context: Context, resId: Int, sizeInDp: Int): BitmapDescriptor {
        val bitmap = BitmapFactory.decodeResource(context.resources, resId)

        val width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            sizeInDp.toFloat(),
            context.resources.displayMetrics
        ).toInt()

        val height = (bitmap.height * width) / bitmap.width

        val scaledBitmap = bitmap.scale(width, height, false)
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }
}