package com.threeqms.popopop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Start : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val b1 = findViewById<View>(R.id.shop_button)

        b1.setOnClickListener {
            val gameplay = Intent(this@Start, MainActivity::class.java)
            startActivity(gameplay)
        }


    }

    /**
     * Enables back button support. Simply navigates one element up on the stack.
     */
    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}