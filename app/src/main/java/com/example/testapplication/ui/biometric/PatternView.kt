package com.example.testapplication.ui.biometric

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.hypot

class PatternView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val gridSize = 3
    private var dotRadius = 0f
    private var dotSpacing = 0f
    private val pathWidth = 8f

    private val paintDot = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        style = Paint.Style.FILL
    }
    private val paintSelected = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }
    private val paintLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        strokeWidth = pathWidth
        style = Paint.Style.STROKE
    }

    private val dots = mutableListOf<PointF>()
    private val selected = mutableListOf<Int>()
    private var listener: ((List<Int>) -> Unit)? = null

    private var currentX = 0f
    private var currentY = 0f
    private var isDrawing = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initDots(w, h)
    }

    private fun initDots(width: Int, height: Int) {
        dots.clear()

        val minDim = width.coerceAtMost(height).toFloat()
        dotSpacing = minDim / 4f
        dotRadius = dotSpacing / 9f

        val startX = (width - (dotSpacing * (gridSize - 1))) / 2f
        val startY = (height - (dotSpacing * (gridSize - 1))) / 2f

        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                dots.add(PointF(startX + col * dotSpacing, startY + row * dotSpacing))
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (dots.isEmpty()) return

        if (selected.size > 1) {
            for (i in 0 until selected.size - 1) {
                val from = dots[selected[i]]
                val to = dots[selected[i + 1]]
                canvas.drawLine(from.x, from.y, to.x, to.y, paintLine)
            }
        }

        if (isDrawing && selected.isNotEmpty()) {
            val last = dots[selected.last()]
            canvas.drawLine(last.x, last.y, currentX, currentY, paintLine)
        }

        dots.forEachIndexed { index, p ->
            val paint = if (selected.contains(index)) paintSelected else paintDot
            canvas.drawCircle(p.x, p.y, dotRadius, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (dots.isEmpty()) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                selected.clear()
                isDrawing = true
                handleTouch(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                handleTouch(event.x, event.y)
                currentX = event.x
                currentY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                isDrawing = false
                invalidate()
                listener?.invoke(selected.toList())
            }
        }
        return true
    }

    private fun handleTouch(x: Float, y: Float) {
        dots.forEachIndexed { index, point ->
            if (!selected.contains(index)) {
                val dx = x - point.x
                val dy = y - point.y
                if (hypot(dx, dy) < dotRadius * 1.5f) {
                    selected.add(index)
                }
            }
        }
    }

    fun setOnPatternListener(listener: (List<Int>) -> Unit) {
        this.listener = listener
    }

    fun clearPattern() {
        selected.clear()
        invalidate()
    }
}
