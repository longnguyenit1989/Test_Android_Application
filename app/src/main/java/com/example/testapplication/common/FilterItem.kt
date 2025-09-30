package com.example.testapplication.common

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

data class FilterItem(
    val name: String,
    val filter: GPUImageFilter?,
    var isSelected: Boolean? = false
)
