package com.threeqms.popopop

import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.FloatRange
import java.util.*
import android.app.Activity


class KernelSimulator(boundsMin: Vector2, boundsMax: Vector2){
    val kernels: MutableList<Kernel?> = mutableListOf()
    val freeIds: Queue<Int> = LinkedList<Int>(listOf())
    private val minBounds : Vector2 = boundsMin
    private  val maxBounds : Vector2 = boundsMax
    private val boundsSize : Vector2 =  maxBounds - minBounds


    fun addKernel(kernelView: View, radiusRange : Vector2 = Vector2(80f, 100f)): Kernel {
        val randomX = minBounds.x + Math.random().toFloat() * boundsSize.x
        val randomY = minBounds.y + Math.random().toFloat() * boundsSize.y / 2

        val randomRadius = radiusRange.x + Math.random().toFloat() * (radiusRange.y - radiusRange.x)

        //TODO: add global constants for minStart speed and maxStartSpeed
        val randomStartSpeed = 100f + Math.random().toFloat() * 200f
        val randomDirection = Vector2(1f, 0f).rotated(Math.random().toFloat() * 360)
        val randomStartVelocity = randomDirection * randomStartSpeed

        val randomAngular = -45f + Math.random().toFloat() * 90f
        val kernel = Kernel(Vector2(randomX, randomY), randomRadius, kernelView, randomStartVelocity, randomAngular)


        if (!freeIds.isEmpty()) {
            val id: Int = freeIds.poll()
            kernels[id] = kernel;
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
        // TODO get gyroscope/accelerometer movement
        // TODO apply a corresponding force to all of the kernels

        // TODO have this motion be done inside the kernel class
        for (kernelOpt in kernels) {
            if (kernelOpt == null)
                continue
            var kernel: Kernel = kernelOpt!!

            kernelOpt.update(this, dt)
        }

        // TODO update the positions and rotations of the views
        for (kernelOpt in kernels) {
            kernelOpt?.updateView()
        }
    }

    fun getMaxBounds(): Vector2 {
        return maxBounds
    }

    fun getMinBounds(): Vector2 {
        return minBounds
    }
}