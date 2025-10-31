package com.example.testapplication.ui.luckywheel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.testapplication.R
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class WheelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var items: MutableList<String> = mutableListOf()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 32f * resources.displayMetrics.density / 2f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    private val sectionColors = mutableListOf<Int>()

    fun setItems(list: MutableList<String>) {
        items.clear()
        items.addAll(list)
        generateColors(items.size)
        invalidate()
        requestLayout()
    }

    fun removeItem(index: Int) {
        if (index in items.indices) {
            items.removeAt(index)
            sectionColors.removeAt(index)
            invalidate()
            requestLayout()
        }
    }

    private fun generateColors(count: Int) {
        sectionColors.clear()

        val basePalette = mutableListOf(
            ContextCompat.getColor(context, R.color.red),
            ContextCompat.getColor(context, R.color.blue),
            ContextCompat.getColor(context, R.color.green),
            ContextCompat.getColor(context, R.color.violet),
            ContextCompat.getColor(context, R.color.yellow),
            ContextCompat.getColor(context, R.color.grey),
        ).distinct().toMutableList()

        if (count <= basePalette.size) {
            sectionColors.addAll(basePalette.take(count))
            return
        }

        sectionColors.addAll(basePalette)
        val random = Random(System.currentTimeMillis())

        while (sectionColors.size < count) {
            var newColor: Int
            do {
                newColor = Color.rgb(
                    random.nextInt(60, 240),
                    random.nextInt(60, 240),
                    random.nextInt(60, 240)
                )
            } while (sectionColors.any { areColorsSimilar(it, newColor) })
            sectionColors.add(newColor)
        }
    }

    private fun areColorsSimilar(c1: Int, c2: Int): Boolean {
        val rDiff = Color.red(c1) - Color.red(c2)
        val gDiff = Color.green(c1) - Color.green(c2)
        val bDiff = Color.blue(c1) - Color.blue(c2)
        val distance = rDiff * rDiff + gDiff * gDiff + bDiff * bDiff
        return distance < 2500
    }

    fun getSectionColor(index: Int): Int {
        return sectionColors.getOrElse(index) { Color.GRAY }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (items.isEmpty()) return

        val w = width.toFloat()
        val h = height.toFloat()
        val cx = w / 2f
        val cy = h / 2f
        val radius = min(w, h) / 2f
        val rectF = RectF(cx - radius, cy - radius, cx + radius, cy + radius)
        val sweep = 360f / items.size
        val sweepRad = Math.toRadians(sweep.toDouble())

        // Get text paint metrics once
        val fm = textPaint.fontMetrics
        val textHeight = fm.descent - fm.ascent

        for (i in items.indices) {
            // Draw the colored section
            paint.color = sectionColors.getOrElse(i) { Color.GRAY }
            val startAngle = i * sweep
            canvas.drawArc(rectF, startAngle, sweep, true, paint)

            // Compute the middle angle of the section (deg) and (rad)
            val middleAngle = startAngle + sweep / 2f
            val angleRad = Math.toRadians(middleAngle.toDouble())

            // Compute convenient position for the text: along the radius towards outside
            val textRadius = radius * 0.6f
            val tx = (cx + textRadius * cos(angleRad)).toFloat()
            val tyCenter = (cy + textRadius * sin(angleRad)).toFloat()

            // Compute maximum width for text inside the section (approximate chord length at textRadius)
            val maxTextWidth = (2.0 * textRadius * sin(sweepRad / 2.0)).toFloat() * 0.9f
                .coerceAtLeast(24f) // minimum 24px to avoid too small

            // Split text into multiple lines if needed (greedy word-wrapping)
            val lines = wrapTextToLines(items[i], textPaint, maxTextWidth)

            // Compute total height of the text block
            val totalTextBlockHeight = lines.size * textHeight

            // Draw each line, centered horizontally at tx and vertically to align block center at tyCenter
            var lineTop = tyCenter - totalTextBlockHeight / 2f

            for (line in lines) {
                // baseline: lineTop - ascent (ascent is negative)
                val baseline = lineTop - fm.ascent
                canvas.drawText(line, tx, baseline, textPaint)
                lineTop += textHeight
            }
        }

        // Draw the white center circle
        paint.color = Color.WHITE
        canvas.drawCircle(cx, cy, radius * 0.25f, paint)
    }

    /**
     * Greedy word wrap: splits text into lines so that each line <= maxWidth when drawn with paint.
     * Returns a list of lines (at least 1 line).
     */
    private fun wrapTextToLines(text: String, paint: Paint, maxWidth: Float): List<String> {
        val words = text.trim().split(Regex("\\s+"))
        if (words.isEmpty()) return listOf("")
        val lines = mutableListOf<String>()
        var current = StringBuilder(words[0])
        for (i in 1 until words.size) {
            val w = words[i]
            val candidate = "${current} $w"
            if (paint.measureText(candidate) <= maxWidth) {
                current.append(" ").append(w)
            } else {
                lines.add(current.toString())
                current = StringBuilder(w)
            }
        }
        lines.add(current.toString())

        // If still a single line but too long (single word too wide), split by character to avoid overflow
        if (lines.size == 1 && paint.measureText(lines[0]) > maxWidth) {
            val long = lines[0]
            lines.clear()
            var part = StringBuilder()
            for (ch in long) {
                part.append(ch)
                if (paint.measureText(part.toString()) > maxWidth) {
                    // remove last char to stay within limit (unless single char)
                    if (part.length > 1) {
                        val last = part.last()
                        part.deleteCharAt(part.length - 1)
                        lines.add(part.toString())
                        part = StringBuilder(last.toString())
                    } else {
                        // single char too wide: still push it
                        lines.add(part.toString())
                        part = StringBuilder()
                    }
                }
            }
            if (part.isNotEmpty()) lines.add(part.toString())
        }

        return lines
    }

}
