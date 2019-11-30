package com.example.sensorsdemo.ui.tabs

import androidx.lifecycle.Observer

class MotionSensorsFragment : BaseSensorsFragment() {

    companion object {
        fun newInstance() = MotionSensorsFragment()
    }

    override fun addObservers() {
        sensorsViewModel.motionSensorsLiveData.observe(this, Observer {
            sensorsAdapter.setData(it)
        })
    }

}