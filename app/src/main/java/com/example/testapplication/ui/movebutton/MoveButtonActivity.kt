package com.example.testapplication.ui.movebutton

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Property
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityMoveButtonBinding
import com.example.testapplication.extension.beGone
import com.example.testapplication.extension.beVisible


class MoveButtonActivity: BaseActivity<ActivityMoveButtonBinding>() {
    private val snapMargin = 60

    private var buttonsVisible = false
    private var isMovingButton = false;
    val distance = 300f

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

            btnClickMe.setOnClickListener {
                if (!buttonsVisible) {
                    showButtons()
                } else {
                    hideButtons()
                }
                buttonsVisible = !buttonsVisible
            }

            ballContainer.setOnClickListener {
                val progress = motionLayout.progress
                when (progress) {
                    0f -> motionLayout.transitionToEnd()
                    1f -> motionLayout.transitionToStart()
                    else -> {
                        // animation is running → do nothing
                    }
                }
            }
        }
    }

    private fun showButtons() {
        if (isMovingButton) {
            return
        }
        isMovingButton = true

        val anim1 = getObjectAnimatorShow(binding.btn1, View.TRANSLATION_Y, -distance)
        val anim2 = getObjectAnimatorShow(binding.btn2, View.TRANSLATION_X, distance)
        val anim3 = getObjectAnimatorShow(binding.btn3, View.TRANSLATION_Y, distance)
        val anim4 = getObjectAnimatorShow(binding.btn4, View.TRANSLATION_X, -distance)

        val anim5X = getObjectAnimatorShow(binding.btn5, View.TRANSLATION_X, distance)
        val anim5Y = getObjectAnimatorShow(binding.btn5, View.TRANSLATION_Y, -distance)

        val anim6X = getObjectAnimatorShow(binding.btn6, View.TRANSLATION_X, distance)
        val anim6Y = getObjectAnimatorShow(binding.btn6, View.TRANSLATION_Y, distance)

        val anim7X = getObjectAnimatorShow(binding.btn7, View.TRANSLATION_X, -distance)
        val anim7Y = getObjectAnimatorShow(binding.btn7, View.TRANSLATION_Y, distance)

        val anim8X = getObjectAnimatorShow(binding.btn8, View.TRANSLATION_X, -distance)
        val anim8Y = getObjectAnimatorShow(binding.btn8, View.TRANSLATION_Y, -distance)

        AnimatorSet().apply {
            playTogether(
                anim1,
                anim2,
                anim3,
                anim4,
                anim5X,
                anim5Y,
                anim6X,
                anim6Y,
                anim7X,
                anim7Y,
                anim8X,
                anim8Y
            )
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                    isMovingButton = false
                }
            })
            start()
        }

        binding.apply {
            btn1.beVisible()
            btn2.beVisible()
            btn3.beVisible()
            btn4.beVisible()
            btn5.beVisible()
            btn6.beVisible()
            btn7.beVisible()
            btn8.beVisible()
        }
    }

    private fun hideButtons() {
        if (isMovingButton) {
            return
        }

        isMovingButton = true
        val anim1 = getObjectAnimatorHide(binding.btn1, View.TRANSLATION_Y)
        val anim2 = getObjectAnimatorHide(binding.btn2, View.TRANSLATION_X)
        val anim3 = getObjectAnimatorHide(binding.btn3, View.TRANSLATION_Y)
        val anim4 = getObjectAnimatorHide(binding.btn4, View.TRANSLATION_X)

        val anim5X = getObjectAnimatorHide(binding.btn5, View.TRANSLATION_X)
        val anim5Y = getObjectAnimatorHide(binding.btn5, View.TRANSLATION_Y)

        val anim6X = getObjectAnimatorHide(binding.btn6, View.TRANSLATION_X)
        val anim6Y = getObjectAnimatorHide(binding.btn6, View.TRANSLATION_Y)

        val anim7X = getObjectAnimatorHide(binding.btn7, View.TRANSLATION_X)
        val anim7Y = getObjectAnimatorHide(binding.btn7, View.TRANSLATION_Y)

        val anim8X = getObjectAnimatorHide(binding.btn8, View.TRANSLATION_X)
        val anim8Y = getObjectAnimatorHide(binding.btn8, View.TRANSLATION_Y)

        AnimatorSet().apply {
            playTogether(
                anim1,
                anim2,
                anim3,
                anim4,
                anim5X,
                anim5Y,
                anim6X,
                anim6Y,
                anim7X,
                anim7Y,
                anim8X,
                anim8Y
            )
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                    binding.apply {
                        btn1.beGone()
                        btn2.beGone()
                        btn3.beGone()
                        btn4.beGone()
                        btn5.beGone()
                        btn6.beGone()
                        btn7.beGone()
                        btn8.beGone()
                    }
                    isMovingButton = false
                }
            })
            start()
        }
    }

    private fun getObjectAnimatorShow(
        view: View,
        property: Property<View, Float>,
        float: Float
    ): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, property, float).apply {
            interpolator = AccelerateInterpolator()
            this.duration = duration
        }
    }

    private fun getObjectAnimatorHide(view: View, property: Property<View, Float>): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, property, 0f).apply {
            interpolator = AccelerateInterpolator()
            this.duration = duration
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