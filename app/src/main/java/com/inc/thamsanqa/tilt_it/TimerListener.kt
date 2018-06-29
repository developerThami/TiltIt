package com.inc.thamsanqa.tilt_it

interface TimerListener{
    fun timerDone()
    fun onTick(millisLeft:Long)
}