package com.example.testapplication.utils

import com.example.testapplication.model.Day
import java.time.YearMonth

object CalendarCache {
    private val cache = mutableMapOf<YearMonth, List<Day>>()

    fun getOrGenerate(year: Int, month: Int, generator: () -> List<Day>): List<Day> {
        val key = YearMonth.of(year, month)
        return cache.getOrPut(key) { generator() }
    }
}
