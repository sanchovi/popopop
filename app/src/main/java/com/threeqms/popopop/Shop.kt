package com.threeqms.popopop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.threeqms.popopop.adapter.ShopCardAdapter
import com.threeqms.popopop.databinding.ActivityMainBinding
import com.threeqms.popopop.databinding.ActivityShopBinding
import com.threeqms.popopop.databinding.ShopSegmentBinding

class Shop : AppCompatActivity() {

    private lateinit var binding: ActivityShopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.shopRecycler.adapter = ShopCardAdapter(applicationContext)
        // Specify fixed size to improve performance
        binding.shopRecycler.setHasFixedSize(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }
}


