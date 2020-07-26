package com.theapache64.nemo.feature.products

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.theapache64.nemo.R
import com.theapache64.nemo.databinding.ActivityProductsBinding
import com.theapache64.nemo.utils.extensions.gone
import com.theapache64.nemo.utils.extensions.visible
import com.theapache64.nemo.utils.flow.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsActivity : AppCompatActivity() {

    private val viewModel: ProductsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityProductsBinding>(
            this,
            R.layout.activity_products
        )
        binding.viewModel = viewModel

        viewModel.products.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.lvProducts.showLoading(R.string.products_loading)
                    binding.rvProducts.gone()
                }
                is Resource.Success -> {
                    binding.lvProducts.hideLoading()
                    binding.rvProducts.visible()

                    binding.rvProducts.adapter = ProductsAdapter(
                        products = it.data,
                        callback = viewModel
                    )
                }
                is Resource.Error -> {
                    binding.rvProducts.gone()
                    binding.lvProducts.showError(it.message)
                }
            }
        })
    }
}