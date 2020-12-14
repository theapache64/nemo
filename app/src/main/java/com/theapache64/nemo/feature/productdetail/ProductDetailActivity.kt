package com.theapache64.nemo.feature.productdetail

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.theapache64.nemo.R
import com.theapache64.nemo.databinding.ActivityProductDetailBinding
import com.theapache64.nemo.feature.base.BaseActivity
import com.theapache64.nemo.feature.cart.CartActivity
import com.theapache64.nemo.utils.calladapter.flow.Resource
import com.theapache64.nemo.utils.extensions.getIntExtraOrThrow
import com.theapache64.nemo.utils.extensions.gone
import com.theapache64.nemo.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProductDetailActivity : BaseActivity<ActivityProductDetailBinding, ProductDetailViewModel>(
    R.layout.activity_product_detail
) {
    companion object {
        const val KEY_PRODUCT_ID = "product_id"
        fun getStartIntent(context: Context, productId: Int): Intent {
            return Intent(context, ProductDetailActivity::class.java).apply {
                // data goes here
                putExtra(KEY_PRODUCT_ID, productId)
            }
        }
    }

    private val cartActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CartActivity.RESULT_CART_UPDATED) {
            // Something happened in cart. So let's just refresh the page
            viewModel.reload()
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

        binding.csrlProduct.setOnRefreshListener {
            viewModel.reload()
        }


        viewModel.productResp.observe(this, {
            when (it) {
                is Resource.Loading -> {
                    Timber.d("onCreate: Loading")
                    binding.gContent.gone()
                    binding.gButtons.gone()
                    binding.lvProduct.showLoading(R.string.product_loading)
                }
                is Resource.Success -> {
                    Timber.d("onCreate: Success")
                    binding.gContent.visible()
                    binding.gButtons.visible()
                    binding.lvProduct.hideLoading()

                    if (it.data.moreDetails.isNotEmpty()) {
                        binding.rvDetails.visible()
                        binding.tvLabelProductDetails.visible()
                        binding.rvDetails.adapter = MoreDetailsAdapter(it.data.moreDetails)
                    } else {
                        // binding.rvDetails.gone()
                        binding.tvLabelProductDetails.gone()
                    }

                }
                is Resource.Error -> {
                    Timber.d("onCreate: Failed")
                    binding.lvProduct.showError(it.errorData)
                }
            }
        })


        // Launch Cart
        viewModel.shouldGoToCart.observe(this, Observer {
            if (it) {
                cartActivityLauncher.launch(CartActivity.getStartIntent(this))
            }
        })

        viewModel.shouldBuyNow.observe(this, Observer { productId ->
            // TODO:
        })
    }



}