package com.theapache64.nemo.feature.products

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.theapache64.nemo.data.remote.Product
import com.theapache64.nemo.data.repositories.ProductsRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.twinkill.utils.livedata.SingleLiveEvent
import timber.log.Timber

/**
 * Created by theapache64 : Jul 17 Fri,2020 @ 20:30
 * Copyright (c) 2020
 * All rights reserved
 */
class ProductsViewModel @ViewModelInject constructor(
    private val productsRepo: ProductsRepo
) : BaseViewModel(), ProductsAdapter.Callback {

    private val _shouldLoadProducts = SingleLiveEvent<Boolean>()
    val products = _shouldLoadProducts.switchMap {
        productsRepo.getProducts()
            .asLiveData(viewModelScope.coroutineContext)
    }

    private val _shouldLaunchProductDetail = MutableLiveData<Int>()
    val shouldLaunchProductDetail: LiveData<Int> = _shouldLaunchProductDetail

    init {
        _shouldLoadProducts.value = true
    }

    override fun onAddToCartClicked(position: Int) {

    }

    override fun onProductClicked(position: Int, product: Product) {
        _shouldLaunchProductDetail.value = product.id
    }

    fun onRetryOrSwipeDown() {
        Timber.d("onRetryClicked: Clicked...")
        _shouldLoadProducts.value = true
    }
}