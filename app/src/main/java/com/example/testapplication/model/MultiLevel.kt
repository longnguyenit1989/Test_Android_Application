package com.example.testapplication.model

data class Teacher(
    val name: String
)

data class ClassRoom(
    val name: String,
    val teachers: List<Teacher>,
    var isExpanded: Boolean = false
)

data class School(
    val name: String,
    val classes: List<ClassRoom>,
    var isExpanded: Boolean = false
)
