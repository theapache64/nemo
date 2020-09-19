package com.theapache64.nemo.feature.productdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.theapache64.nemo.data.remote.Product
import com.theapache64.nemo.data.repositories.ConfigRepo
import com.theapache64.nemo.data.repositories.ProductsRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.nemo.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.onEach

/**
 * Created by theapache64 : Jul 26 Sun,2020 @ 22:53
 */
class ProductDetailViewModel @ViewModelInject constructor(
    private val productsRepo: ProductsRepo,
    private val configRepo: ConfigRepo
) : BaseViewModel() {

    val config = configRepo.getLocalConfig()
    val product = MutableLiveData<Product>()
    private val _productId = MutableLiveData<Int>()
    val productResp = _productId.switchMap { productId ->
        productsRepo.getProduct(productId)
            .onEach { response ->
                if (response is Resource.Success) {
                    response.data.let {
                        product.value = it
                    }
                }
            }
            .asLiveData(viewModelScope.coroutineContext)
    }

    fun init(productId: Int) {
        _productId.value = productId
    }

    fun reload() {
        _productId.value = _productId.value
    }
}