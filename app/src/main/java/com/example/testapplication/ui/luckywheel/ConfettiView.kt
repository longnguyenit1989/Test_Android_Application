package com.example.testapplication.ui.luckywheel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.animation.ValueAnimator
import kotlin.random.Random

class ConfettiView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private data class Particle(
        var x: Float,
        var y: Float,
        val color: Int,
        val vx: Float,
        var vy: Float,
        var alpha: Float,
        val size: Float
    )

    private val particles = mutableListOf<Particle>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun launchFirework(centerX: Float, centerY: Float, count: Int = 200) {
        particles.clear()
        val colors = listOf(
            0xFFFFC107.toInt(),
            0xFFFF5722.toInt(),
            0xFF4CAF50.toInt(),
            0xFF2196F3.toInt(),
            0xFFE91E63.toInt()
        )

        for (i in 0 until count) {
            val angle = Random.nextFloat() * 2 * Math.PI
            val speed = Random.nextFloat() * 15 + 5
            val vx = (Math.cos(angle) * speed).toFloat()
            val vy = (Math.sin(angle) * speed).toFloat()
            val color = colors[Random.nextInt(colors.size)]
            val size = Random.nextFloat() * 12 + 8
            particles.add(Particle(centerX, centerY, color, vx, vy, 1f, size))
        }

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 1200L
        animator.addUpdateListener {
            val dt = 1 / 60f
            particles.forEach { p ->
                p.x += p.vx * dt * 60
                p.y += p.vy * dt * 60
                p.vy += 0.4f * dt * 60
                p.alpha -= 0.015f
            }
            invalidate()
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        particles.forEach { p ->
            paint.color = p.color
            paint.alpha = (p.alpha * 255).toInt().coerceIn(0, 255)
            canvas.drawCircle(p.x, p.y, p.size, paint)
        }
    }
}
