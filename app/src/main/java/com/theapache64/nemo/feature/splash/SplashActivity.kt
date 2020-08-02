package com.theapache64.nemo.feature.splash

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.theapache64.nemo.R
import com.theapache64.nemo.databinding.ActivitySplashBinding
import com.theapache64.nemo.feature.base.BaseActivity
import com.theapache64.nemo.feature.products.ProductsActivity
import com.theapache64.nemo.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity :
    BaseActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash) {

    override fun onCreate() {

        binding.pbConfigSync.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                indeterminateTintList = ColorStateList.valueOf(Color.WHITE)
            } else {
                indeterminateDrawable.setColorFilter(
                    Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN
                );
            }
        }

        // Go to products
        viewModel.shouldGoToProducts.observe(this, Observer { shouldGoToProducts ->
            if (shouldGoToProducts) {
                startActivity(ProductsActivity.getStartIntent(this))
                finish()
            }
        })

        viewModel.shouldShowConfigSyncError.observe(this, Observer { shouldShow ->
            if (shouldShow) {
                showConfigSyncError()
            }
        })

        viewModel.shouldShowProgress.observe(this, Observer { shouldShow ->
            if (shouldShow) {
                binding.pbConfigSync.visible()
            } else {
                binding.pbConfigSync.visibility = View.INVISIBLE
            }
        })
    }

    private fun showConfigSyncError() {
        AlertDialog.Builder(this)
            .setTitle(R.string.splash_sync_error_title)
            .setMessage(R.string.splash_sync_error_message)
            .setPositiveButton(R.string.action_retry) { _: DialogInterface, _: Int ->
                viewModel.onRetryClicked()
            }
            .create()
            .show()
    }

    override val viewModel: SplashViewModel by viewModels()
}