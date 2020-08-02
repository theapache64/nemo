package com.theapache64.nemo.feature.products

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.theapache64.nemo.data.remote.Product
import com.theapache64.nemo.data.repositories.ConfigRepo
import com.theapache64.nemo.data.repositories.ProductsRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.twinkill.utils.livedata.SingleLiveEvent

/**
 * Created by theapache64 : Jul 17 Fri,2020 @ 20:30
 * Copyright (c) 2020
 * All rights reserved
 */
class ProductsViewModel @ViewModelInject constructor(
    private val productsRepo: ProductsRepo,
    private val configRepo: ConfigRepo
) : BaseViewModel(), ProductsAdapter.Callback {

    val visibleThreshold = 0
    var previousTotal = -1
    var isLoading: Boolean = false
    var firstVisibleItem = -1
    var totalItemCount = -1
    var visibleItemCount = -1

    private val _pageNo = SingleLiveEvent<Int>()
    val products = _pageNo.switchMap { pageNo ->
        val config = configRepo.getLocalConfig()!!
        productsRepo.getProducts(pageNo, config)
            .asLiveData(viewModelScope.coroutineContext)
    }

    private val _shouldLaunchProductDetail = MutableLiveData<Int>()
    val shouldLaunchProductDetail: LiveData<Int> = _shouldLaunchProductDetail

    private val _shouldClearProducts = MutableLiveData<Boolean>()
    val shouldClearProducts: LiveData<Boolean> = _shouldClearProducts

    init {
        onRetryOrSwipeDown()
    }

    override fun onAddToCartClicked(position: Int) {

    }

    override fun onProductClicked(position: Int, product: Product) {
        _shouldLaunchProductDetail.value = product.id
    }

    fun onRetryOrSwipeDown() {
        _shouldClearProducts.value = true
        _pageNo.value = 1
    }

    fun onScrollEndReached() {
        _pageNo.value = _pageNo.value?.plus(1)
    }
}