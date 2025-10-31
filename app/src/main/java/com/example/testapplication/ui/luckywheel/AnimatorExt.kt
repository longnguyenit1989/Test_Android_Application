package com.example.testapplication.ui.luckywheel

import android.animation.Animator

inline fun Animator.addListener(
    crossinline onEnd: (Animator?) -> Unit = {}
) {
    this.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) = onEnd(animation)
        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
}