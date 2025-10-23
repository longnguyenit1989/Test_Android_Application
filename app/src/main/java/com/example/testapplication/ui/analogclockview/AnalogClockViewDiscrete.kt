package com.example.testapplication.ui.analogclockview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.testapplication.R
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class AnalogClockViewDiscrete @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    // Paint objects for different parts of the clock
    private val paintCircle = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintHour = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintMinute = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintSecond = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintTick = Paint(Paint.ANTI_ALIAS_FLAG)

    // Calendar for getting current time
    private val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"))

    // Handler for updating every second
    private val handler = Handler(Looper.getMainLooper())

    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var showNumbers = true

    // Runnable that triggers invalidate() every 1 second
    private val tickRunnable = object : Runnable {
        override fun run() {
            invalidate()
            handler.postDelayed(this, 1000L) // update once per second
        }
    }

    init {
        // Load default colors from resources
        val circleColor = ContextCompat.getColor(context, R.color.clock_circle)
        val hourColor = ContextCompat.getColor(context, R.color.clock_hour_hand)
        val minuteColor = ContextCompat.getColor(context, R.color.clock_minute_hand)
        val secondColor = ContextCompat.getColor(context, R.color.clock_second_hand)
        val textColor = ContextCompat.getColor(context, R.color.clock_text)

        // Circle outline
        paintCircle.apply {
            color = circleColor
            style = Paint.Style.STROKE
            strokeWidth = 10f
            clearShadowLayer()
        }

        // Hour hand
        paintHour.apply {
            color = hourColor
            strokeWidth = 14f
            strokeCap = Paint.Cap.ROUND
        }

        // Minute hand
        paintMinute.apply {
            color = minuteColor
            strokeWidth = 10f
            strokeCap = Paint.Cap.ROUND
        }

        // Second hand (discrete)
        paintSecond.apply {
            color = secondColor
            strokeWidth = 4f
            strokeCap = Paint.Cap.ROUND
        }

        // Text for numbers
        paintText.apply {
            color = textColor
            textAlign = Paint.Align.CENTER
        }

        // Tick marks
        paintTick.color = textColor

        // Load custom attributes if provided in XML
        context.theme.obtainStyledAttributes(attrs, R.styleable.AnalogClockView, 0, 0).apply {
            try {
                paintCircle.color = getColor(R.styleable.AnalogClockView_clockColorCircle, paintCircle.color)
                paintHour.color = getColor(R.styleable.AnalogClockView_clockColorHour, paintHour.color)
                paintMinute.color = getColor(R.styleable.AnalogClockView_clockColorMinute, paintMinute.color)
                paintSecond.color = getColor(R.styleable.AnalogClockView_clockColorSecond, paintSecond.color)
                paintText.color = getColor(R.styleable.AnalogClockView_clockColorText, paintText.color)
                showNumbers = getBoolean(R.styleable.AnalogClockView_clockShowNumbers, true)
            } finally {
                recycle()
            }
        }

        setLayerType(LAYER_TYPE_SOFTWARE, null)
        handler.post(tickRunnable)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacks(tickRunnable)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = min(centerX, centerY) * 0.9f
        paintText.textSize = radius * 0.12f
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Create a radial gradient background for the circle
        val gradientCenter = ContextCompat.getColor(context, R.color.clock_gradient_center)
        val gradientEdge = ContextCompat.getColor(context, R.color.clock_gradient_edge)

        val gradient = RadialGradient(
            centerX, centerY, radius,
            intArrayOf(gradientCenter, gradientEdge),
            floatArrayOf(0.6f, 1f),
            Shader.TileMode.CLAMP
        )
        paintCircle.shader = gradient

        // Draw outer circle
        canvas.drawCircle(centerX, centerY, radius, paintCircle)

        // Update time (discrete seconds)
        calendar.timeInMillis = System.currentTimeMillis()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        // Calculate angles for each hand
        val minuteFrac = minute + second / 60f
        val hourFrac = hour + minuteFrac / 60f

        val secondAngle = second * 6f       // jump per second
        val minuteAngle = minuteFrac * 6f
        val hourAngle = hourFrac * 30f

        // Draw tick marks (60 total)
        for (i in 0 until 60) {
            val angle = Math.toRadians((i * 6 - 90).toDouble())
            val start = if (i % 5 == 0) radius * 0.85f else radius * 0.92f
            val end = radius
            val startX = centerX + start * cos(angle).toFloat()
            val startY = centerY + start * sin(angle).toFloat()
            val endX = centerX + end * cos(angle).toFloat()
            val endY = centerY + end * sin(angle).toFloat()
            paintTick.strokeWidth = if (i % 5 == 0) 5f else 2f
            canvas.drawLine(startX, startY, endX, endY, paintTick)
        }

        // Draw hour numbers (1 to 12)
        if (showNumbers) {
            for (i in 1..12) {
                val angle = Math.toRadians((i * 30 - 90).toDouble())
                val x = centerX + radius * 0.72f * cos(angle).toFloat()
                val y = centerY + radius * 0.72f * sin(angle).toFloat() + paintText.textSize / 3f
                canvas.drawText(i.toString(), x, y, paintText)
            }
        }

        // Draw hands
        drawHand(canvas, hourAngle, radius * 0.5f, paintHour)
        drawHand(canvas, minuteAngle, radius * 0.7f, paintMinute)
        drawHand(canvas, secondAngle, radius * 0.85f, paintSecond)

        // Draw small center circle
        paintSecond.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, radius * 0.04f, paintSecond)
        paintSecond.style = Paint.Style.STROKE
    }

    // Helper function to draw a single hand
    private fun drawHand(canvas: Canvas, angleDegrees: Float, length: Float, paint: Paint) {
        val angle = Math.toRadians(angleDegrees - 90.0)
        val x = centerX + (length * cos(angle)).toFloat()
        val y = centerY + (length * sin(angle)).toFloat()
        canvas.drawLine(centerX, centerY, x, y, paint)
    }
}
