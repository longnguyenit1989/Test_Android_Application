package com.example.testapplication.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapHelper(val map: GoogleMap): GoogleMap.OnMarkerClickListener {

    fun focus(locations: List<LatLng>,) {
        val builder = LatLngBounds.Builder()

        locations.forEach { location ->
            builder.include(location)
            map.addMarker(MarkerOptions().position(location))
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