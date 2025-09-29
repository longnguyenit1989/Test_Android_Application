package com.example.testapplication.ui.movebutton

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityMoveButtonBinding


class MoveButtonActivity: BaseActivity<ActivityMoveButtonBinding>() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MoveButtonActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityMoveButtonBinding {
        return ActivityMoveButtonBinding.inflate(inflater)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            btnMove.setOnTouchListener(object : OnTouchListener {
                var dX: Float = 0f
                var dY: Float = 0f

                override fun onTouch(view: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            dX = view.x - event.rawX
                            dY = view.y - event.rawY
                            return true
                        }

                        MotionEvent.ACTION_MOVE -> {
                            view.animate()
                                .x(event.rawX + dX)
                                .y(event.rawY + dY)
                                .setDuration(0)
                                .start()
                            return true
                        }

                        else -> return false
                    }
                }
            })
        }
    }
}