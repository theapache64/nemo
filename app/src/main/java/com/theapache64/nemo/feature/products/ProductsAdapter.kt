package com.theapache64.nemo.feature.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theapache64.nemo.data.remote.ProductsResponse
import com.theapache64.nemo.databinding.ItemProductBinding

/**
 * Created by theapache64 : Jul 17 Fri,2020 @ 20:30
 * Copyright (c) 2020
 * All rights reserved
 */
class ProductsAdapter(private val products: List<ProductsResponse.Product>) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.product = products[position]
    }

    class ViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

}