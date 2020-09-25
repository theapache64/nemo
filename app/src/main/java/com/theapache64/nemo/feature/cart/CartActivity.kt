package com.theapache64.nemo.feature.cart

import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.theapache64.nemo.R
import com.theapache64.nemo.databinding.ActivityCartBinding
import com.theapache64.nemo.feature.base.BaseActivity
import com.theapache64.nemo.utils.calladapter.flow.Resource
import com.theapache64.nemo.utils.extensions.gone
import com.theapache64.nemo.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityCartBinding, CartViewModel>(R.layout.activity_cart) {

    override val viewModel: CartViewModel by viewModels()

    override fun onCreate() {
        binding.viewModel = viewModel
        setSupportActionBar(binding.mtCart)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.csrlCart.setOnRefreshListener {
            viewModel.loadCart()
        }

        viewModel.cartItemResponse.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.lvCart.showLoading(R.string.cart_loading_cart)
                    binding.rvCart.gone()
                }
                is Resource.Success -> {
                    binding.lvCart.hideLoading()
                    binding.rvCart.adapter = CartAdapter(
                        viewModel.config,
                        it.data,
                        viewModel
                    )
                    binding.rvCart.visible()
                }
                is Resource.Error -> {
                    binding.lvCart.showError(it.errorData)
                }
            }
        })

        viewModel.shouldNotifyItemChanged.observe(this, Observer { position ->
            binding.rvCart.adapter?.notifyItemChanged(position)
        })

        viewModel.shouldNotifyItemRemoved.observe(this, Observer { position ->
            binding.rvCart.adapter?.notifyItemRemoved(position)
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