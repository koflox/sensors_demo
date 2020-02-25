package com.example.sensorsdemo

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SensorsWrapper(private val sensorManager: SensorManager) : SensorEventListener {

    companion object {

        const val ONE_RADIAN_IN_DEGREES = 57.2957795F

    }

    interface SensorsListener {

        fun onRotationChanged(azimuth: Float, pitch: Float, roll: Float)

    }

    private val mRotationSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    private var lastAccuracy: Int = SensorManager.SENSOR_STATUS_UNRELIABLE
    private lateinit var sensorsListener: SensorsListener

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        lastAccuracy = accuracy
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when {
            lastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE -> return
            event?.sensor == mRotationSensor -> calculateOrientation(event.values)
        }
    }

    fun startListeningRotation(sensorsListener: SensorsListener) {
        this.sensorsListener = sensorsListener
        sensorManager.registerListener(this, mRotationSensor, 16_000)
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    private fun calculateOrientation(rotationVector: FloatArray) {
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector)

        val worldAxisForDeviceAxisX: Int = SensorManager.AXIS_X
        val worldAxisForDeviceAxisY: Int = SensorManager.AXIS_Z

        val adjustedRotationMatrix = FloatArray(9)
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisForDeviceAxisX,
                worldAxisForDeviceAxisY, adjustedRotationMatrix)

        // Transform rotation matrix into azimuth/pitch/roll
        // Explanation of getOrientation() axis
        // https://google-developer-training.github.io/android-developer-advanced-course-concepts/unit-1-expand-the-user-experience/lesson-3-sensors/3-2-c-motion-and-position-sensors/3-2-c-motion-and-position-sensors.html
        val orientation = FloatArray(3)
        SensorManager.getOrientation(adjustedRotationMatrix, orientation)

        val azimuth = orientation[0] * ONE_RADIAN_IN_DEGREES
        val pitch = orientation[1] * ONE_RADIAN_IN_DEGREES
        val roll = orientation[2] * ONE_RADIAN_IN_DEGREES

        sensorsListener.onRotationChanged(azimuth, pitch, roll)
    }

}
