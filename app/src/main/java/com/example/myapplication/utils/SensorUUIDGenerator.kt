package com.example.myapplication.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build

class SensorUUIDGenerator(private val sensorManager: SensorManager) : SensorEventListener {

    private val sensorData = mutableListOf<Float>()
    private var onUUIDGenerated: ((String) -> Unit)? = null

    fun generateUUID(callback: (String) -> Unit) {
        onUUIDGenerated = callback
        val sensors = listOf(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE)
        sensors.forEach { type ->
            sensorManager.getDefaultSensor(type)?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.values?.let { sensorData.addAll(it.toList()) }
        if (sensorData.size >= 6) {
            sensorManager.unregisterListener(this)
            val hardwareInfo = "${Build.MANUFACTURER}-${Build.MODEL}-${Build.VERSION.SDK_INT}"
            val rawData = sensorData.joinToString(separator = ",") + hardwareInfo
            val uuid = rawData.toSHA256()
            onUUIDGenerated?.invoke(uuid)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
