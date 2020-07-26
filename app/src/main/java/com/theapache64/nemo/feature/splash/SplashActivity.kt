package com.theapache64.nemo.feature.splash

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.theapache64.nemo.R
import com.theapache64.nemo.databinding.ActivitySplashBinding
import com.theapache64.nemo.feature.base.BaseActivity
import com.theapache64.nemo.feature.products.ProductsActivity

class SplashActivity :
    BaseActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash) {

    override fun onCreate() {

        // Go to products
        viewModel.shouldGoToProducts.observe(this, Observer { shouldGoToProducts ->
            if (shouldGoToProducts) {
                startActivity(ProductsActivity.getStartIntent(this))
                finish()
            }
        })
    }

    override val viewModel: SplashViewModel by viewModels()
}