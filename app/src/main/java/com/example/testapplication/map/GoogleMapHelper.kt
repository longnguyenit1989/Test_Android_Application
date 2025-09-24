package com.example.testapplication.map

import android.content.Context
import com.example.testapplication.R
import com.example.testapplication.utils.Bitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapHelper(val context: Context, val map: GoogleMap): GoogleMap.OnMarkerClickListener {

    fun focus(locations: List<LatLng>,) {
        val builder = LatLngBounds.Builder()

        locations.forEach { location ->
            builder.include(location)
            val markerOptions = MarkerOptions()
                .position(location)
                .icon(Bitmap.getBitmapDescriptor(context,R.drawable.flag, 32))

//            map.addMarker(MarkerOptions().position(location))
            map.addMarker(markerOptions)
        }

        val newLatLongBound = CameraUpdateFactory.newLatLngBounds(
            builder.build(),
            20
        )

//        map.addCircle(CircleOptions().center(locations[0]).radius(5000.0))
        map.setMaxZoomPreference(18f)
        map.animateCamera(newLatLongBound)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        return false
    }
}