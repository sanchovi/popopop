package com.threeqms.popopop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.threeqms.popopop.adapter.ShopCardAdapter
import com.threeqms.popopop.databinding.ActivityShopBinding

class Shop : AppCompatActivity() {

    private lateinit var binding: ActivityShopBinding
    private lateinit var kernelStorage: KernelStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)

        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(binding.root)
        kernelStorage = KernelStorage(this@Shop)
        kernelStorage.initialize()

        binding.shopRecycler.adapter = ShopCardAdapter(applicationContext, kernelStorage)
        // Specify fixed size to improve performance
        binding.shopRecycler.setHasFixedSize(true)

        binding.resetButton.setOnClickListener{
            kernelStorage.resetKernels()
            (binding.shopRecycler.adapter as ShopCardAdapter).updateAllCards()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}


