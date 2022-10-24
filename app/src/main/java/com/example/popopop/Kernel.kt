package com.example.popopop

import androidx.fragment.app.Fragment
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Vector2(private val xPoint : Float, private val yPoint : Float){
    var x : Float = xPoint
    var y : Float = yPoint
    companion object {
        val Pi = 3.141592653589793238462643383279502884f;
        val Deg2Rad = Pi / 180.0f;
        val Rad2Deg = 180.0f / Pi;
    }

    constructor() : this(0.0f, 0.0f) {

    }
    fun magnitude() : Float{
        return sqrt(x * x + y * y)
    }
    fun magnitudeSqr() : Float{
        return x * x + y * y
    }
    fun dot(v : Vector2) : Float{
        return x * v.x + y * v.y
    }
    fun normalized() : Vector2{
        return this / magnitude()
    }
    fun normalizedOrZero() : Vector2{
        val magnitude = magnitude()
        if(magnitude == 0f){
            return Vector2()
        }
        return this / magnitude
    }
    fun angleFrom(axis : Vector2) : Float {

        return atan2(this.y * axis.x - this.x * axis.y, this.x * axis.x + this.y * axis.y) * Rad2Deg
    }
    fun rotated(degrees : Float) : Vector2{
        val rads = degrees * Deg2Rad
        return Vector2(this.x * cos(rads) - this.y * sin(rads), this.x * sin(rads) + this.y * cos(rads))
    }
    fun projected(axis : Vector2) : Vector2 {
        return (dot(axis) / axis.magnitudeSqr()) * axis
    }
    fun perpProjected(axis : Vector2) : Vector2{
        return this - projected(axis)
    }
    fun reflect(axis : Vector2) : Vector2{
        val reflectAngle = angleFrom(axis)
        return rotated(-2 * reflectAngle)
    }

    operator fun Vector2.unaryPlus() = Vector2(x, y)
    operator fun Vector2.unaryMinus() = Vector2(-x, -y)
    operator fun Vector2.plus(v : Vector2) = Vector2(x + v.x, y + v.y)
    operator fun Vector2.minus(v : Vector2) = Vector2(x - v.x, y - v.y)
    operator fun Vector2.times(v : Vector2) = Vector2(x * v.x, y * v.y)
    operator fun Vector2.times(f : Float) = Vector2(x * f, y * f)
    operator fun Float.times(v : Vector2) = Vector2(this * v.x, this * v.y)
    operator fun Vector2.div(v : Vector2) = Vector2(x / v.x, y / v.y)
    operator fun Vector2.div(f : Float) = Vector2(x / f, y / f)
    override fun equals(v : Any?) = (v is Vector2) && (v != null) && (v.x == x) && (v.y == y)
}
class KernelFragment(pos : Vector2, r : Float) : Fragment() {
    val position : Vector2 = pos
    val radius : Float = r
    val velocity : Vector2 = Vector2()
    val popProgress : Float = 0f
    val mass : Float = 1f

    companion object {
        val gravity = 9.81f
        val elasticity = 0.9f
    }
}