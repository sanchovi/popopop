package com.threeqms.popopop

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
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

    private lateinit var sensorManager:SensorManager
    private lateinit var accelerometer :Sensor
    private lateinit var gyroscope :Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)

        container = findViewById<FrameLayout>(R.id.frameLayout)


    }

    override fun onStart() {
        super.onStart()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        //Initiate KernelSimulator
        container.post(Runnable{
            val minPoint = Vector2(container.x, container.y)
            val maxPoint = Vector2(container.x + container.width, container.y + container.height)


            simulator = KernelSimulator(sensorManager, minPoint, maxPoint)

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

                    simulator?.simulate(dtMillis / 1000.0f)

                    currentMillis = System.currentTimeMillis()
                    var desiredWaitTime: Long = 16 - (currentMillis - previousMillis)
                    if (desiredWaitTime > 0)
                        delay(desiredWaitTime)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        container.post(Runnable{
            simulator.start()
        })
    }

    override fun onPause() {
        super.onPause()
        container.post(Runnable{
            simulator.stop()
        })
    }

    fun createKernelView() : View
    {
        val view: View = inflater.inflate(R.layout.fragment_kernel, null)
        container.addView(view)
        return view
    }

}