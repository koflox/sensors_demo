package com.example.sensorsdemo

import android.content.Context
import android.hardware.SensorManager
import android.view.OrientationEventListener

class OrientationManager(context: Context, rate: Int = SensorManager.SENSOR_DELAY_NORMAL) : OrientationEventListener(context, rate) {

    enum class ScreenOrientation {
        REVERSED_LANDSCAPE, LANDSCAPE, PORTRAIT, REVERSED_PORTRAIT
    }

    interface OrientationListener {

        fun onOrientationChange(screenOrientation: ScreenOrientation)
    }

    lateinit var orientationListener: OrientationListener

    private var screenOrientation: ScreenOrientation = ScreenOrientation.PORTRAIT

    override fun onOrientationChanged(orientation: Int) {
        if (orientation == ORIENTATION_UNKNOWN) {
            return
        }
        val newOrientation: ScreenOrientation = when (orientation) {
            in 60..140 -> ScreenOrientation.REVERSED_LANDSCAPE
            in 140..220 -> ScreenOrientation.REVERSED_PORTRAIT
            in 220..300 -> ScreenOrientation.LANDSCAPE
            else -> ScreenOrientation.PORTRAIT
        }
        if (newOrientation != screenOrientation) {
            screenOrientation = newOrientation
            orientationListener.onOrientationChange(screenOrientation)
        }
    }

}

fun OrientationManager.ScreenOrientation?.getRotationValue(): Float {
    return when (this) {
        OrientationManager.ScreenOrientation.REVERSED_LANDSCAPE -> -90F
        OrientationManager.ScreenOrientation.LANDSCAPE -> 90F
        OrientationManager.ScreenOrientation.PORTRAIT -> 0F
        OrientationManager.ScreenOrientation.REVERSED_PORTRAIT -> 180F
        else -> -90F
    }
}
