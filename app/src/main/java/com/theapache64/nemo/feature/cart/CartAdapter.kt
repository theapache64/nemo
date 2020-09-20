package com.theapache64.nemo.feature.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theapache64.nemo.data.remote.Config
import com.theapache64.nemo.databinding.ItemCartBinding

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 18:26
 */
class CartAdapter(
    private val config: Config,
    private val cartItems: List<CartItem>
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.cartItem = cartItems[position]
        holder.binding.config = config
    }

    override fun getItemCount(): Int = cartItems.size
}