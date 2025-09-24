package com.example.testapplication.canvas

import android.content.Context
import android.content.Intent
import com.example.testapplication.R
import jp.co.sompo_japan.drv.ui.base.BaseActivity

class CoordinateActivity: BaseActivity() {

    companion object {
        fun startActivity(context: Context): Intent {
            return context.run {
                Intent(this, CoordinateActivity::class.java)
            }
        }
    }

    override fun layoutId() = R.layout.activity_coordinate
}