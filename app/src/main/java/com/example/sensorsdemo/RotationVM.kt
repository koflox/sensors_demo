package com.example.sensorsdemo

import android.app.Activity
import android.app.Application
import android.graphics.drawable.GradientDrawable
import android.hardware.SensorManager
import android.util.Size
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class RotationVM(app: Application) : AndroidViewModel(app) {

    companion object {
        const val SCALE_FACTOR_OFFSET = 0.35F
    }

    private var flag: Boolean = false

    var windowSize: Size? = null
    private val colorPointerAligned = ContextCompat.getColor(app, R.color.color_pointer_aligned)
    private val colorPointerUnaligned =
        ContextCompat.getColor(app, R.color.color_pointer_unaligned)

    private val _deviceRotation = MutableLiveData<Triple<Float, Float, Float>>()
    private val _deviceOrientationDegrees = MutableLiveData<Float>()
    private val _deviceOffset = MutableLiveData<Pair<Float, Float>>()
    private val _pointerBg = MutableLiveData<GradientDrawable>().apply {
        value = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(colorPointerAligned)
        }
    }

    val deviceOffset: LiveData<Pair<Float, Float>> = _deviceOffset
    val deviceRotation: LiveData<Triple<Float, Float, Float>> = _deviceRotation
    val deviceOrientationDegrees: LiveData<Float> = _deviceOrientationDegrees
    val pointerBg: LiveData<GradientDrawable> = _pointerBg

    private var deviceOrientation = OrientationManager.ScreenOrientation.PORTRAIT
    private val initialDeviceRotation = FloatArray(3)

    private val sensorsListener = object : SensorsWrapper.SensorsListener {
        override fun onRotationChanged(azimuth: Float, pitch: Float, roll: Float) {
            handleRotation(roll, azimuth, pitch)
        }
    }

    private val orientationListener = object : OrientationManager.OrientationListener {
        override fun onOrientationChange(screenOrientation: OrientationManager.ScreenOrientation) {
            deviceOrientation = screenOrientation
        }
    }
    private val sensorsWrapper =
        SensorsWrapper(app.getSystemService(Activity.SENSOR_SERVICE) as SensorManager)
    private val orientationManager = OrientationManager(app).apply {
        orientationListener = this@RotationVM.orientationListener
    }

    fun onTrackButtonClick() {
        flag = !flag
        if (!flag) {
            _deviceOffset.postValue(Pair(0F, 0F))
            _pointerBg.postValue(pointerBg.value?.apply {
                setColor(colorPointerAligned)
            })
        }
    }

    fun startDeviceRotationListening() {
        sensorsWrapper.startListeningRotation(sensorsListener)
        orientationManager.enable()
    }

    fun stopDeviceRotationListening() {
        sensorsWrapper.stopListening()
        orientationManager.disable()
    }

    private fun handleRotation(roll: Float, azimuth: Float, pitch: Float) {
        when {
            flag -> windowSize?.run {
                var rotation: Float
                val rotationX: Float // vertical
                val rotationY: Float // horizontal
                when (deviceOrientation) {
                    OrientationManager.ScreenOrientation.REVERSED_LANDSCAPE -> {
                        rotation = initialDeviceRotation[2] - roll
                        rotationX = azimuth - initialDeviceRotation[0]
                        rotationY = pitch - initialDeviceRotation[1]
                    }
                    OrientationManager.ScreenOrientation.LANDSCAPE -> {
                        rotation = initialDeviceRotation[2] - roll
                        rotationX = initialDeviceRotation[0] - azimuth
                        rotationY = initialDeviceRotation[1] - pitch
                    }
                    OrientationManager.ScreenOrientation.PORTRAIT -> {
                        rotation = initialDeviceRotation[2] - roll
                        rotationX = initialDeviceRotation[1] - pitch
                        rotationY = azimuth - initialDeviceRotation[0]
                    }
                    OrientationManager.ScreenOrientation.REVERSED_PORTRAIT -> {
                        rotation = initialDeviceRotation[2] - roll
                        rotationX = pitch - initialDeviceRotation[1]
                        rotationY = initialDeviceRotation[0] - azimuth
                    }
                }
                rotation += deviceOrientation.getRotationValue()
                displayOffsets(rotation, rotationX, rotationY)
            } ?: return
            else -> {
                initialDeviceRotation[0] = azimuth
                initialDeviceRotation[1] = pitch
                initialDeviceRotation[2] = roll
            }
        }
    }

    private fun Size.displayOffsets(
        rotation: Float,
        rotationX: Float,
        rotationY: Float
    ) {
        _deviceRotation.postValue(Triple(rotation, rotationX, rotationY))

        val maxWidthBorder = width * SCALE_FACTOR_OFFSET
        val maxHeightBorder = height * SCALE_FACTOR_OFFSET

        val offsetY = rotationX / 90F * maxHeightBorder
        val offsetX = -rotationY / 90F * maxWidthBorder
        _deviceOffset.postValue(Pair(offsetX, offsetY))

        val offsetXAbs = abs(offsetX)
        val offsetYAbs = abs(offsetY)
        val maxOffset = max(offsetXAbs, offsetYAbs)
        val maxBorder = max(maxWidthBorder, maxHeightBorder)
        val fraction = min(maxOffset / maxBorder, 1.0F)
        val currentColor = ArgbEvaluator.getInstance().evaluate(
            fraction,
            colorPointerAligned,
            colorPointerUnaligned
        ) as Int
        _pointerBg.postValue(pointerBg.value?.apply {
            setColor(currentColor)
        })
    }

}