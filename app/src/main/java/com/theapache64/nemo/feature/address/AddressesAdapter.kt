package com.theapache64.nemo.feature.address

/**
 * Created by theapache64 : Oct 10 Sat,2020 @ 19:49
 */
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theapache64.nemo.data.local.table.addresses.AddressEntity
import com.theapache64.nemo.databinding.ItemAddressBinding

class AddressesAdapter(
    context: Context,
    private val addresses: List<AddressEntity>,
    private val callback: Callback
) : RecyclerView.Adapter<AddressesAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddressBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = addresses.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val address = addresses[position]
        holder.binding.run {
            this.address = address
            this.position = position
            this.callback = callback
        }
    }

    inner class ViewHolder(val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface Callback {
        fun onAddressClicked(position: Int)
        fun onEditAddressClicked(position: Int)
    }
}