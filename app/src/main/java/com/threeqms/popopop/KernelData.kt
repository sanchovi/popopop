package com.threeqms.popopop

import android.content.res.Resources

object KernelData {
    val KernelTypes: List<KernelTypeDef> = listOf(
            KernelTypeDef(
                "Basic",
                R.drawable.popcorn,
                R.drawable.kernel,
                5,
                40,
                10f,
                100f,
            ) ,

            KernelTypeDef(
                "Cheese",
                R.drawable.popcorn,
                R.drawable.kernel,
                10,
                50,
                10f,
                100f,
            ),

            KernelTypeDef(
                "Cherry Blossom",
                R.drawable.popcorn,
                R.drawable.kernel,
                30,
                30,
                5f,
                200f,
            ),

            KernelTypeDef(
                "Blue Raspberry",
                R.drawable.popcorn,
                R.drawable.kernel,
                30,
                30,
                5f,
                200f,
            )



    )
}