package com.theapache64.nemo.feature.productdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.theapache64.nemo.R
import com.theapache64.nemo.data.remote.Product
import com.theapache64.nemo.data.repositories.CartRepo
import com.theapache64.nemo.data.repositories.ConfigRepo
import com.theapache64.nemo.data.repositories.ProductsRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.nemo.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Created by theapache64 : Jul 26 Sun,2020 @ 22:53
 */
class ProductDetailViewModel @ViewModelInject constructor(
    private val productsRepo: ProductsRepo,
    private val configRepo: ConfigRepo,
    private val cartRepo: CartRepo
) : BaseViewModel() {

    private val _isAddToCartVisible = MutableLiveData<Boolean>()
    val isAddToCartVisible: LiveData<Boolean> = _isAddToCartVisible

    private val _isGoToCartVisible = MutableLiveData<Boolean>()
    val isGoToCartVisible: LiveData<Boolean> = _isGoToCartVisible

    val config = configRepo.getLocalConfig()
    val product = MutableLiveData<Product>()
    private val _productId = MutableLiveData<Int>()
    val productResp = _productId.switchMap { productId ->
        productsRepo.getProduct(productId)
            .onEach { response ->
                if (response is Resource.Success) {
                    response.data.let {
                        product.value = it

                        // Checking if we want to show or cart buttons
                        val cart = cartRepo.getCart()
                        val hasProductInCart = cart.find { cartItem ->
                            cartItem.productId == it.id
                        } != null

                        if (hasProductInCart) {
                            onProductExistInCart()
                        } else {
                            _isAddToCartVisible.value = true
                            _isGoToCartVisible.value = false
                        }
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

    fun onAddToCartClicked() {
        viewModelScope.launch {
            cartRepo.addToCart(product.value!!.id)
            onProductExistInCart()
            _toastMsg.value = R.string.toast_added_to_cart
        }
    }

    private fun onProductExistInCart() {
        _isAddToCartVisible.value = false
        _isGoToCartVisible.value = true
    }

    private val _shouldGoToCart = MutableLiveData<Boolean>()
    val shouldGoToCart: LiveData<Boolean> = _shouldGoToCart

    private val _shouldBuyNow = MutableLiveData<Int>()
    val shouldBuyNow: LiveData<Int> = _shouldBuyNow

    fun onGoToCartClicked() {
        _shouldGoToCart.value = true
    }

    fun onBuyNowClicked() {
        _shouldBuyNow.value = product.value!!.id
    }
}