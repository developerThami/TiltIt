package com.inc.thamsanqa.tilt_it

import java.util.*

class GeneratorUtil {

    companion object {

        fun randomDirectionGenerator(): Direction {

            val randomNumber = Random().nextInt(4)

            return when (randomNumber) {
                0 -> Direction.UP
                1 -> Direction.DOWN
                2 -> Direction.LEFT
                3 -> Direction.RIGHT
                else -> {
                    Direction.NONE
                }
            }
        }


        fun randomTimeGenerator(): Long {
            val randomNumber = Random().nextInt(7) + 2
            return randomNumber.toLong()
        }
    }

}

