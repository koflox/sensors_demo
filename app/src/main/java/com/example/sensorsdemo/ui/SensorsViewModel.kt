package com.example.sensorsdemo.ui

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sensorsdemo.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SensorsViewModel(app: Application) : AndroidViewModel(app) {

    private val motionSensors = mutableMapOf<Int, SensorSdo>()
    private val positionSensors = mutableMapOf<Int, SensorSdo>()
    private val environmentalSensors = mutableMapOf<Int, SensorSdo>()

    private val _motionSensorsLiveData = MutableLiveData<List<SensorSdo>>()
    val motionSensorsLiveData: LiveData<List<SensorSdo>>
        get() = _motionSensorsLiveData

    private val _positionSensorsLiveData = MutableLiveData<List<SensorSdo>>()
    val positionSensorsLiveData: LiveData<List<SensorSdo>>
        get() = _positionSensorsLiveData

    private val _environmentalSensorsLiveData = MutableLiveData<List<SensorSdo>>()
    val environmentalSensorsLiveData: LiveData<List<SensorSdo>>
        get() = _environmentalSensorsLiveData

    fun addSensor(sensor: Sensor?, type: Int, sensorCategory: SensorCategory, measureUnit: SensorMeasureUnit,
                  backedSensorName: String? = null) {
        val sensorSdo = sensor.toSensorSdo(type, sensorCategory, measureUnit, backedSensorName)
        when (sensorCategory) {
            SensorCategory.MOTION -> motionSensors[type] = sensorSdo
            SensorCategory.POSITION -> positionSensors[type] = sensorSdo
            SensorCategory.ENVIRONMENT -> environmentalSensors[type] = sensorSdo
        }
    }

    fun getSensors() = with(mutableListOf<Sensor?>()) {
        motionSensors.values.forEach { it.sensor?.run { add(this) } }
        positionSensors.values.forEach { it.sensor?.run { add(this) } }
        environmentalSensors.values.forEach { it.sensor?.run { add(this) } }
        this
    }

    fun onSensorChanged(event: SensorEvent?) = viewModelScope.launch(Dispatchers.Default) {
        motionSensors[event?.sensor?.type]?.run {
            setDisplayedData(event?.values)
            _motionSensorsLiveData.postValue(motionSensors.values.toList())
        }
        positionSensors[event?.sensor?.type]?.run {
            setDisplayedData(event?.values)
            _positionSensorsLiveData.postValue(positionSensors.values.toList())
        }
        environmentalSensors[event?.sensor?.type]?.run {
            setDisplayedData(event?.values)
            _environmentalSensorsLiveData.postValue(environmentalSensors.values.toList())
        }
    }

}