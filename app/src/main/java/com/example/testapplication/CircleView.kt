package com.example.testapplication

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class CircleView : View {

    private var circlePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animatePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animateX = 0f

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        circlePaint.color = Color.WHITE
        animatePaint.color = Color.RED
        animatePaint.style = Paint.Style.FILL
        startAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.min(centerX, centerY)

        // Draw white circle
        canvas.drawCircle(centerX, centerY, radius, circlePaint)

        // Draw animated red semi-circle
        canvas.drawArc(0f, 0f, width.toFloat(), height.toFloat(), -90f, animateX, true, animatePaint)
    }

    private fun startAnimation() {
        val objectAnimator = ObjectAnimator.ofFloat(this, "animateX", 0f, 180f)
        objectAnimator.duration = 2000 // 2 seconds
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.repeatCount = ObjectAnimator.INFINITE
        objectAnimator.start()
    }

    fun setAnimateX(value: Float) {
        animateX = value
        invalidate()
    }
}
