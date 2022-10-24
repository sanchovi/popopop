package com.example.popopop

import java.util.*

class KernelSimulator {
    val kernels: MutableList<KernelFragment?> = mutableListOf()
    val freeIds: Queue<Int> = LinkedList<Int>(listOf())

    fun addKernel(): KernelFragment {
        val kernel = KernelFragment(Vector2(), 10f)
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
            var kernel: KernelFragment = kernelOpt!!
            kernel.position += kernel.velocity * dt
        }

        // TODO update the positions and rotations of the views
    }
}
