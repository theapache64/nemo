package com.theapache64.nemo.feature.products

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.theapache64.nemo.R
import com.theapache64.nemo.databinding.ActivityProductsBinding
import com.theapache64.nemo.feature.base.BaseActivity
import com.theapache64.nemo.feature.productdetail.ProductDetailActivity
import com.theapache64.nemo.utils.extensions.gone
import com.theapache64.nemo.utils.extensions.visible
import com.theapache64.nemo.utils.flow.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsActivity :
    BaseActivity<ActivityProductsBinding, ProductsViewModel>(R.layout.activity_products) {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, ProductsActivity::class.java).apply {
                // data goes here
            }
        }
    }


    override val viewModel: ProductsViewModel by viewModels()

    override fun onCreate() {
        binding.viewModel = viewModel
        watchProducts()
        watchProductDetail()
    }

    private fun watchProductDetail() {
        viewModel.shouldLaunchProductDetail.observe(this, Observer { productId ->
            startActivity(ProductDetailActivity.getStartIntent(this, productId))
        })
    }

    private fun watchProducts() {
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