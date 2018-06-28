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
import java.util.*


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    private var mGravity: FloatArray? = null

    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NONE
    }

    lateinit var tiltDirection: Direction

    lateinit var expectedTiltDirection: Direction
    lateinit var actualtiltDirection: Direction

    var time: Long = 0

    var orientation = FloatArray(3)
    private var pitch: Float = 0.0f
    private var roll: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        initListeners()
        tiltDirection = Direction.NONE

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

            tiltDirection = determineTiltDirection(x, y)
            roll = determineRoll(y, z)
            pitch = determinePitch(x, y, z)


            Log.d("Angles", "X:$pitch , Y:$roll , Direction:$tiltDirection")
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

    private var timer: CountDownTimer = object : CountDownTimer(totalMilliSeconds, intervalMilliSeconds) {
        override fun onTick(millisUntilFinished: Long) {
            totalMilliSeconds = millisUntilFinished
        }

        override fun onFinish() {
            startGame()
        }
    }

    private fun startGame() {

    }

    private fun randomDirectionGenerator(): Direction {

        val randomNumber = Random().nextInt(4) + 1

        when (randomNumber) {
            0 -> return Direction.DOWN
            1 -> return Direction.DOWN
            2 -> return Direction.LEFT
            3 -> return Direction.RIGHT
        }

        return Direction.NONE
    }
}
