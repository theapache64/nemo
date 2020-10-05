package com.theapache64.nemo.feature.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theapache64.nemo.data.remote.Config
import com.theapache64.nemo.databinding.ItemCartBinding
import me.himanshusoni.quantityview.QuantityView

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 18:26
 */
class CartAdapter(
    private val config: Config,
    private val cartItems: MutableList<CartItem>,
    private val callback: Callback
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {


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
    fun removeData(position: Int) {
        cartItems.removeAt(position)
    }


    inner class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root),
        QuantityView.OnQuantityChangeListener {
        init {
            binding.qvCartQuantity.onQuantityChangeListener = this
            binding.bRemove.setOnClickListener {
                callback.onRemoveClicked(layoutPosition)
            }
        }

        override fun onQuantityChanged(
            oldQuantity: Int,
            newQuantity: Int,
            programmatically: Boolean
        ) {
            callback.onQuantityChanged(layoutPosition, newQuantity)
        }

        override fun onLimitReached() {

        }
    }

    interface Callback {
        fun onQuantityChanged(position: Int, count: Int)
        fun onRemoveClicked(position: Int)
    }
}