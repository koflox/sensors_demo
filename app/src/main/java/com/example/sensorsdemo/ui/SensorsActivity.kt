package com.example.sensorsdemo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.sensorsdemo.R
import com.example.sensorsdemo.data.SensorCategory
import com.example.sensorsdemo.data.SensorMeasureUnit
import kotlinx.android.synthetic.main.activity_main.*

class SensorsActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var sensorsViewModel: SensorsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vpSensors.adapter = SectionsPagerAdapter(this, supportFragmentManager)
        tabs.setupWithViewPager(vpSensors)

        sensorsViewModel = ViewModelProviders.of(this).get(SensorsViewModel::class.java)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        initMotionSensors()
        initPositionSensors()
        initEnvironmentalSensors()
    }

    override fun onResume() {
        super.onResume()
        sensorsViewModel.getSensors().forEach { sensor ->
            sensorManager.registerListener(this@SensorsActivity, sensor, SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this@SensorsActivity)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        sensorsViewModel.onSensorChanged(event)
    }

    @SuppressLint("InlinedApi")
    private fun initMotionSensors() {
        val sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        val sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        val sensorLinearAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        val sensorRotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val sensorStepCounter = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            else -> null
        }
        val sensorAccelerometerUncalibrated = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED)
            else -> null
        }
        val sensorGyroscopeUncalibrated = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 -> sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED)
            else -> null
        }
        sensorsViewModel.run {
            addSensor(sensorAccelerometer, Sensor.TYPE_ACCELEROMETER, SensorCategory.MOTION,
                    SensorMeasureUnit.M_S_2, "TYPE_ACCELEROMETER")
            addSensor(sensorGravity, Sensor.TYPE_GRAVITY, SensorCategory.MOTION,
                    SensorMeasureUnit.M_S_2, "TYPE_GRAVITY")
            addSensor(sensorGyroscope, Sensor.TYPE_GYROSCOPE, SensorCategory.MOTION,
                    SensorMeasureUnit.RAD_S, "TYPE_GYROSCOPE")
            addSensor(sensorLinearAcceleration, Sensor.TYPE_LINEAR_ACCELERATION, SensorCategory.MOTION,
                    SensorMeasureUnit.M_S_2, "TYPE_LINEAR_ACCELERATION")
            addSensor(sensorRotationVector, Sensor.TYPE_ROTATION_VECTOR, SensorCategory.MOTION,
                    SensorMeasureUnit.UNITLESS, "TYPE_ROTATION_VECTOR")
            sensorStepCounter?.run {
                addSensor(this, Sensor.TYPE_STEP_COUNTER, SensorCategory.MOTION,
                        SensorMeasureUnit.STEPS, "TYPE_STEP_COUNTER")
            } ?: kotlin.run {
                addSensor(null, 19, SensorCategory.MOTION, SensorMeasureUnit.UNITLESS,
                        "TYPE_STEP_COUNTER")
            }
            sensorAccelerometerUncalibrated?.run {
                addSensor(this, Sensor.TYPE_ACCELEROMETER_UNCALIBRATED, SensorCategory.MOTION,
                        SensorMeasureUnit.M_S_2, "TYPE_ACCELEROMETER_UNCALIBRATED")
            } ?: kotlin.run {
                addSensor(null, 35, SensorCategory.MOTION,
                        SensorMeasureUnit.UNITLESS, "TYPE_ACCELEROMETER_UNCALIBRATED")
            }
            sensorGyroscopeUncalibrated?.run {
                addSensor(this, Sensor.TYPE_GYROSCOPE_UNCALIBRATED, SensorCategory.MOTION,
                        SensorMeasureUnit.RAD_S, "TYPE_GYROSCOPE_UNCALIBRATED")
            } ?: kotlin.run {
                addSensor(null, 16, SensorCategory.MOTION, SensorMeasureUnit.UNITLESS,
                        "TYPE_GYROSCOPE_UNCALIBRATED")
            }
        }
    }

    @SuppressLint("InlinedApi")
    private fun initPositionSensors() {
        val sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        val sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val sensorGameRotationVector = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 -> sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
            else -> null
        }
        val sensorGeomagneticRotationVector = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)
            else -> null
        }
        val sensorMagneticFieldUncalibrated = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 -> sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED)
            else -> null
        }
        sensorsViewModel.run {
            addSensor(sensorMagneticField, Sensor.TYPE_MAGNETIC_FIELD, SensorCategory.POSITION,
                    SensorMeasureUnit.U_T, "TYPE_MAGNETIC_FIELD")
            addSensor(sensorProximity, Sensor.TYPE_PROXIMITY, SensorCategory.POSITION, SensorMeasureUnit.CM,
                    "TYPE_PROXIMITY")
            sensorGameRotationVector?.run {
                addSensor(this, Sensor.TYPE_GAME_ROTATION_VECTOR, SensorCategory.POSITION,
                        SensorMeasureUnit.UNITLESS, "TYPE_GAME_ROTATION_VECTOR")
            } ?: kotlin.run {
                addSensor(null, 15, SensorCategory.POSITION, SensorMeasureUnit.UNITLESS,
                        "TYPE_GAME_ROTATION_VECTOR")
            }
            sensorGeomagneticRotationVector?.run {
                addSensor(this, Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, SensorCategory.POSITION,
                        SensorMeasureUnit.UNITLESS, "TYPE_GEOMAGNETIC_ROTATION_VECTOR")
            } ?: kotlin.run {
                addSensor(null, 20, SensorCategory.POSITION, SensorMeasureUnit.UNITLESS,
                        "TYPE_GEOMAGNETIC_ROTATION_VECTOR")
            }
            sensorMagneticFieldUncalibrated?.run {
                addSensor(this, Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED, SensorCategory.POSITION,
                        SensorMeasureUnit.U_T, "TYPE_MAGNETIC_FIELD_UNCALIBRATED")
            } ?: kotlin.run {
                addSensor(null, 14, SensorCategory.POSITION, SensorMeasureUnit.UNITLESS,
                        "TYPE_MAGNETIC_FIELD_UNCALIBRATED")
            }
        }
    }

    private fun initEnvironmentalSensors() {
        val sensorAmbientTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        val sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        val sensorPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        val sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        sensorsViewModel.run {
            addSensor(sensorAmbientTemperature, Sensor.TYPE_AMBIENT_TEMPERATURE, SensorCategory.ENVIRONMENT,
                    SensorMeasureUnit.CELSIUS, "TYPE_AMBIENT_TEMPERATURE")
            addSensor(sensorLight, Sensor.TYPE_LIGHT, SensorCategory.ENVIRONMENT, SensorMeasureUnit.LIGHT,
                    "TYPE_LIGHT")
            addSensor(sensorPressure, Sensor.TYPE_PRESSURE, SensorCategory.ENVIRONMENT,
                    SensorMeasureUnit.PRESSURE, "TYPE_PRESSURE")
            addSensor(sensorHumidity, Sensor.TYPE_RELATIVE_HUMIDITY, SensorCategory.ENVIRONMENT,
                    SensorMeasureUnit.HUMIDITY, "TYPE_RELATIVE_HUMIDITY")
        }
    }

}





