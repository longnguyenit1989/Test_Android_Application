package com.example.testapplication.ui.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testapplication.R
import com.example.testapplication.base.BaseFragment
import com.example.testapplication.databinding.FragmentMonthBinding
import com.example.testapplication.extension.setTextSizeFloat
import com.example.testapplication.model.Day
import com.example.testapplication.utils.CalendarCache
import com.example.testapplication.utils.LunarCalendar
import java.time.*
import java.time.temporal.TemporalAdjusters
import java.util.*

class MonthFragment : BaseFragment<FragmentMonthBinding>() {

    private var monthAdapter = MonthAdapter { day ->
        showDayDetailDialog(day)
    }

    companion object {
        private const val KEY_YEAR = "year"
        private const val KEY_MONTH = "month"
        private const val THICK_NESS_STROKE = 1
        private const val SPAN_COUNT = 7

        fun newInstance(year: Int, month: Int) = MonthFragment().apply {
            arguments = bundleOf(KEY_YEAR to year, KEY_MONTH to month)
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMonthBinding {
        return FragmentMonthBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val year = requireArguments().getInt(KEY_YEAR)
        val month = requireArguments().getInt(KEY_MONTH)

        binding.recyclerMonth.apply {
            layoutManager = GridLayoutManager(requireContext(), 7)
            adapter = monthAdapter
            addItemDecoration(
                GridDividerDecoration(
                    ContextCompat.getColor(context, R.color.grey),
                    THICK_NESS_STROKE,
                    SPAN_COUNT
                )
            )
        }


        val days = CalendarCache.getOrGenerate(year, month) {
            generateDays(year, month)
        }
        monthAdapter.submitList(days)
    }

    private fun generateDays(year: Int, month: Int): List<Day> {
        val result = mutableListOf<Day>()
        val first = LocalDate.of(year, month, 1)
        val start = first.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val end = first.plusMonths(1).minusDays(1)
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))

        var day = start
        while (!day.isAfter(end)) {
            val lunar = LunarCalendar.solarToLunar(
                Date.from(day.atStartOfDay(ZoneOffset.UTC).toInstant())
            )

            val events = mutableListOf<String>()
            if (day.dayOfWeek in DayOfWeek.MONDAY..DayOfWeek.FRIDAY && day.dayOfMonth % 2 == 0) {
                events.add(getString(R.string.go_to_work))
            }

            result.add(Day(day, lunar, events))
            day = day.plusDays(1)
        }
        return result
    }

    @SuppressLint("SetTextI18n")
    private fun showDayDetailDialog(day: Day) {
        val context = requireContext()
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

        val date = day.date
        val solarText = TextView(context).apply {
            text =
                "📅 ${context.getString(R.string.solar_date)}: ${date.dayOfMonth}/${date.monthValue}/${date.year}"
            setTextColor(blackColor)
        }
        solarText.setTextSizeFloat(mediumTextSizePx)

        val lunarText = TextView(context).apply {
            text =
                "🌙 ${context.getString(R.string.lunar_date)}: ${day.lunarDate.second}/${day.lunarDate.first}"
            setTextColor(greyColor)
        }
        lunarText.setTextSizeFloat(mediumTextSizePx)

        layout.addView(solarText)
        layout.addView(lunarText)

        if (day.events.isNotEmpty()) {
            val title = TextView(context).apply {
                text = "\n📌 ${context.getString(R.string.events)}"
                setTextColor(blackColor)
            }
            title.setTextSizeFloat(smallTextSizePx)
            layout.addView(title)

            day.events.forEach { e ->
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
