package com.example.testapplication.ui.circle

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random
import androidx.core.graphics.withRotation
import com.example.testapplication.R

class RadarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {

    private var radarRadius = 0f
    private var sweepAngle = 0f

    private val radarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        shader = null
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = Color.argb(100, 0, 255, 0)
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private var dots = mutableListOf<Pair<Float, Float>>()

    private val animator = ValueAnimator.ofFloat(0f, 360f).apply {
        duration = 4000L
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            val newAngle = it.animatedValue as Float

            if (sweepAngle > newAngle) {
                generateRandomDots()
            }

            sweepAngle = newAngle
            invalidate()
        }
    }

    init {
        animator.start()
        generateRandomDots()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radarRadius = min(w, h) / 2f * 0.8f
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f

        val gradient = RadialGradient(
            cx, cy, radarRadius,
            intArrayOf(Color.argb(80, 0, 255, 0), Color.argb(10, 0, 255, 0)),
            floatArrayOf(0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        radarPaint.shader = gradient
        canvas.drawCircle(cx, cy, radarRadius, radarPaint)

        canvas.drawCircle(cx, cy, radarRadius, borderPaint)

        val sweepGradient = SweepGradient(
            cx, cy,
            intArrayOf(Color.TRANSPARENT, Color.argb(200, 0, 255, 0)),
            floatArrayOf(0f, 1f)
        )
        val sweepPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = sweepGradient
        }

        canvas.withRotation(sweepAngle, cx, cy) {
            drawArc(
                cx - radarRadius, cy - radarRadius,
                cx + radarRadius, cy + radarRadius,
                0f, 60f,
                true, sweepPaint
            )
        }

        dots.forEach { (dx, dy) ->
            canvas.drawCircle(cx + dx, cy + dy, 8f, dotPaint)
        }

        val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.red)
        }
        canvas.drawCircle(cx, cy, 10f, centerPaint) // bán kính 10px
    }


    private fun generateRandomDots() {
        dots.clear()
        for (i in 0 until 3) {
            val r = Random.nextFloat() * radarRadius
            val angle = Random.nextFloat() * 360f
            val x = r * cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = r * sin(Math.toRadians(angle.toDouble())).toFloat()
            dots.add(x to y)
        }
    }
}
