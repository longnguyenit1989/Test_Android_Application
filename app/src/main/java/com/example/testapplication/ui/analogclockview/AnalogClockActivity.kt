package com.example.testapplication.ui.analogclockview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityAnalogClockBinding

class AnalogClockActivity: BaseActivity<ActivityAnalogClockBinding>() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AnalogClockActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityAnalogClockBinding {
        return ActivityAnalogClockBinding.inflate(inflater)
    }
}