package com.threeqms.popopop

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.threeqms.popopop.databinding.ActivityMainBinding
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var inflater: LayoutInflater
    private lateinit var container: FrameLayout
    private lateinit var simulator: KernelSimulator
    private lateinit var kernelStorage: KernelStorage
    private var isRunning: Boolean = false
    private var isButtonPressed: Boolean = false
    private var kernelTempView : View? = null
//    private val workManager = WorkManager.getInstance(application)

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
                if(kernelTempView == null) {
                    kernelTempView = createKernelView()
                }
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

        kernelStorage = KernelStorage(this@MainActivity)
        kernelStorage.initialize()

        //Initiate KernelSimulator
        container.post(Runnable{
            val minPoint = Vector2(container.x, container.y)
            val maxPoint = Vector2(container.x + container.width, container.y + container.height)

            simulator = KernelSimulator(sensorManager, minPoint, maxPoint, this)

            for(i in 0 until kernelStorage.kernels.size){
                for(j in 0 until kernelStorage.kernels[i].numAdded){
                    simulator.addKernel(createKernelView(), KernelData.KernelTypes[i])
                }
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
                    //Delayed button press
                    if(kernelTempView != null && isButtonPressed) {
                        var kernelId : Int = -1
                        for(i in 0 until kernelStorage.kernels.size){
                            if(kernelStorage.kernels[i].numOwned > kernelStorage.kernels[i].numAdded){
                                kernelId = i
                                break
                            }
                        }
                        if(kernelId == -1){
                            val toast = Toast.makeText(applicationContext, "You're out of kernels. Pop some or buy more from the shop!", Toast.LENGTH_LONG)
                            toast.show()
                        }
                        else {
                            kernelStorage.kernels[kernelId].numAdded++
                            kernelStorage.saveKernelsToPrefs()
                            simulator.addKernel(kernelTempView!!, KernelData.KernelTypes[kernelId])
                            kernelTempView = null
                        }
                        isButtonPressed = false
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
//        scheduleReminder(5, TimeUnit.SECONDS)
    }

//    override fun onStop() {
//        super.onStop()
////        scheduleReminder(5, TimeUnit.SECONDS)
//    }

    fun createKernelView() : View
    {
        val view: View = inflater.inflate(R.layout.fragment_kernel, null)
        container.addView(view)
        return view
    }


//    internal fun scheduleReminder(
//        duration: Long,
//        unit: TimeUnit,
//    ) {
//        //Generate a OneTimeWorkRequest with the passed in duration and time unit
//        val pushNotifRequest = OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(duration, unit).build()
//
//        //enqueue the work
//        workManager.enqueue(pushNotifRequest)
//
//    }
}
