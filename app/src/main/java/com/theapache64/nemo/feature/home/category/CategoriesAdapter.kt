package com.theapache64.nemo.feature.home.category

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 13:14
 */
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theapache64.nemo.data.remote.Category
import com.theapache64.nemo.databinding.ItemCategoryBinding

class CategoriesAdapter(
    context: Context,
    private val categories: List<Category>,
    private val callback: (position: Int) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.binding.category = category
    }

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                callback(layoutPosition)
            }
        }
    }
}
