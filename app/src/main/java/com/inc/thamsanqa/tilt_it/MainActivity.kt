package com.inc.thamsanqa.tilt_it

import android.content.Intent
import android.hardware.Sensor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorManager
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.inc.thamsanqa.tilt_it.GeneratorUtil.Companion.randomDirectionGenerator
import com.inc.thamsanqa.tilt_it.GeneratorUtil.Companion.randomTimeGenerator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), TimerListener {

    private lateinit var mSensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    private val sSecond: Long = 1000

    private lateinit var actualTiltDirection: Direction
    private lateinit var expectedTiltDirection: Direction

    var orientation = FloatArray(3)
    private lateinit var timer: SimpleTimer
    private var scoreCount: Int = 0

    private lateinit var sensor: AccelerometorSensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensor = AccelerometorSensor(mSensorManager, accelerometer)

        actualTiltDirection = Direction.NONE
        expectedTiltDirection = Direction.NONE

        action_new_game.setOnClickListener { restartGame() }
        action_quit.setOnClickListener { quitGame() }
        startGame()
    }

    private fun quitGame() {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
    }

    private fun restartGame() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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
        sensor = AccelerometorSensor(mSensorManager, accelerometer)
        super.onResume()
    }

    override fun onPause() {
        timer.stopTimer()
        sensor.stopListener()
        super.onPause()
    }

    private fun startNextRound() {
        timer = SimpleTimer(this, randomTimeGenerator() * sSecond, sSecond)
        timer.startTimer()
    }

    private fun startGame() {
        SimpleTimer(this, 3 * sSecond, sSecond).startTimer()
    }

    override fun onTick(millisLeft: Long) {
        val value = millisLeft / 1000
        iv_center.text = value.toString()
    }

    override fun timerDone() {

        val isTiltDirectionCorrect = checkTiltDirection(sensor.actualTiltDirection, expectedTiltDirection)
        val isTiltAngleSufficient = checkTiltAngle(sensor.pitch, sensor.roll, sensor.actualTiltDirection)

        grantPointIfTiltMeetsRequirements(isTiltDirectionCorrect, isTiltAngleSufficient)

        requestUserToTilt()

        if (scoreCount < 10) {
            startNextRound()
        } else {
            gameOver()
        }
    }

    private fun grantPointIfTiltMeetsRequirements(tiltDirectionCorrect: Boolean, tiltAngleSufficient: Boolean) {
        if (tiltAngleSufficient && tiltDirectionCorrect) {
            incrementPoints()
        }
    }

    private fun incrementPoints() {
        scoreCount++
        updateUserScore()
    }

    private fun gameOver() {
        timer.stopTimer()
        sensor.stopListener()

        noTilt()
        showToast("Congratulations!! You have reached 10 points")
    }

    private fun updateUserScore() {
        tv_score.text = "score : $scoreCount"
    }

    private fun checkTiltAngle(pitch: Float, roll: Float, actualTiltDirection: Direction): Boolean {

        /**
         * when tilting Right ,  (Check roll fif angle >= 50)
         * when tilting Left  ,  (Check roll fif angle >= 50)
         * when tilting Up    ,  (Check pitch if angle >= 50)
         * when tilting Down  ,  (Check pitch if angle >= 50)
         **/

        var angle = 0.0f
        when (actualTiltDirection) {
            Direction.UP -> {
                angle = Math.abs(pitch)
            }
            Direction.DOWN -> {
                angle = Math.abs(pitch)
            }
            Direction.LEFT -> {
                angle = Math.abs(roll)
            }
            Direction.RIGHT -> {
                angle = Math.abs(roll)
            }
            Direction.NONE -> {
                angle = 0.0f
            }
        }

        return angle >= 50.0
    }

    private fun checkTiltDirection(actualTiltDirection: Direction, expectedTiltDirection: Direction): Boolean {
        //Log.d("Angles", "Expected:$expectedTiltDirection  Actual:$actualTiltDirection")
        return actualTiltDirection == expectedTiltDirection
    }

    private fun requestUserToTilt() {

        expectedTiltDirection = randomDirectionGenerator()
        showToast(expectedTiltDirection.toString())

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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun noTilt() {
        iv_down.visibility = View.INVISIBLE
        iv_left.visibility = View.INVISIBLE
        iv_right.visibility = View.INVISIBLE
        iv_up.visibility = View.INVISIBLE
        iv_center.visibility = View.INVISIBLE
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
