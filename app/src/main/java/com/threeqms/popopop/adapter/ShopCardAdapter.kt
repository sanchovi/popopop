/*
* Copyright (C) 2021 The Android Open Source Project.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.threeqms.popopop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.threeqms.popopop.R
import com.threeqms.popopop.KernelData

/**
 * Adapter to inflate the appropriate list item layout and populate the view with information
 * from the appropriate data source
 */
class ShopCardAdapter(
    private val context: Context?,
): RecyclerView.Adapter<ShopCardAdapter.ShopCardViewHolder>() {

    // Initialize the data using the List found in data/DataSource
    val src = KernelData.KernelTypes

    /**
     * Initialize view elements
     */
    class ShopCardViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        // Declare and initialize all of the list item UI components
        var kernelImage: ImageView = view!!.findViewById(R.id.kernelImage)
        var kernelName: TextView = view!!.findViewById(R.id.kernelNameText)
        var kernelPrice: TextView = view!!.findViewById(R.id.priceText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopCardViewHolder {
        // inflate layout
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.shop_segment, parent, false)

        return ShopCardViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return src.size
    }

    override fun onBindViewHolder(holder: ShopCardViewHolder, position: Int) {
        //Get the data at the current position
        val item = src[position]

        // Set the image resource for the current dog
        holder.kernelImage.setImageResource(item.drawablePopped)

        // set text for kernel name
        holder.kernelName.text = item.name

        // set kernel price
        holder.kernelPrice.text = item.price.toString()
    }
}