package com.example.testapplication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Property
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.canvas.CoordinateActivity
import com.example.testapplication.databinding.ActivityMainBinding
import com.example.testapplication.map.MapActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var buttonsVisible = false
    val distance = 300f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    private fun setupUi() {
        binding.apply {
            btnHome.setOnClickListener {
                if (!buttonsVisible) {
                    showButtons()
                } else {
                    hideButtons()
                }
                buttonsVisible = !buttonsVisible
            }

            btnMap.setOnClickListener {
                startActivity(MapActivity.newIntent(this@MainActivity))
            }

            btnCanvas.setOnClickListener {
                startActivity(CoordinateActivity.newIntent(this@MainActivity))
            }
        }
    }

    private fun showButtons() {
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
            playTogether(anim1, anim2, anim3, anim4, anim5X, anim5Y, anim6X, anim6Y, anim7X, anim7Y, anim8X, anim8Y)
            start()
        }

        binding.apply {
            btn1.visibility = View.VISIBLE
            btn2.visibility = View.VISIBLE
            btn3.visibility = View.VISIBLE
            btn4.visibility = View.VISIBLE
            btn5.visibility = View.VISIBLE
            btn6.visibility = View.VISIBLE
            btn7.visibility = View.VISIBLE
            btn8.visibility = View.VISIBLE
        }
    }

    private fun hideButtons() {
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
            playTogether(anim1, anim2, anim3, anim4, anim5X, anim5Y, anim6X, anim6Y, anim7X, anim7Y, anim8X, anim8Y)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                    binding.apply {
                        btn1.visibility = View.GONE
                        btn2.visibility = View.GONE
                        btn3.visibility = View.GONE
                        btn4.visibility = View.GONE
                        btn5.visibility = View.GONE
                        btn6.visibility = View.GONE
                        btn7.visibility = View.GONE
                        btn8.visibility = View.GONE
                    }
                }
            })
            start()
        }
    }

    private fun getObjectAnimatorShow(view: View, property: Property<View, Float>, float: Float): ObjectAnimator {
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
}


