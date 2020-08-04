package com.theapache64.nemo.feature.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theapache64.nemo.data.remote.Config
import com.theapache64.nemo.data.remote.Product
import com.theapache64.nemo.databinding.ItemProductBinding

/**
 * Created by theapache64 : Jul 17 Fri,2020 @ 20:30
 * Copyright (c) 2020
 * All rights reserved
 */
class ProductsAdapter(
    private val config: Config,
    private val callback: Callback
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private val products = mutableListOf<Product>()

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
        holder.binding.config = config
    }

    fun append(newProducts: List<Product>) {
        val positionStart = products.size
        products.addAll(newProducts)
        notifyItemRangeInserted(positionStart, newProducts.size)
    }

    fun clear() {
        products.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                callback.onProductClicked(layoutPosition, products[layoutPosition])
            }
        }
    }

    interface Callback {
        fun onAddToCartClicked(position: Int)
        fun onProductClicked(position: Int, product: Product)
    }

}