package com.theapache64.nemo.feature.productdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theapache64.nemo.data.remote.Product
import com.theapache64.nemo.databinding.ItemMoreDetailsBinding

/**
 * Created by theapache64 : Sep 17 Thu,2020 @ 08:39
 */
class MoreDetailsAdapter(
    private val detailsList: List<Product.Detail>
) : RecyclerView.Adapter<MoreDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMoreDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.detail = detailsList[position]
    }

    override fun getItemCount(): Int = detailsList.size

    inner class ViewHolder(val binding: ItemMoreDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)
}