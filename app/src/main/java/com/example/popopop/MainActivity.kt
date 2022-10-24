package com.example.popopop

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.popopop.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vi = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = vi.inflate(R.layout.fragment_kernel, null)

        findViewById<FrameLayout>(R.id.frameLayout).addView(view)

//        var v : View = View.inflate(applicationContext, R.layout.fragment_kernel, findViewById(R.id.frameLayout))
        view.layoutParams.width = 300
        view.layoutParams.height = 300
        view.x = 100f
        view.y = 100f
    }

}