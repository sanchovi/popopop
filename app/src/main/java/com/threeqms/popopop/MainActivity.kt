package com.threeqms.popopop

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.threeqms.popopop.databinding.ActivityMainBinding
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var inflater: LayoutInflater
    private lateinit var container: FrameLayout
    private lateinit var simulator: KernelSimulator
    private var isRunning: Boolean = false
    private var isButtonPressed: Boolean = false
    private var kernelTempView : View? = null

    private lateinit var sensorManager:SensorManager
    private lateinit var accelerometer :Sensor
    private lateinit var gyroscope :Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)

        container = findViewById<FrameLayout>(R.id.frameLayout)
        binding.spawnButton.setOnClickListener{
            if(!isButtonPressed) {
                isButtonPressed = true

                kernelTempView = createKernelView()

                kernelTempView!!.isVisible = false;
            }
        }

        val b1 = findViewById<View>(R.id.shop_button)

        b1.setOnClickListener {
            val shop = Intent(this@MainActivity, Shop::class.java)
            startActivity(shop)
        }
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

            simulator = KernelSimulator(sensorManager, minPoint, maxPoint, this)

            for(i in 1..3){
                simulator.addKernel(createKernelView(), KernelData.KernelTypes[0])
            }

            var previousMillis: Long = System.currentTimeMillis()
            MainScope().launch {
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
                    if(kernelTempView != null && isButtonPressed) {
                        // TODO: add logic to choose from available non spawned kernel types
                        simulator.addKernel(kernelTempView!!, KernelData.KernelTypes[0])
                        isButtonPressed = false
                        kernelTempView = null
                    }
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