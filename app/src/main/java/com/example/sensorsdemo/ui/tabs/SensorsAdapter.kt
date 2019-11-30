package com.example.sensorsdemo.ui.tabs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sensorsdemo.R
import com.example.sensorsdemo.data.SensorSdo
import kotlinx.android.synthetic.main.item_sensor.view.*

class SensorsAdapter : RecyclerView.Adapter<SensorsAdapter.SensorViewHolder>() {

    private val sensors = mutableListOf<SensorSdo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sensor, parent, false)
        return SensorViewHolder(view)
    }

    override fun getItemCount(): Int = sensors.size

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        holder.onBind(sensors[position])
    }

    fun setData(data: List<SensorSdo>) = sensors.run {
        clear()
        addAll(data)
        notifyDataSetChanged()
    }

    class SensorViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun onBind(sensorSdo: SensorSdo) {
            view.run {
                tvSensorName.text = sensorSdo.sensorName
                tvSensorAvailability.text = sensorSdo.isSensorAvailable.toString()
                tvSensorValues.text = sensorSdo.sensorDisplayedData
            }
        }

    }

}