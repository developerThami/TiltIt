package com.inc.thamsanqa.tilt_it

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorManager
import android.os.CountDownTimer
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    private var mGravity: FloatArray? = null

    var time:Long = 0

    var orientation = FloatArray(3)
    private var pitch: Float = 0.0f
    private var roll: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        //initListeners()

        //timer.start()
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
        timer.cancel()
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

    var totalMilliSeconds: Long = 1000
    var intervalMilliSeconds: Long = 1

    private var timer: CountDownTimer = object : CountDownTimer( totalMilliSeconds, intervalMilliSeconds) {
        override fun onTick(millisUntilFinished: Long) {
            totalMilliSeconds = millisUntilFinished
        }

        override fun onFinish() {
            startGame()
        }
    }

    private fun startGame() {

    }
}
