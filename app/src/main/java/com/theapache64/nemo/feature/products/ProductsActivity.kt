package com.theapache64.nemo.feature.products

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.theapache64.nemo.R
import com.theapache64.nemo.data.remote.Category
import com.theapache64.nemo.databinding.ActivityProductsBinding
import com.theapache64.nemo.feature.base.BaseActivity
import com.theapache64.nemo.feature.productdetail.ProductDetailActivity
import com.theapache64.nemo.utils.calladapter.flow.Resource
import com.theapache64.nemo.utils.extensions.get
import com.theapache64.nemo.utils.extensions.gone
import com.theapache64.nemo.utils.extensions.toast
import com.theapache64.nemo.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProductsActivity :
    BaseActivity<ActivityProductsBinding, ProductsViewModel>(R.layout.activity_products) {

    companion object {
        private const val KEY_CATEGORY = "category"
        fun getStartIntent(context: Context, category: Category): Intent {
            return Intent(context, ProductsActivity::class.java).apply {
                // data goes here
                putExtra(KEY_CATEGORY, category)
            }
        }
    }


    override val viewModel: ProductsViewModel by viewModels()

    override fun onCreate() {
        binding.viewModel = viewModel
        setSupportActionBar(binding.mtProducts)

        val category = getParcelableOrThrow<Category>(KEY_CATEGORY)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = category.categoryName
        binding.root.post {
            supportActionBar?.subtitle =
                "${category.totalProducts} ${category.totalProducts.get("item")} available"
        }

        viewModel.init(category)
        watchConfig()
        watchProductDetail()
    }

    private fun watchProductDetail() {
        viewModel.shouldLaunchProductDetail.observe(this, Observer { productId ->
            startActivity(ProductDetailActivity.getStartIntent(this, productId))
        })
    }

    private fun watchConfig() {

        // Waiting for config
        viewModel.config.observe(this, Observer {
            val productsAdapter = ProductsAdapter(
                products = viewModel.products,
                callback = viewModel,
                config = it
            )
            binding.rvProducts.adapter = productsAdapter
            watchProducts(productsAdapter)
        })


    }

    private fun watchProducts(productsAdapter: ProductsAdapter) {

        viewModel.shouldClearProducts.observe(this, Observer { shouldClear ->
            if (shouldClear) {
                productsAdapter.notifyDataSetChanged()
            }
        })

        viewModel.productsResp.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    Timber.d("watchProducts: It's loading")
                    if (productsAdapter.itemCount == 0) {
                        // normal loading
                        binding.lvProducts.showLoading(R.string.products_loading)
                        binding.rvProducts.gone()
                    } else {
                        // pagination loading
                        binding.csrlProducts.isRefreshing = true
                    }
                }
                is Resource.Success -> {
                    if (viewModel.positionStart == 0) {
                        binding.lvProducts.hideLoading()
                        binding.rvProducts.visible()
                    } else {
                        binding.csrlProducts.isRefreshing = false
                    }
                    productsAdapter.notifyItemRangeInserted(
                        viewModel.positionStart,
                        viewModel.products.size
                    )
                }
                is Resource.Error -> {
                    if (productsAdapter.itemCount == 0) {
                        binding.rvProducts.gone()
                        binding.lvProducts.showError(it.errorData)
                    } else {
                        toast(it.errorData)
                        binding.csrlProducts.isRefreshing = false
                    }
                }
            }
        })

        addScrollEndListener {
            Timber.d("watchProducts: Scroll end reached")
            viewModel.onScrollEndReached()
        }
    }

    private fun addScrollEndListener(onEndReached: () -> Unit) {

        binding.rvProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                viewModel.visibleItemCount = recyclerView.childCount
                val layoutManager = recyclerView.layoutManager!! as GridLayoutManager
                viewModel.totalItemCount = layoutManager.itemCount
                viewModel.firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if (viewModel.isLoading) {
                    if (viewModel.totalItemCount > viewModel.previousTotal) {
                        viewModel.isLoading = false
                        viewModel.previousTotal = viewModel.totalItemCount
                    }
                }

                val isEndReached =
                    viewModel.totalItemCount - viewModel.visibleItemCount <= viewModel.firstVisibleItem + viewModel.visibleThreshold

                if (!viewModel.isLoading && isEndReached) {
                    // End has been reached
                    onEndReached()

                    // Do something
                    viewModel.isLoading = true
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}