package com.example.coincapapp.utils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class DecimalValueFormatter : ValueFormatter() {
    private val format = DecimalFormat("#.###")

    override fun getPointLabel(entry: Entry?): String {

        return if ((entry?.x ?: 0.0f) % 3 == 0.0f) format.format(entry?.y ?: 0f) else ""
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return super.getAxisLabel(value, axis)
    }
}