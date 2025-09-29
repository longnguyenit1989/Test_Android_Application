package com.example.testapplication.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.testapplication.model.Stroke
import androidx.core.graphics.createBitmap

class DrawView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val strokes = mutableListOf<Stroke>()
    private val undoneStrokes = mutableListOf<Stroke>()
    private var currentPath: Path? = null

    private var currentColor: Int = Color.BLACK
    private var currentWidth: Float = 8f
    private var currentPaint: Paint = createPaint(currentColor, currentWidth)

    private fun createPaint(color: Int, width: Float): Paint {
        return Paint().apply {
            this.color = color
            this.strokeWidth = width
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
    }

    fun setStrokeColor(color: Int) {
        currentColor = color
        currentPaint = createPaint(currentColor, currentWidth)
    }

    fun setStrokeWidth(width: Float) {
        currentWidth = width
        currentPaint = createPaint(currentColor, currentWidth)
    }

    fun getStrokeColor(): Int = currentColor

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (stroke in strokes) {
            canvas.drawPath(stroke.path, stroke.paint)
        }
        currentPath?.let { canvas.drawPath(it, currentPaint) }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPaint = createPaint(currentColor, currentWidth)
                currentPath = Path().apply { moveTo(event.x, event.y) }
                undoneStrokes.clear()
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath?.lineTo(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                currentPath?.let {
                    strokes.add(Stroke(it, Paint(currentPaint)))
                }
                currentPath = null
            }
        }
        invalidate()
        return true
    }

    fun undo() {
        if (strokes.isNotEmpty()) {
            val lastStroke = strokes.removeAt(strokes.size - 1)
            undoneStrokes.add(lastStroke)
            invalidate()
        }
    }

    fun redo() {
        if (undoneStrokes.isNotEmpty()) {
            val restored = undoneStrokes.removeAt(undoneStrokes.size - 1)
            strokes.add(restored)
            invalidate()
        }
    }

    fun clearAll() {
        strokes.clear()
        undoneStrokes.clear()
        invalidate()
    }

    fun exportToBitmap(): Bitmap {
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        this.draw(canvas) // vẽ toàn bộ view (bao gồm strokes)
        return bitmap
    }

}


