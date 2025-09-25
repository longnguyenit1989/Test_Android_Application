package com.example.testapplication.canvas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.example.testapplication.R
import com.example.testapplication.databinding.ActivityCoordinateBinding
import com.example.testapplication.base.BaseActivity

class CoordinateActivity: BaseActivity<ActivityCoordinateBinding>() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, CoordinateActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityCoordinateBinding {
        return ActivityCoordinateBinding.inflate(inflater)
    }
}