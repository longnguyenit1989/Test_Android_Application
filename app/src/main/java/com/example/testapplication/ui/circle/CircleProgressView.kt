package com.example.testapplication.ui.circle

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.example.testapplication.R
import com.example.testapplication.utils.Converter
import kotlin.math.min

class CircleProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val strokeWidthPx = Converter.dpToPx(this@CircleProgressView, 12f)

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = strokeWidthPx
        color = context.getColor(R.color.x_light_grey)
        strokeCap = Paint.Cap.ROUND
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = strokeWidthPx
        color = context.getColor(R.color.light_blue)
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        textSize = Converter.spToPx(this@CircleProgressView, 24f)
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val oval = RectF()
    private var currentPercent = 0f

    private var animator: ValueAnimator? = null

    fun setProgressAnimated(targetPercent: Float, duration: Long = 600L) {
        animator?.cancel()
        val safeTarget = targetPercent.coerceIn(0f, 100f)
        animator = ValueAnimator.ofFloat(currentPercent, safeTarget).apply {
            this.duration = duration
            addUpdateListener {
                currentPercent = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    fun setProgressInstant(percent: Float) {
        currentPercent = percent.coerceIn(0f, 100f)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val padding = strokeWidthPx / 2f + 4f
        val left = padding
        val top = padding
        val right = width.toFloat() - padding
        val bottom = height.toFloat() - padding

        oval.set(left, top, right, bottom)

        canvas.drawArc(oval, 0f, 360f, false, backgroundPaint)

        val sweep = (currentPercent / 100f) * 360f
        canvas.drawArc(oval, -90f, sweep, false, progressPaint)

        val text = "${currentPercent.toInt()}%"
        val x = width / 2f
        val y = (height / 2f) - (textPaint.descent() + textPaint.ascent()) / 2f
        canvas.drawText(text, x, y, textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val size = if (w == 0) h else if (h == 0) w else min(w, h)
        setMeasuredDimension(size, size)
    }
}
