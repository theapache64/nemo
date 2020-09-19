package com.theapache64.nemo.feature.cart

import android.view.MenuItem
import androidx.activity.viewModels
import com.theapache64.nemo.R
import com.theapache64.nemo.databinding.ActivityCartBinding
import com.theapache64.nemo.feature.base.BaseActivity

class CartActivity : BaseActivity<ActivityCartBinding, CartViewModel>(R.layout.activity_cart) {

    override val viewModel: CartViewModel by viewModels()

    override fun onCreate() {
        binding.viewModel = viewModel
        setSupportActionBar(binding.mtCart)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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