package com.example.testapplication.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.testapplication.R
import jp.co.sompo_japan.drv.ui.base.BaseActivity

class MapActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment(MyGoogleMapFragment(), R.id.frlActivityMap)
    }

    override fun layoutId() = R.layout.activity_map

    private fun showFragment(
        fragment: Fragment,
        containerId: Int,
        isAddToBackStack: Boolean = true,
        bundle: Bundle? = null,
        tag: String? = null
    ) {
        supportFragmentManager.beginTransaction().apply {
            replace(containerId, fragment.apply { bundle?.let { arguments = it } })
            if (isAddToBackStack) {
                addToBackStack(tag ?: fragment.tag)
            }
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            commit()
        }
    }
}