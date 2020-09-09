package com.theapache64.nemo.feature.home

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.theapache64.nemo.R
import com.theapache64.nemo.databinding.ActivityHomeBinding
import com.theapache64.nemo.feature.base.BaseActivity
import com.theapache64.nemo.feature.home.banner.BannerAdapter
import com.theapache64.nemo.utils.calladapter.flow.Resource
import com.theapache64.nemo.utils.extensions.invisible
import com.theapache64.nemo.utils.extensions.visible
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.indicator.enums.IndicatorSlideMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java).apply {
                // data goes here
            }
        }
    }

    override val viewModel: HomeViewModel by viewModels()

    override fun onCreate() {
        binding.viewModel = viewModel

        binding.csrlHome.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.bvpHome
            .setAdapter(BannerAdapter())
            .setLifecycleRegistry(lifecycle)
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.home_banner_item_margin))
            .setRevealWidth(resources.getDimensionPixelOffset(R.dimen.home_banner_reveal_width))
            .setIndicatorStyle(IndicatorSlideMode.SCALE)
            .setPageStyle(PageStyle.MULTI_PAGE_SCALE)
            .create()

        viewModel.banners.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.lvHome.showLoading(R.string.home_loading_banners)
                    binding.bvpHome.invisible()
                }
                is Resource.Success -> {
                    binding.lvHome.hideLoading()
                    binding.bvpHome.visible()
                    binding.bvpHome.refreshData(it.data)
                }
                is Resource.Error -> {
                    binding.lvHome.showError(it.errorData)
                }
            }
        })
    }
}