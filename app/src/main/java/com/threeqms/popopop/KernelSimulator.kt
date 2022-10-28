package com.threeqms.popopop

import android.view.View
import java.util.*

class KernelSimulator {
    val kernels: MutableList<Kernel?> = mutableListOf()
    val freeIds: Queue<Int> = LinkedList<Int>(listOf())

    fun addKernel(kernelView: View): Kernel {
        val kernel = Kernel(Vector2(), 10f, kernelView)

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

            kernel.velocity += Vector2(0f, Kernel.GRAVITY) * dt
            kernel.position += kernel.velocity * dt
        }

        // TODO update the positions and rotations of the views
        for (kernelOpt in kernels) {
            kernelOpt?.updateView()
        }
    }
}