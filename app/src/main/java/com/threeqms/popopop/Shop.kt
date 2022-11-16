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
    private lateinit var kernelStorage: KernelStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)

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
        return super.onSupportNavigateUp()
    }
}


