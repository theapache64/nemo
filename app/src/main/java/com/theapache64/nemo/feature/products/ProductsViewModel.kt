package com.theapache64.nemo.feature.products

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.theapache64.nemo.data.remote.Category
import com.theapache64.nemo.data.remote.Config
import com.theapache64.nemo.data.remote.Product
import com.theapache64.nemo.data.repository.ConfigRepo
import com.theapache64.nemo.data.repository.ProductsRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.nemo.utils.calladapter.flow.Resource
import com.theapache64.nemo.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

/**
 * Created by theapache64 : Jul 17 Fri,2020 @ 20:30
 * Copyright (c) 2020
 * All rights reserved
 */
class ProductsViewModel @ViewModelInject constructor(
    private val productsRepo: ProductsRepo,
    private val configRepo: ConfigRepo
) : BaseViewModel(), ProductsAdapter.Callback {

    var positionStart: Int = -1
    private lateinit var category: Category
    val config: LiveData<Config> = MutableLiveData(configRepo.getLocalConfig())

    val visibleThreshold = 5
    var previousTotal = -1
    var isLoading: Boolean = true
    var firstVisibleItem = -1
    var totalItemCount = -1
    var visibleItemCount = -1

    private val _pageNo = SingleLiveEvent<Int>()
    val products = mutableListOf<Product>()


    fun init(category: Category) {
        this.category = category


        // Load first page
        _pageNo.value = 1
    }

    val productsResp :LiveData<Resource<List<Product>>> = _pageNo.switchMap { pageNo ->
        productsRepo.getProducts(category, pageNo)
            .onEach {
                if (it is Resource.Success) {
                    positionStart = products.size
                    products.addAll(it.data)
                }
            }
            .asLiveData(viewModelScope.coroutineContext)
    }

    private val _shouldLaunchProductDetail = SingleLiveEvent<Int>()
    val shouldLaunchProductDetail: LiveData<Int> = _shouldLaunchProductDetail

    private val _shouldUpdateRecyclerView = SingleLiveEvent<Boolean>()
    val shouldClearProducts: LiveData<Boolean> = _shouldUpdateRecyclerView


    private fun resetPageData() {
        _pageNo.value = 1
        totalItemCount = 0
        visibleItemCount = 0
        firstVisibleItem = 0
        previousTotal = 0
    }

    override fun onAddToCartClicked(position: Int) {

    }

    override fun onProductClicked(position: Int, product: Product) {
        _shouldLaunchProductDetail.value = product.id
    }

    fun onRetryOrSwipeDown() {
        products.clear()
        _shouldUpdateRecyclerView.value = true
        resetPageData()
    }

    fun onScrollEndReached() {
        Timber.d("onScrollEndReached: ${_pageNo.value}")
        val totalPages = config.value!!.totalPages
        val nextPageNo = _pageNo.value!!.plus(1)
        if (nextPageNo <= totalPages) {
            _pageNo.value = nextPageNo
        } else {
            Timber.d("onScrollEndReached: All products loaded.")
        }
    }


}