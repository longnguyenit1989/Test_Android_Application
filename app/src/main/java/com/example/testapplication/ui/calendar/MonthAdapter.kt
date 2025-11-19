package com.example.testapplication.ui.calendar

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.databinding.ItemDayBinding
import com.example.testapplication.extension.beGone
import com.example.testapplication.extension.beVisible
import com.example.testapplication.model.Day
import java.time.DayOfWeek
import java.time.LocalDate

class MonthAdapter(
    private val onDayClick: (Day) -> Unit
) : ListAdapter<Day, MonthAdapter.DayViewHolder>(DayDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = getItem(position)
        val date = day.date
        val context = holder.binding.root.context

        val redColor = context.getColor(R.color.red)
        val blackColor = context.getColor(R.color.black)
        val violetColor = context.getColor(R.color.violet)
        val whiteColor = context.getColor(R.color.white)

        holder.binding.tvDayNumber.text = date.dayOfMonth.toString()
        holder.binding.tvLunarDay.text = "${day.lunarDate.second}/${day.lunarDate.first}"

        when {
            date == LocalDate.now() -> {
                holder.binding.viewTodayCircle.beVisible()
                holder.binding.tvDayNumber.setTextColor(whiteColor)
            }

            date.dayOfWeek == DayOfWeek.SUNDAY -> {
                holder.binding.viewTodayCircle.beGone()
                holder.binding.tvDayNumber.setTextColor(redColor)
            }

            else -> {
                holder.binding.viewTodayCircle.beGone()
                holder.binding.tvDayNumber.setTextColor(blackColor)
            }
        }

        val eventsLayout = holder.binding.llEvents
        eventsLayout.removeAllViews()
        val density = context.resources.displayMetrics.density
        day.events.forEach { e ->
            val event = TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (12 * density).toInt()
                ).apply {
                    topMargin = (2 * density).toInt()
                    leftMargin = (4 * density).toInt()
                    rightMargin = (4 * density).toInt()
                }
                text = e
                textSize = 8f
                setTextColor(whiteColor)
                background = GradientDrawable().apply {
                    cornerRadius = 4 * density
                    setColor(violetColor)
                }
                gravity = android.view.Gravity.CENTER
                isSingleLine = true
                ellipsize = TextUtils.TruncateAt.END
            }
            eventsLayout.addView(event)
        }

        holder.binding.root.setOnClickListener { onDayClick(day) }
    }

    class DayViewHolder(val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root)

    class DayDiffCallback : DiffUtil.ItemCallback<Day>() {
        override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
            return oldItem == newItem
        }
    }
}





