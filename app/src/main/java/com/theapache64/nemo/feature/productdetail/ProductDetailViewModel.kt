package com.theapache64.nemo.feature.productdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.theapache64.nemo.R
import com.theapache64.nemo.data.remote.Product
import com.theapache64.nemo.data.repository.CartRepo
import com.theapache64.nemo.data.repository.ConfigRepo
import com.theapache64.nemo.data.repository.ProductsRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.nemo.utils.calladapter.flow.Resource
import com.theapache64.nemo.utils.livedata.SingleLiveEvent
import com.theapache64.nemo.utils.test.EspressoIdlingResource
import kotlinx.coroutines.flow.firstOrNull
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

    private val _isAddToCartVisible = MutableLiveData(false)
    val isAddToCartVisible: LiveData<Boolean> = _isAddToCartVisible

    private val _isGoToCartVisible = MutableLiveData(false)
    val isGoToCartVisible: LiveData<Boolean> = _isGoToCartVisible

    val config = configRepo.getLocalConfig()
    val product = MutableLiveData<Product>()
    private val _productId = MutableLiveData<Int>()
    val productResp = _productId.switchMap { productId ->
        productsRepo.getProduct(productId)
            .onEach { response ->
                when (response) {
                    is Resource.Loading -> {
                        EspressoIdlingResource.increment()
                    }

                    is Resource.Success -> {
                        response.data.let {
                            product.postValue(it)

                            // Checking if we want to show or cart buttons
                            cartRepo.getCartProductsFlow().firstOrNull()
                                ?.let { cart ->

                                    val hasProductInCart = cart.find { cartItem ->
                                        cartItem.productId == it.id
                                    } != null

                                    if (hasProductInCart) {
                                        onProductExistInCart()
                                    } else {
                                        onProductNotExistInCart()
                                    }
                                }
                        }

                        EspressoIdlingResource.decrement()
                    }
                    is Resource.Error -> {
                        EspressoIdlingResource.decrement()
                    }
                }
            }
            .asLiveData()
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
        println("Product exist in cart")
        _isAddToCartVisible.postValue(false)
        _isGoToCartVisible.postValue(true)
    }

    private fun onProductNotExistInCart() {
        println("Product doesn't exist in cart")
        _isAddToCartVisible.postValue(true)
        _isGoToCartVisible.postValue(false)
    }

    private val _shouldGoToCart = SingleLiveEvent<Boolean>()
    val shouldGoToCart: LiveData<Boolean> = _shouldGoToCart

    private val _shouldBuyNow = SingleLiveEvent<Int>()
    val shouldBuyNow: LiveData<Int> = _shouldBuyNow

    fun onGoToCartClicked() {
        _shouldGoToCart.value = true
    }

    fun onBuyNowClicked() {
        _shouldBuyNow.value = product.value!!.id
    }
}