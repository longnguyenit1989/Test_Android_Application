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
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.testapplication.canvas.CoordinateActivity
import com.example.testapplication.databinding.ActivityMainBinding
import com.example.testapplication.map.MapActivity
import com.example.testapplication.base.BaseActivity

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

        AnimatorSet().apply {
            playTogether(anim1, anim2, anim3, anim4)
            start()
        }

        binding.apply {
            btn1.visibility = View.VISIBLE
            btn2.visibility = View.VISIBLE
            btn3.visibility = View.VISIBLE
            btn4.visibility = View.VISIBLE
        }
    }

    private fun hideButtons() {
        val anim1 = getObjectAnimatorHide(binding.btn1, View.TRANSLATION_Y)
        val anim2 = getObjectAnimatorHide(binding.btn2, View.TRANSLATION_X)
        val anim3 = getObjectAnimatorHide(binding.btn3, View.TRANSLATION_Y)
        val anim4 = getObjectAnimatorHide(binding.btn4, View.TRANSLATION_X)

        AnimatorSet().apply {
            playTogether(anim1, anim2, anim3, anim4)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                    binding.apply {
                        btn1.visibility = View.GONE
                        btn2.visibility = View.GONE
                        btn3.visibility = View.GONE
                        btn4.visibility = View.GONE
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


