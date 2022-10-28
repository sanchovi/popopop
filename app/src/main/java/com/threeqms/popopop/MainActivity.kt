package com.threeqms.popopop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val b1 = findViewById<View>(R.id.start_button)

        b1.setOnClickListener {
            val game = Intent(this@MainActivity, Gameplay::class.java)
            startActivity(game)
        }


    }

    /**
     * Enables back button support. Simply navigates one element up on the stack.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}