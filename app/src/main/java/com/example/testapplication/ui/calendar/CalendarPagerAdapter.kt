package com.example.testapplication.ui.calendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.time.YearMonth

class CalendarPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val months = mutableListOf<YearMonth>()

    init {
        val now = YearMonth.now()
        for (i in -12..12) {
            months.add(now.plusMonths(i.toLong()))
        }
    }

    override fun getItemCount() = months.size

    override fun createFragment(position: Int): Fragment {
        val ym = months[position]
        return MonthFragment.newInstance(ym.year, ym.monthValue)
    }

    fun getMonthAt(position: Int): YearMonth = months[position]
}


