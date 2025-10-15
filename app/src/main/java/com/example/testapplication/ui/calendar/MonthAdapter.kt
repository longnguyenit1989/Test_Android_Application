package com.example.testapplication.ui.calendar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.databinding.ItemDayBinding
import com.example.testapplication.extension.beGone
import com.example.testapplication.extension.beVisible
import com.example.testapplication.extension.setTextSizeFloat
import com.example.testapplication.utils.LunarCalendar
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters
import java.util.Date

class MonthAdapter(
    private val year: Int,
    private val month: Int
) : RecyclerView.Adapter<MonthAdapter.DayViewHolder>() {

    private val days = mutableListOf<LocalDate?>()

    init {
        val first = LocalDate.of(year, month, 1)
        val start = first.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val end = first.plusMonths(1).minusDays(1)
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))

        var day = start
        while (!day.isAfter(end)) {
            days.add(day)
            day = day.plusDays(1)
        }
    }

    override fun getItemCount() = days.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val redColor = holder.binding.root.context.getColor(R.color.red)
        val blackColor = holder.binding.root.context.getColor(R.color.black)
        val blueColor = holder.binding.root.context.getColor(R.color.blue)
        val violetColor = holder.binding.root.context.getColor(R.color.violet)
        val whiteColor = holder.binding.root.context.getColor(R.color.white)

        val textGoToWork = holder.binding.root.context.getString(R.string.go_to_work)

        val date = days[position] ?: return
        holder.binding.tvDayNumber.text = date.dayOfMonth.toString()

        val lunar = LunarCalendar.solarToLunar(
            Date.from(date.atStartOfDay(ZoneOffset.UTC).toInstant())
        )
        holder.binding.tvLunarDay.text = "${lunar.second}/${lunar.first}"

        if (date == LocalDate.now()) {
            holder.binding.viewTodayCircle.beVisible()
            holder.binding.tvDayNumber.setTextColor(blueColor)
        } else if (date.dayOfWeek == DayOfWeek.SUNDAY) {
            holder.binding.tvDayNumber.setTextColor(redColor)
            holder.binding.viewTodayCircle.beGone()
        } else {
            holder.binding.viewTodayCircle.beGone()
            holder.binding.tvDayNumber.setTextColor(blackColor)
        }

        val eventsLayout = holder.binding.llEvents
        eventsLayout.removeAllViews()

        if (date.dayOfWeek in DayOfWeek.MONDAY..DayOfWeek.FRIDAY) {
            if (date.dayOfMonth % 2 == 0) {
                val density = holder.binding.root.resources.displayMetrics.density
                val event = TextView(holder.binding.root.context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (12 * density).toInt()
                    ).apply {
                        topMargin = (2 * density).toInt()
                        leftMargin = (4 * density).toInt()
                        rightMargin = (4 * density).toInt()
                    }
                    text = textGoToWork
                    textSize = 8f
                    setTextColor(whiteColor)
                    gravity = Gravity.CENTER
                    setPadding(0, 0, 0, 0)

                    background = GradientDrawable().apply {
                        cornerRadius = 4 * density
                        setColor(violetColor)
                    }

                    isSingleLine = true
                    maxLines = 1
                    ellipsize = TextUtils.TruncateAt.END
                }
                eventsLayout.addView(event)
            }
        }

        holder.binding.root.setOnClickListener {
            showDayDetailDialog(holder, date, lunar)
        }
    }

    class DayViewHolder(val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("SetTextI18n")
    private fun showDayDetailDialog(holder: DayViewHolder, date: LocalDate, lunar: Pair<Int, Int>) {
        val context = holder.binding.root.context
        val builder = AlertDialog.Builder(context)

        val blackColor = context.getColor(R.color.black)
        val blueColor = context.getColor(R.color.blue)
        val greyColor = context.getColor(R.color.grey)

        val mediumTextSizePx = context.resources.getDimension(R.dimen.x_medium_text_size)
        val smallTextSizePx = context.resources.getDimension(R.dimen.small_text_size)

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }

        val solarText = TextView(context).apply {
            text =
                "📅 ${context.getString(R.string.solar_date)}: ${date.dayOfMonth}/${date.monthValue}/${date.year}"
            setTextColor(blackColor)
        }
        solarText.setTextSizeFloat(mediumTextSizePx)

        val lunarText = TextView(context).apply {
            text = "🌙 ${context.getString(R.string.lunar_date)}: ${lunar.second}/${lunar.first}"
            setTextColor(greyColor)
        }
        lunarText.setTextSizeFloat(mediumTextSizePx)

        layout.addView(solarText)
        layout.addView(lunarText)

        val events = mutableListOf<String>()
        if (date.dayOfWeek in DayOfWeek.MONDAY..DayOfWeek.FRIDAY && date.dayOfMonth % 2 == 0) {
            events.add(context.getString(R.string.go_to_work))
        }

        if (events.isNotEmpty()) {
            val title = TextView(context).apply {
                text = "\n📌 ${context.getString(R.string.events)}"
                setTextColor(blackColor)
            }
            title.setTextSizeFloat(smallTextSizePx)
            layout.addView(title)

            events.forEach { e ->
                val eventView = TextView(context).apply {
                    text = "• $e"
                    setTextColor(blueColor)
                }
                eventView.setTextSizeFloat(smallTextSizePx)
                layout.addView(eventView)
            }
        } else {
            val noneView = TextView(context).apply {
                text = "\n(⛱ ${context.getString(R.string.not_events)})"
                setTextColor(greyColor)
            }
            noneView.setTextSizeFloat(smallTextSizePx)
            layout.addView(noneView)
        }

        builder.setView(layout)
        builder.setPositiveButton(context.getString(R.string.close)) { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

}



