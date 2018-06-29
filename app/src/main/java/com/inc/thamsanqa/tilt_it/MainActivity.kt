package com.inc.thamsanqa.tilt_it

import android.hardware.Sensor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.inc.thamsanqa.tilt_it.GeneratorUtil.Companion.randomDirectionGenerator
import com.inc.thamsanqa.tilt_it.GeneratorUtil.Companion.randomTimeGenerator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), TimerListener {


    private lateinit var mSensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    private val sSecond:Long = 1000

    private lateinit var actualTiltDirection: Direction
    private lateinit var expectedTiltDirection: Direction

    var orientation = FloatArray(3)
    private var pitch: Float = 0.0f
    private var roll: Float = 0.0f

    var scoreCount: Int = 0
    var answered:Boolean = false

    lateinit var sensor: AccelerometorSensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensor = AccelerometorSensor(mSensorManager,accelerometer)

//        Log.d("Angles", "X:$pitch , Y:$roll , Expected:$expectedTiltDirection  Actual:$actualTiltDirection")
//        actualTiltDirection = Direction.NONE
//        expectedTiltDirection = Direction.NONE
//        startGame()

    }

    public override fun onDestroy() {
        sensor.stopListener()
        super.onDestroy()
    }

    override fun onBackPressed() {
        sensor.stopListener()
        super.onBackPressed()
    }

    public override fun onResume() {
        sensor = AccelerometorSensor(mSensorManager,accelerometer)
        super.onResume()
    }

    override fun onPause() {
        sensor.stopListener()
        super.onPause()
    }

    private fun startNextRound() {
        SimpleTimer(this).startTimer(randomTimeGenerator() * sSecond, sSecond)
    }

    private fun startGame() {
        SimpleTimer(this).startTimer(3 * sSecond, sSecond)
    }

    override fun onTick(millisLeft: Long) {
        val value = millisLeft / 1000
        iv_center.text = value.toString()
    }

    override fun timerDone() {

        requestUserToTilt()
        startNextRound()

        Toast.makeText(this,"done",Toast.LENGTH_SHORT).show()
    }

    private fun requestUserToTilt() {

        expectedTiltDirection = randomDirectionGenerator()

        when (expectedTiltDirection) {
            Direction.UP -> tiltUp()
            Direction.DOWN -> tiltDown()
            Direction.LEFT -> tiltLeft()
            Direction.RIGHT -> tiltRight()
            else -> {
                noTilt()
            }
        }
    }

    private fun noTilt() {
        iv_down.visibility = View.INVISIBLE
        iv_left.visibility = View.INVISIBLE
        iv_right.visibility = View.INVISIBLE
        iv_up.visibility = View.INVISIBLE
    }

    private fun tiltRight() {
        iv_right.visibility = View.VISIBLE
        iv_down.visibility = View.INVISIBLE
        iv_left.visibility = View.INVISIBLE
        iv_up.visibility = View.INVISIBLE
    }

    private fun tiltLeft() {
        iv_left.visibility = View.VISIBLE
        iv_down.visibility = View.INVISIBLE
        iv_up.visibility = View.INVISIBLE
        iv_right.visibility = View.INVISIBLE
    }

    private fun tiltUp() {
        iv_up.visibility = View.VISIBLE
        iv_down.visibility = View.INVISIBLE
        iv_left.visibility = View.INVISIBLE
        iv_right.visibility = View.INVISIBLE
    }

    private fun tiltDown() {
        iv_down.visibility = View.VISIBLE
        iv_up.visibility = View.INVISIBLE
        iv_left.visibility = View.INVISIBLE
        iv_right.visibility = View.INVISIBLE
    }



}
