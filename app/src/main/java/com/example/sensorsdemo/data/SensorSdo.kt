package com.example.sensorsdemo.data

import android.hardware.Sensor
import java.text.DecimalFormat

data class SensorSdo(
        val sensor: Sensor?,
        val sensorName: String,
        val sensorType: Int,
        val isSensorAvailable: Boolean,
        val sensorCategory: SensorCategory,
        var measureUnit: SensorMeasureUnit,
        var sensorDisplayedData: String = ""
)

fun Sensor?.toSensorSdo(type: Int, sensorCategory: SensorCategory, measureUnit: SensorMeasureUnit,
                        backedSensorName: String? = null) = SensorSdo(
        sensor = this,
        sensorName = "Name: ${this?.name}\nBacked name (type): $backedSensorName",
        sensorType = type,
        isSensorAvailable = this != null,
        sensorCategory = sensorCategory,
        measureUnit = measureUnit
)

fun SensorSdo.setDisplayedData(data: FloatArray?) {
    val decFormat = DecimalFormat("#####0.000")
    sensorDisplayedData = with(StringBuilder()) {
        when {
            data?.isEmpty() ?: false -> append("Data is absent")
            else -> data?.forEachIndexed { index, sensorValue ->
                append(decFormat.format(sensorValue))
                append(" ")
                append(measureUnit.displayedUnit)
                if (index != data.size - 1)
                    append("\n")
            }
        }
        toString()
    }
}