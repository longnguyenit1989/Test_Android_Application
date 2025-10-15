package com.example.testapplication.model

import java.time.LocalDate

data class Day(
    val date: LocalDate,
    val lunarDate: Pair<Int?, Int?> = Pair<Int, Int>(-1, -1),
    val events: List<String> = emptyList()
)