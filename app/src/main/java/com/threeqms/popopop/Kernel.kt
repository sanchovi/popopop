package com.threeqms.popopop

import android.view.View
import kotlin.math.roundToInt

class Kernel(pos : Vector2, r : Float, v : View) {

    companion object {
        val GRAVITY = 9.81f
        val ELASTICITY = 0.9f
    }
    
    val view : View = v

    val position : Vector2 = pos
    val rotation : Float = 0f
    val radius : Float = r
    val velocity : Vector2 = Vector2()
    val angularVelocity : Float = 0f
    val popProgress : Float = 0f
    val mass : Float = 1f

    init {
        updateView()
    }

    fun updateView()
    {
        view.layoutParams.width = radius.roundToInt()
        view.layoutParams.height = radius.roundToInt()
        view.x = (position.x - radius / 2f)
        view.y = (position.y - radius / 2f)
        view.rotation = rotation
    }
}