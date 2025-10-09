package com.example.testapplication.ui.movebutton

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityMoveButtonBinding


class MoveButtonActivity: BaseActivity<ActivityMoveButtonBinding>() {
    private val snapMargin = 60

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
        setupUi()
    }
    private val originalPositions = mutableMapOf<View, Pair<Float, Float>>()
    private val isInBorder = mutableMapOf<View, Boolean>()

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUi() {
        binding.apply {
            listOf(btnClickMe1, btnClickMe2, btnMove).forEach { button ->
                button.post {
                    originalPositions[button] = Pair(button.x, button.y)
                    isInBorder[button] = false
                }
            }

            btnMove.setOnTouchListener(object : View.OnTouchListener {
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

                        MotionEvent.ACTION_UP -> {
                            val borderX = borderView.x
                            val borderY = borderView.y
                            val borderWidth = borderView.width
                            val borderHeight = borderView.height

                            val btnWidth = view.width
                            val btnHeight = view.height

                            val btnCenterX = view.x + btnWidth / 2
                            val btnCenterY = view.y + btnHeight / 2

                            if (btnCenterX in (borderX - snapMargin)..(borderX + borderWidth + snapMargin) &&
                                btnCenterY in (borderY - snapMargin)..(borderY + borderHeight + snapMargin)) {
                                moveButtonToBorder(btnMove, borderView)
                                isInBorder[btnMove] = true
                            }
                            return true
                        }

                        else -> return false
                    }
                }
            })

            btnClickMe1.setOnClickListener {
                toggleButtonPosition(btnClickMe1, borderView1)
            }

            btnClickMe2.setOnClickListener {
                toggleButtonPosition(btnClickMe2, borderView2)
            }
        }
    }

    private fun toggleButtonPosition(button: View, border: View) {
        if (isInBorder[button] == true) {
            val (originalX, originalY) = originalPositions[button]!!
            button.animate()
                .x(originalX)
                .y(originalY)
                .setDuration(300)
                .start()
            isInBorder[button] = false
        } else {
            moveButtonToBorder(button, border)
            isInBorder[button] = true
        }
    }

    private fun moveButtonToBorder(button: View, border: View) {
        val targetX = border.x + (border.width - button.width) / 2
        val targetY = border.y + (border.height - button.height) / 2

        button.animate()
            .x(targetX)
            .y(targetY)
            .setDuration(300)
            .start()
    }

}