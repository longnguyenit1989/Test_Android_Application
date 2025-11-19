package com.example.testapplication.ui.steptoday

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.example.testapplication.R
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityStepTodayBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StepTodayActivity : BaseActivity<ActivityStepTodayBinding>(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepDetector: Sensor? = null
    private var stepsToday = 0
    private val stepLengthMeters = 0.7f
    private val weightKg = 70f
    private val prefsName = "stepPrefs"
    private val keyDate = "date"
    private val keySteps = "stepsToday"
    private val hourlySteps = mutableMapOf<Int, Int>()

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, StepTodayActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        if (stepDetector == null) {
            Toast.makeText(this, "Device does not have STEP_DETECTOR sensor", Toast.LENGTH_LONG)
                .show()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    101
                )
            }
        }

        loadStepsToday()
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        stepDetector?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityStepTodayBinding {
        return ActivityStepTodayBinding.inflate(inflater)
    }

    private fun loadStepsToday() {
        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val savedDate = prefs.getString(keyDate, "")
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        stepsToday = if (savedDate == today) {
            prefs.getInt(keySteps, 0)
        } else {
            prefs.edit { putString(keyDate, today).putInt(keySteps, 0) }
            0
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        val distance = stepsToday * stepLengthMeters
        val calories = distance * 0.5f * weightKg / 1000f
        binding.tvSteps.text = "${getString(R.string.steps_today)}: $stepsToday"
        binding.tvDistance.text = "${getString(R.string.distance)}: ${"%.1f".format(distance)} m"
        binding.tvCalories.text = "${getString(R.string.calories)}: ${"%.1f".format(calories)} kcal"

        updateChart()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val stepIncrement = if (it.values[0] < 1f) 1 else it.values[0].toInt()
            stepsToday += stepIncrement

            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            hourlySteps[hour] = (hourlySteps[hour] ?: 0) + stepIncrement

            getSharedPreferences(prefsName, Context.MODE_PRIVATE).edit {
                putInt(keySteps, stepsToday)
            }
            updateUI()
        }
    }

    private fun updateChart() {
        val entries = mutableListOf<BarEntry>()
        for (hour in 0..23) {
            val steps = hourlySteps[hour]?.toFloat() ?: 0f
            entries.add(BarEntry(hour.toFloat(), steps))
        }

        val dataSet = BarDataSet(entries, getString(R.string.steps_per_hour))
        dataSet.color = ContextCompat.getColor(binding.root.context, R.color.blue)

        // hide value 0.0
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getBarLabel(barEntry: BarEntry?): String {
                return if ((barEntry?.y ?: 0f) > 0f) (barEntry?.y!!.toInt()).toString() else ""
            }
        }

        val barData = BarData(dataSet)
        barData.barWidth = 0.9f

        binding.barChartSteps.data = barData
        binding.barChartSteps.setFitBars(true)
        binding.barChartSteps.description.isEnabled = false

        val xAxis = binding.barChartSteps.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)

        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val hour = value.toInt()
                return if (hour % 2 == 0) hour.toString() else ""
            }
        }

        binding.barChartSteps.axisRight.isEnabled = false
        binding.barChartSteps.invalidate()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun getHourlySteps(): Map<Int, Int> {
        return hourlySteps.toMap()
    }
}
