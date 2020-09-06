package com.theapache64.nemo.feature.productdetail

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.theapache64.nemo.R
import com.theapache64.nemo.databinding.ActivityProductDetailBinding
import com.theapache64.nemo.feature.base.BaseActivity
import com.theapache64.nemo.utils.extensions.getIntExtraOrThrow
import com.theapache64.twinkill.network.utils.retrofit.adapters.flow.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailActivity : BaseActivity<ActivityProductDetailBinding, ProductDetailViewModel>(
    R.layout.activity_product_detail
) {
    companion object {
        private const val KEY_PRODUCT_ID = "product_id"
        fun getStartIntent(context: Context, productId: Int): Intent {
            return Intent(context, ProductDetailActivity::class.java).apply {
                // data goes here
                putExtra(KEY_PRODUCT_ID, productId)
            }
        }
    }

    override val viewModel: ProductDetailViewModel by viewModels()
    override fun onCreate() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val productId = if (isDebugActivity()) {
            1
        } else {
            // real
            intent.getIntExtraOrThrow(KEY_PRODUCT_ID)
        }
        viewModel.init(productId)

        watchProduct()
    }

    private fun watchProduct() {
        viewModel.productResp.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                }
                is Resource.Error -> {
                }
            }
        })
    }

}