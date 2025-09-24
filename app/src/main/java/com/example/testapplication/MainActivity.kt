package com.example.testapplication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import jp.co.sompo_japan.drv.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var buttonHome: Button
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button

    private var buttonsVisible = false
    val distance = 200f
    val duration = 300L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buttonHome = findViewById(R.id.buttonHome)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        button4 = findViewById(R.id.button4)

        buttonHome.setOnClickListener {
                if (!buttonsVisible) {
                    showButtons()
                } else {
                    hideButtons()
                }
                buttonsVisible = !buttonsVisible
        }
    }

    override fun layoutId() = R.layout.activity_main

    private fun showButtons() {
        // Di chuyển các button ra từ trung tâm của buttonHome
        val anim1 = ObjectAnimator.ofFloat(button1, View.TRANSLATION_Y, -distance).apply {
            interpolator = AccelerateInterpolator()
            this.duration = duration
        }

        val anim2 = ObjectAnimator.ofFloat(button2, View.TRANSLATION_X, distance).apply {
            interpolator = AccelerateInterpolator()
            this.duration = duration
        }

        val anim3 = ObjectAnimator.ofFloat(button3, View.TRANSLATION_Y, distance, buttonHome.x, buttonHome.y + 300).apply {
            interpolator = AccelerateInterpolator()
            this.duration = duration
        }

        val anim4 = ObjectAnimator.ofFloat(button4, View.TRANSLATION_X, -distance).apply {
            interpolator = AccelerateInterpolator()
            this.duration = duration
        }

        AnimatorSet().apply {
            playTogether(anim1, anim2, anim3, anim4)
            start()
        }

        // Hiển thị các button
        button1.visibility = View.VISIBLE
        button2.visibility = View.VISIBLE
        button3.visibility = View.VISIBLE
        button4.visibility = View.VISIBLE
    }

    private fun hideButtons() {
        // Thu các button về lại trung tâm của buttonHome
        val anim1 = ObjectAnimator.ofFloat(button1, View.TRANSLATION_Y, 0f).apply {
            interpolator = AccelerateInterpolator()
            this.duration = duration
        }

        val anim2 = ObjectAnimator.ofFloat(button2, View.TRANSLATION_X, 0f).apply {
            interpolator = AccelerateInterpolator()
            this.duration = duration
        }

        val anim3 = ObjectAnimator.ofFloat(button3, View.TRANSLATION_Y, 0f).apply {
            interpolator = AccelerateInterpolator()
            this.duration = duration
        }

        val anim4 = ObjectAnimator.ofFloat(button4, View.TRANSLATION_X, 0f).apply {
            interpolator = AccelerateInterpolator()
            this.duration = duration
        }

        AnimatorSet().apply {
            playTogether(anim1, anim2, anim3, anim4)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                    button1.visibility = View.INVISIBLE
                    button2.visibility = View.INVISIBLE
                    button3.visibility = View.INVISIBLE
                    button4.visibility = View.INVISIBLE
                }
            })
            start()
        }

    }
}


object AnimationUtils {
    fun moveView(view: View, direction: Int, startX: Float, distance: Float) {
        var endX = startX
        var endY = view.y
        when (direction) {
            0 -> endX = startX + distance
            1 -> endX = startX - distance
            2 -> endY = view.y - distance
            3 -> endY = view.y + distance
        }
        val animatorX = ObjectAnimator.ofFloat(view, "translationX", startX, endX)
        val animatorY = ObjectAnimator.ofFloat(view, "translationY", view.y, endY)
        animatorX.setDuration(1000) // Duration in milliseconds
        animatorY.setDuration(1000)
        animatorX.start()
        animatorY.start()
    }
}


