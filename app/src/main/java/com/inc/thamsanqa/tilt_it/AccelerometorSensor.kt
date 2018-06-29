package com.inc.thamsanqa.tilt_it

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class AccelerometorSensor(private val mSensorManager: SensorManager, accelerometer: Sensor) : SensorEventListener {

     lateinit var actualTiltDirection: Direction
     var pitch: Float = 0.0f
     var roll: Float = 0.0f

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {

            val x = event.values[0].toDouble()
            val y = event.values[1].toDouble()
            val z = event.values[2].toDouble()

            actualTiltDirection = determineTiltDirection(x, y)
            roll = determineRoll(y, z)
            pitch = determinePitch(x, y, z)

            Log.d("Angles", "X:$pitch , Y:$roll ,  Actual:$actualTiltDirection")
        }
    }

    private fun determinePitch(x: Double, y: Double, z: Double): Float {
        return (Math.atan2((-x), Math.sqrt(y * y + z * z)) * 57.3).toFloat()
    }

    private fun determineRoll(y: Double, z: Double): Float {
        return (Math.atan2(y, z) * 57.3).toFloat()
    }

    private fun determineTiltDirection(x: Double, y: Double): Direction {

        if (Math.abs(x) > Math.abs(y)) {

            if (x < 0)
                return Direction.UP

            if (x > 0)
                return Direction.DOWN

        } else {

            if (y < 0)
                return Direction.LEFT

            if (y > 0)
                return Direction.RIGHT
        }

        if (x > -2 && x < 2 && y > -2 && y < 2) {
            return Direction.NONE
        }

        return Direction.NONE
    }

    fun stopListener() {
        mSensorManager.unregisterListener(this)
    }

    init {
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }
}