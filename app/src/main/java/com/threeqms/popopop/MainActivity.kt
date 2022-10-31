package com.threeqms.popopop

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var sensorManager:SensorManager
    private lateinit var accelerometer :Sensor
    private lateinit var gyroscope :Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val b1 = findViewById<View>(R.id.start_button)

    }

    override fun onStart() {
        super.onStart()

        container.post(Runnable{
            val minPoint = Vector2(container.x, container.y)
            val maxPoint = Vector2(container.x + container.width, container.y + container.height)
            simulator = KernelSimulator(minPoint, maxPoint)
            for(i in 1..25){
                simulator.addKernel(createKernelView())
            }

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
        })
    }

    fun createKernelView() : View
    {
        val view: View = inflater.inflate(R.layout.fragment_kernel, null)
        container.addView(view)
        return view
    }

}