package com.example.testapplication.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.testapplication.R
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityMapBinding

class MapActivity: BaseActivity<ActivityMapBinding>() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MapActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment(MyGoogleMapFragment(), R.id.frlActivityMap, false)
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityMapBinding {
        return ActivityMapBinding.inflate(inflater)
    }

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