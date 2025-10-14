package com.example.testapplication.ui.circle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityCircleBinding

class CircleActivity : BaseActivity<ActivityCircleBinding>() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, CircleActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityCircleBinding {
        return ActivityCircleBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.apply {
            btn25.setOnClickListener { circleProgress.setProgressAnimated(25f) }
            btn50.setOnClickListener { circleProgress.setProgressAnimated(50f) }
            btn75.setOnClickListener { circleProgress.setProgressAnimated(75f) }
            btn100.setOnClickListener { circleProgress.setProgressAnimated(100f) }

            circleProgress.setProgressInstant(0f)
        }
    }
}