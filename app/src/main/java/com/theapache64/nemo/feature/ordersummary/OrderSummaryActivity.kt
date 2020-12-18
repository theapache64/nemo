package com.theapache64.nemo.feature.ordersummary

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.theapache64.nemo.R
import com.theapache64.nemo.databinding.ActivityOrderSummaryBinding
import com.theapache64.nemo.feature.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderSummaryActivity :
    BaseActivity<ActivityOrderSummaryBinding, OrderSummaryViewModel>(R.layout.activity_order_summary) {

    companion object {
        private const val KEY_PRODUCT_ID = "product_id"
        fun getStartIntent(context: Context, productId: Int): Intent {
            return Intent(context, OrderSummaryActivity::class.java).apply {
                // data goes here
                putExtra(KEY_PRODUCT_ID, productId)
            }
        }
    }

    override val viewModel: OrderSummaryViewModel by viewModels()

    override fun onCreate() {
        binding.viewModel = viewModel
    }

}