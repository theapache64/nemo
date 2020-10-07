package com.theapache64.nemo.feature.cart

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.theapache64.nemo.R
import com.theapache64.nemo.databinding.ActivityCartBinding
import com.theapache64.nemo.feature.base.BaseActivity
import com.theapache64.nemo.feature.ordersummary.OrderSummaryActivity
import com.theapache64.nemo.utils.calladapter.flow.Resource
import com.theapache64.nemo.utils.extensions.gone
import com.theapache64.nemo.utils.extensions.invisible
import com.theapache64.nemo.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityCartBinding, CartViewModel>(R.layout.activity_cart) {
    companion object {
        const val RESULT_CART_UPDATED = 134
        fun getStartIntent(context: Context): Intent {
            return Intent(context, CartActivity::class.java).apply {
                // data goes here
            }
        }
    }

    private var cartAdapter: CartAdapter? = null
    override val viewModel: CartViewModel by viewModels()

    override fun onCreate() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.mtCart)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.csrlCart.setOnRefreshListener {
            viewModel.loadCart()
        }

        viewModel.cartItemResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.lvCart.showLoading(R.string.cart_loading_cart)
                    binding.gContent.gone()
                }
                is Resource.Success -> {
                    binding.lvCart.hideLoading()
                    cartAdapter = CartAdapter(
                        viewModel.config,
                        it.data.toMutableList(),
                        viewModel
                    )
                    binding.rvCart.adapter = cartAdapter
                    binding.gContent.visible()
                }
                is Resource.Error -> {
                    binding.lvCart.showError(it.errorData)
                }
            }
        })

        viewModel.shouldNotifyItemChanged.observe(this, Observer { position ->
            cartAdapter?.notifyItemChanged(position)
        })

        viewModel.shouldNotifyItemRemoved.observe(this, Observer { position ->
            cartAdapter?.removeData(position)
            cartAdapter?.notifyItemRemoved(position)

            // Notifying launched activity
            setResult(RESULT_CART_UPDATED)
        })

        viewModel.shouldShowCartEmpty.observe(this, Observer {
            if (it) {
                binding.gContent.invisible()
                binding.lvCart.showError(getString(R.string.cart_error_empty_cart))
            }
        })

        viewModel.shouldLaunchOrderSummary.observe(this, Observer {
            if (it) {
                startActivity(OrderSummaryActivity.getStartIntent(this))
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
    }
}