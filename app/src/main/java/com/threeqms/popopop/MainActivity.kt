package com.threeqms.popopop

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.threeqms.popopop.databinding.ActivityMainBinding
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var inflater: LayoutInflater
    private lateinit var container: FrameLayout
    private lateinit var simulator: KernelSimulator
    private var isRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)

        container = findViewById<FrameLayout>(R.id.frameLayout)

        simulator = KernelSimulator()
        simulator.addKernel(createKernelView())
        simulator.addKernel(createKernelView())
        simulator.addKernel(createKernelView())

        var previousMillis: Long = System.currentTimeMillis()
        GlobalScope.launch {
            isRunning = true

            while (isRunning) {
                var currentMillis: Long = System.currentTimeMillis()
                var dtMillis: Long = currentMillis - previousMillis
                if (dtMillis > 67)
                    dtMillis = 67
                var previousMillis = currentMillis

                simulator.simulate(dtMillis / 1000.0f)

                currentMillis = System.currentTimeMillis()
                var desiredWaitTime: Long = 16 - (currentMillis - previousMillis)
                if (desiredWaitTime > 0)
                    delay(desiredWaitTime)
            }
        }
    }

    fun createKernelView() : View
    {
        val view: View = inflater.inflate(R.layout.fragment_kernel, null)
        container.addView(view)
        return view
    }

}