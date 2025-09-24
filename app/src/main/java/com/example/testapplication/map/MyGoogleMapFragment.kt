package com.example.testapplication.map

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.testapplication.R
import com.example.testapplication.canvas.CoordinateActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MyGoogleMapFragment : Fragment(), OnMapReadyCallback {
    private var googleMapHelper: GoogleMapHelper? = null
    private var supportMapFragment = SupportMapFragment.newInstance()

    override fun onMapReady(p0: GoogleMap) {
        googleMapHelper = GoogleMapHelper(requireActivity(),p0)
        Handler(Looper.getMainLooper()).postDelayed({
            googleMapHelper?.focus(listLocationDummy)
        }, 500)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        supportMapFragment.getMapAsync(this)
        val view = inflater.inflate(R.layout.fragment_my_google_map, container, false)
        childFragmentManager.beginTransaction()
            .replace(R.id.frlFragmentMyGoogleMap, supportMapFragment as Fragment)
            .commit()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheet(view)
    }

    private fun setupBottomSheet(view: View) {
        val bottomSheet = view.findViewById<View>(R.id.bottomSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val displayMetrics = resources.displayMetrics
        val halfHeight = displayMetrics.heightPixels / 2
        bottomSheet.layoutParams.height = halfHeight
        bottomSheet.requestLayout()

        val dragHandle = view.findViewById<View>(R.id.dragHandle)
        dragHandle.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        val imgNext = view.findViewById<ImageView>(R.id.imgNext)
        imgNext.setOnClickListener {
            startActivity(CoordinateActivity.startActivity(requireActivity()))
        }
    }


    private val listLocationDummy = listOf(
        LatLng(21.052171, 105.8365053),
        LatLng(21.0641963, 105.8075825),
        LatLng(21.056500, 105.830200),
        LatLng(21.060700, 105.820500),
        LatLng(21.058200, 105.812900),
        LatLng(21.062800, 105.834400),
        LatLng(21.067500, 105.817000)
    )
}
