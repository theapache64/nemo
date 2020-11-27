package com.theapache64.nemo.feature.home

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.theapache64.nemo.R
import com.theapache64.nemo.databinding.ActivityHomeBinding
import com.theapache64.nemo.feature.base.BaseActivity
import com.theapache64.nemo.feature.cart.CartActivity
import com.theapache64.nemo.feature.home.banner.BannerAdapter
import com.theapache64.nemo.feature.home.category.CategoriesAdapter
import com.theapache64.nemo.feature.productdetail.ProductDetailActivity
import com.theapache64.nemo.feature.products.ProductsActivity
import com.theapache64.nemo.utils.calladapter.flow.Resource
import com.theapache64.nemo.utils.extensions.gone
import com.theapache64.nemo.utils.extensions.invisible
import com.theapache64.nemo.utils.extensions.visible
import com.theapache64.nemo.utils.test.EspressoIdlingResource
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.indicator.enums.IndicatorSlideMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home),
    BottomNavigationView.OnNavigationItemReselectedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

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
        lifecycle.addObserver(viewModel)

        binding.csrlHome.setOnRefreshListener {
            viewModel.refreshPage()
        }

        binding.bvpHome
            .setAdapter(BannerAdapter())
            .setLifecycleRegistry(lifecycle)
            .setIndicatorSlideMode(IndicatorSlideMode.WORM)
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.home_banner_item_margin))
            .setRevealWidth(resources.getDimensionPixelOffset(R.dimen.home_banner_reveal_width))
            .setOnPageClickListener {
                viewModel.onBannerClicked(it)
            }
            .setIndicatorStyle(IndicatorSlideMode.SCALE)
            .setPageStyle(PageStyle.MULTI_PAGE_SCALE)
            .create()

        binding.bnvHome.setOnNavigationItemSelectedListener(this)

        watchBanners()
        watchCategories()
        watchCartCount()
    }

    private fun watchBanners() {
        // Loading
        viewModel.banners.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.lvHome.showLoading(R.string.home_loading_banners)
                    binding.bvpHome.invisible()
                    EspressoIdlingResource.increment()
                }
                is Resource.Success -> {
                    binding.lvHome.hideLoading()

                    if (it.data.isEmpty()) {
                        // hide
                        binding.bvpHome.gone()
                    } else {
                        // render
                        binding.bvpHome.visible()
                        binding.bvpHome.refreshData(it.data)
                    }

                    EspressoIdlingResource.decrement()
                }
                is Resource.Error -> {
                    binding.lvHome.showError(it.errorData)
                    EspressoIdlingResource.decrement()
                }
            }
        })

        // Click listener : Category
        viewModel.shouldLaunchCategory.observe(this, {
            ProductsActivity.getStartIntent(this, it)
        })

        // Click listener : Product
        viewModel.shouldLaunchProduct.observe(this, { productId ->
            ProductDetailActivity.getStartIntent(this, productId)
        })
    }

    private fun watchCategories() {
        viewModel.categories.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    binding.rvCategories.gone()
                    binding.tvLabelCategories.gone()
                }

                is Resource.Success -> {
                    val adapter = CategoriesAdapter(this, it.data) { position ->
                        val category = it.data[position]
                        startActivity(ProductsActivity.getStartIntent(this, category))
                    }
                    binding.rvCategories.adapter = adapter
                    binding.rvCategories.visible()
                    binding.tvLabelCategories.visible()
                }

                is Resource.Error -> {
                    binding.lvHome.showError(it.errorData)
                }
            }
        })
    }

    private fun watchCartCount() {
        // Cart count
        viewModel.addCartCountBadge.observe(this, Observer { count ->

            // Add badge
            val miCart = binding.bnvHome.getOrCreateBadge(R.id.mi_home_cart).apply {
                backgroundColor = ContextCompat.getColor(this@HomeActivity, R.color.orange_900)
            }

            miCart.isVisible = true
            miCart.number = count

        })

        // Remove cart count
        viewModel.shouldRemoveCartCountBadge.observe(this, Observer {
            if (it) {
                // Remove badge
                binding.bnvHome.removeBadge(R.id.mi_home_cart)
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Timber.d("onNavigationItemSelected: Selected")
        when (item.itemId) {
            R.id.mi_home_cart -> {
                startActivity(CartActivity.getStartIntent(this))
            }
        }
        return false
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        // nothing
        onNavigationItemSelected(item)
    }
}