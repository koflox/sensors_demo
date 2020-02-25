package com.example.sensorsdemo

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_rotation.*

class RotationActivity : AppCompatActivity() {

    private lateinit var viewModel: RotationVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rotation)
        viewModel = ViewModelProviders.of(this).get(RotationVM::class.java).apply {
            val displayMetrics = DisplayMetrics().also {
                windowManager.defaultDisplay.getMetrics(it)
            }
            val windowSize = Size(displayMetrics.widthPixels, displayMetrics.heightPixels)
            this.windowSize = windowSize
        }

        initViews()
        addObservers()
    }

    override fun onStart() {
        super.onStart()
        viewModel.startDeviceRotationListening()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopDeviceRotationListening()
    }

    private fun initViews() {
        btnTrackRotation.setOnClickListener {
            viewModel.onTrackButtonClick()
        }
    }

    private fun addObservers() {
        viewModel.deviceOffset.observe(this, Observer {
            it?.let { (offsetX, offsetY) ->
                viewInitialDeviceRotation.translationX = offsetX
                viewInitialDeviceRotation.translationY = offsetY
            }
        })
        viewModel.pointerBg.observe(this, Observer {
            it?.let { bg ->
                viewInitialDeviceRotation.background = bg
            }
        })
    }

}