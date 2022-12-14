package com.threeqms.popopop

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.threeqms.popopop.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import com.threeqms.popopop.PopReminderWorker

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
            }
        }

        val b1 = findViewById<View>(R.id.shop_button)

        b1.setOnClickListener {
            val shop = Intent(this@MainActivity, Shop::class.java)
            startActivity(shop)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "popChannel"
            val descriptionText = "popChannelDesc"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("pop_channel_id", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
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

            simulator = KernelSimulator(sensorManager, minPoint, maxPoint)

            for(i in 1..3){
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
                    if(kernelTempView != null && isButtonPressed) {
                        simulator.addKernel(kernelTempView!!)
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
        scheduleReminder(2, TimeUnit.SECONDS)
    }

    override fun onStop() {
        scheduleReminder(2, TimeUnit.SECONDS)
        super.onStop()
    }

    fun createKernelView() : View
    {
        val view: View = inflater.inflate(R.layout.fragment_kernel, null)
        container.addView(view)
        return view
    }

    private val workManager = WorkManager.getInstance(application)

    @SuppressLint("RestrictedApi")
    internal fun scheduleReminder(
        duration: Long,
        unit: TimeUnit,
    ) {


        val pushNotifRequest = OneTimeWorkRequestBuilder<PopReminderWorker>().setInitialDelay(duration, unit).build()

        workManager.enqueue(pushNotifRequest)

    }
}