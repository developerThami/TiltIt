package com.inc.thamsanqa.tilt_it

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorManager
import android.util.Log
import android.view.View
import com.inc.thamsanqa.tilt_it.R.id.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    var mGravity: FloatArray? = null

    var orientation = FloatArray(3)
    private var pitch: Float = 0.0f
    private var roll: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        initListeners()
    }

    private fun initListeners() {
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {


        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values

            val x = event.values[0].toDouble()
            val y = event.values[1].toDouble()
            val z = event.values[2].toDouble()

            roll = (Math.atan2(y, z) * 57.3).toFloat()
            pitch = (Math.atan2((-x), Math.sqrt(y * y + z * z)) * 57.3).toFloat()

            Log.d("Angles", "X:$pitch , Y:$roll")

        }

    }

    public override fun onDestroy() {
        mSensorManager.unregisterListener(this)
        super.onDestroy()
    }

    override fun onBackPressed() {
        mSensorManager.unregisterListener(this)
        super.onBackPressed()
    }

    public override fun onResume() {
        initListeners()
        super.onResume()
    }

    override fun onPause() {
        mSensorManager.unregisterListener(this)
        super.onPause()
    }
}
