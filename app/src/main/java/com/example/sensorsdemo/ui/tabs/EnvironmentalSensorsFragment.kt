package com.example.sensorsdemo.ui.tabs

import androidx.lifecycle.Observer

class EnvironmentalSensorsFragment : BaseSensorsFragment() {

    companion object {
        fun newInstance() = EnvironmentalSensorsFragment()
    }

    override fun addObservers() {
        sensorsViewModel.environmentalSensorsLiveData.observe(this, Observer {
            sensorsAdapter.setData(it)
        })
    }

}