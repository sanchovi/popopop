package com.threeqms.popopop

import android.app.Activity
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.hardware.*
import android.view.View
import java.util.*

class KernelSimulator(sm : SensorManager, boundsMin: Vector2, boundsMax: Vector2, act: Activity) : SensorEventListener {
    val sensorManager : SensorManager = sm
    val kernels: MutableList<Kernel?> = mutableListOf()
    val freeIds: Queue<Int> = LinkedList<Int>(listOf())
    val activity: Activity = act
    private val minBounds : Vector2 = boundsMin
    private  val maxBounds : Vector2 = boundsMax
    private val boundsSize : Vector2 =  maxBounds - minBounds
    private var acceleration : Array<Float> = arrayOf(0f, 0f, 0f)
    private var angularAcceleration : Array<Float> = arrayOf(0f, 0f, 0f)


    fun addKernel(kernelView: View, kernelType: KernelTypeDef): Kernel {
        val randomX = minBounds.x + Math.random().toFloat() * boundsSize.x
        val randomY = minBounds.y + Math.random().toFloat() * boundsSize.y / 2

        val randomRadius = kernelType.size + Math.random().toFloat() * kernelType.sizeDeviation * 2 - kernelType.sizeDeviation

        //TODO: add global constants for minStart speed and maxStartSpeed
        val randomStartSpeed = 100f + Math.random().toFloat() * 200f
        val randomDirection = Vector2(1f, 0f).rotated(Math.random().toFloat() * 360)
        val randomStartVelocity = randomDirection * randomStartSpeed

        val randomAngular = -45f + Math.random().toFloat() * 90f

        val res : Resources = Resources.getSystem()
        val theme : Theme = res.newTheme()
        val kernel =
            Kernel(Vector2(randomX, randomY),
            randomRadius,
            kernelView,
            randomStartVelocity,
            randomAngular,
            res.getDrawable(kernelType.drawableUnpopped, theme),
            res.getDrawable(kernelType.drawablePopped, theme))


        if (!freeIds.isEmpty()) {
            val id: Int = freeIds.poll()
            kernels[id] = kernel
        }
        else {
            kernels.add(kernel)
        }

        return kernel
    }

    fun removeKernel(id: Int) {
        kernels[id] = null
        freeIds.add(id)
    }

    fun simulate(dt: Float) {

        for (kernelOpt in kernels) {
            if (kernelOpt == null)
                continue
            var kernel: Kernel = kernelOpt!!

            kernel.update(this, dt)
        }

        for (kernelOpt in kernels)
            kernelOpt?.updateView()
    }

    fun getMaxBounds(): Vector2 {
        return maxBounds
    }

    fun getMinBounds(): Vector2 {
        return minBounds
    }

    fun getAcceleration(): Array<Float> {
        return acceleration
    }

    fun getAngularAcceleration(): Array<Float> {
        return angularAcceleration
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //https://developer.android.com/guide/topics/sensors/sensors_motion
        if (event?.sensor!!.type == Sensor.TYPE_ACCELEROMETER) {
            acceleration[0] = event.values[0]
            acceleration[1] = event.values[1]
            acceleration[2] = event.values[2]
            //println("Updated acceleration values")
        }
        else if (event!!.sensor.type == Sensor.TYPE_GYROSCOPE) {
            angularAcceleration[0] = event.values[0]
            angularAcceleration[1] = event.values[1]
            angularAcceleration[2] = event.values[2]
            //println("Updated gyroscope values")
        }
//        else if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
//            // convert the rotation-vector to a 4x4 matrix. the matrix
//            // is interpreted by Open GL as the inverse of the
//            // rotation-vector, which is what we want.
//            SensorManager.getRotationMatrixFromVector(
//                mRotationMatrix , event.values);
//        }
    }

    override fun onAccuracyChanged(event: Sensor?, accuracy: Int) {
        return
    }

    fun start() {
        // enable our sensor when the activity is resumed, ask for
        // 10 ms updates.
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 10000)
    }

    fun stop() {
        // make sure to turn our sensor off when the activity is paused
        sensorManager.unregisterListener(this)
    }
}
