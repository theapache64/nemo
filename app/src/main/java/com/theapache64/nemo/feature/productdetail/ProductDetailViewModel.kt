package com.theapache64.nemo.feature.productdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.theapache64.nemo.data.repositories.ProductsRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.nemo.utils.flow.Resource
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

/**
 * Created by theapache64 : Jul 26 Sun,2020 @ 22:53
 */
class ProductDetailViewModel @ViewModelInject constructor(
    private val productsRepo: ProductsRepo
) : BaseViewModel() {

    val productTitle = MutableLiveData<String>()
    private val _productId = MutableLiveData<Int>()
    val product = _productId.switchMap { productId ->
        productsRepo.getProduct(productId)
            .onEach {
                if (it is Resource.Success) {
                    Timber.d("Setting title: ${it.data.title} ")
                    productTitle.value = it.data.title
                }
            }
            .asLiveData(viewModelScope.coroutineContext)
    }

    fun init(productId: Int) {
        _productId.value = productId
    }
}