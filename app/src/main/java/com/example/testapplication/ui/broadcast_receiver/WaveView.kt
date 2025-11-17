package com.example.testapplication.ui.broadcast_receiver

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.hypot

class WaveView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private data class Wave(var radius: Float)

    private val waves = mutableListOf<Wave>()
    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = ContextCompat.getColor(context, android.R.color.holo_blue_light)
    }
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, android.R.color.holo_blue_dark)
    }

    private var maxDistance = 0f
    private val waveInterval = 1000L
    private var lastWaveTime = 0L
    private var lastFrameTime = 0L
    private val waveSpeed = 150f
    private var isRunning = false

    init {
        postInvalidateOnAnimation()
    }

    fun startWave() {
        if (!isRunning) {
            isRunning = true
            lastWaveTime = System.currentTimeMillis()
            lastFrameTime = System.currentTimeMillis()
        }
    }

    fun stopWave() {
        isRunning = false
        waves.clear()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        maxDistance = hypot(w.toDouble() / 2, h.toDouble() / 2).toFloat() + 100f
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f

        val currentTime = System.currentTimeMillis()
        var deltaTime = (currentTime - lastFrameTime) / 1000f
        if (deltaTime > 0.1f) deltaTime = 0.1f
        lastFrameTime = currentTime

        if (isRunning) {
            if (currentTime - lastWaveTime >= waveInterval) {
                waves.add(Wave(0f))
                lastWaveTime = currentTime
            }
        }

        canvas.drawCircle(centerX, centerY, 50f, circlePaint)

        val iterator = waves.iterator()
        while (iterator.hasNext()) {
            val wave = iterator.next()
            wave.radius += waveSpeed * deltaTime

            val alpha = ((1f - wave.radius / maxDistance) * 255).toInt().coerceIn(0, 255)
            wavePaint.alpha = alpha
            canvas.drawCircle(centerX, centerY, wave.radius, wavePaint)

            if (wave.radius >= maxDistance) {
                iterator.remove()
            }
        }

        postInvalidateOnAnimation()
    }
}
