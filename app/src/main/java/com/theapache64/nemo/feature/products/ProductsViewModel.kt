package com.theapache64.nemo.feature.products

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.theapache64.nemo.data.repositories.ProductsRepository
import com.theapache64.twinkill.utils.livedata.SingleLiveEvent
import timber.log.Timber

/**
 * Created by theapache64 : Jul 17 Fri,2020 @ 20:30
 * Copyright (c) 2020
 * All rights reserved
 */
class ProductsViewModel @ViewModelInject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel(), ProductsAdapter.Callback {

    private val _shouldLoadProducts = SingleLiveEvent<Boolean>()
    val products = _shouldLoadProducts.switchMap {
        productsRepository.getProducts()
            .asLiveData(viewModelScope.coroutineContext)
    }

    init {
        _shouldLoadProducts.value = true
    }

    override fun onAddToCartClicked(position: Int) {

    }

    override fun onProductClicked(position: Int) {

    }

    fun onRetryOrSwipeDown() {
        Timber.d("onRetryClicked: Clicked...")
        _shouldLoadProducts.value = true
    }
}