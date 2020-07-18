package com.theapache64.nemo.feature.products

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.theapache64.nemo.data.repositories.ProductsRepository

/**
 * Created by theapache64 : Jul 17 Fri,2020 @ 20:30
 * Copyright (c) 2020
 * All rights reserved
 */
class ProductsViewModel @ViewModelInject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel(), ProductsAdapter.Callback {

    val products = liveData {
        emit(productsRepository.getProducts())
    }

    override fun onAddToCartClicked(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onProductClicked(position: Int) {
        TODO("Not yet implemented")
    }
}