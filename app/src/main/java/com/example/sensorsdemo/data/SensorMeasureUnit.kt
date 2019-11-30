package com.example.sensorsdemo.data

enum class SensorMeasureUnit(val displayedUnit: String) {
    M_S_2("m/s^2"),
    RAD_S("rad/s"),
    UNITLESS(""),
    STEPS("steps"),
    CM("cm"),
    CELSIUS("°C"),
    LIGHT("lx"),
    PRESSURE("hPa or mbar"),
    HUMIDITY("%"),
    U_T("μT")
}