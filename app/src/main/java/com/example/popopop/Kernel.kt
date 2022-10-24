package com.example.popopop

import androidx.fragment.app.Fragment

class KernelFragment(pos : Vector2, r : Float) : Fragment() {
    val position : Vector2 = pos
    val rotation : Float = 0f
    val radius : Float = r
    val velocity : Vector2 = Vector2()
    val angularVelocity : Float = 0f
    val popProgress : Float = 0f
    val mass : Float = 1f

    companion object {
        val gravity = 9.81f
        val elasticity = 0.9f
    }
}