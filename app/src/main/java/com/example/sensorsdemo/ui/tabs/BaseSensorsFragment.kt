package com.example.sensorsdemo.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.sensorsdemo.R
import com.example.sensorsdemo.ui.SensorsViewModel
import kotlinx.android.synthetic.main.fragment_sensors.*

abstract class BaseSensorsFragment : Fragment() {

    protected lateinit var sensorsViewModel: SensorsViewModel
    protected lateinit var sensorsAdapter: SensorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorsViewModel = ViewModelProviders.of(activity!!).get(SensorsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sensors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sensorsAdapter = SensorsAdapter()
        rvSensors.apply {
            layoutManager = LinearLayoutManager(activity!!, VERTICAL, false)
            adapter = sensorsAdapter
            setHasFixedSize(true)
        }
        addObservers()
    }

    abstract fun addObservers()

}