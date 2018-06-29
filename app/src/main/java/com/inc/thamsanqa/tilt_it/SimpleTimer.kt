package com.inc.thamsanqa.tilt_it

import android.os.CountDownTimer
import com.inc.thamsanqa.tilt_it.R.id.iv_center
import kotlinx.android.synthetic.main.activity_main.*

class SimpleTimer(var listener: TimerListener, duration: Long, interval: Long) {

    var timer: CountDownTimer = object : CountDownTimer(duration, interval) {
        override fun onTick(millisUntilFinished: Long) {
            listener.onTick(millisUntilFinished)
        }

        override fun onFinish() {
            listener.timerDone()
        }
    }

    fun startTimer() {
        timer.start()
    }

    fun stopTimer() {
        timer.cancel()
    }
}
