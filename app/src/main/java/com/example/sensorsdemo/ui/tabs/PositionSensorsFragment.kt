package com.example.sensorsdemo.ui.tabs

import androidx.lifecycle.Observer

class PositionSensorsFragment : BaseSensorsFragment() {

    companion object {
        fun newInstance() = PositionSensorsFragment()
    }

    override fun addObservers() {
        sensorsViewModel.positionSensorsLiveData.observe(this, Observer {
            sensorsAdapter.setData(it)
        })
    }

}