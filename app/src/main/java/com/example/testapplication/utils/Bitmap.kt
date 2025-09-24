package com.example.testapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.scale
import com.example.testapplication.R
import com.example.testapplication.utils.Converter.dpToPx
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import androidx.core.graphics.createBitmap

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

    fun createCustomMarker(context: Context, title: String, promo: String): BitmapDescriptor {
        val markerView = LayoutInflater.from(context).inflate(R.layout.custom_marker, null)

        markerView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val txtTitle = markerView.findViewById<TextView>(R.id.txtTitle)
        val txtPromo = markerView.findViewById<TextView>(R.id.txtPromo)
        txtTitle.text = title
        txtPromo.text = promo

        val specWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val specHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        markerView.measure(specWidth, specHeight)
        val measuredWidth = markerView.measuredWidth
        val measuredHeight = markerView.measuredHeight

        val width = if (measuredWidth > 0) measuredWidth else dpToPx(context, 80)
        val height = if (measuredHeight > 0) measuredHeight else dpToPx(context, 40)

        markerView.layout(0, 0, width, height)

        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        markerView.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}