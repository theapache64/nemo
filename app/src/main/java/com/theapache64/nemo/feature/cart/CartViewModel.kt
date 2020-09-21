package com.theapache64.nemo.feature.cart

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.theapache64.nemo.data.repository.CartRepo
import com.theapache64.nemo.data.repository.ConfigRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.nemo.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.onEach

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 14:52
 */
class CartViewModel @ViewModelInject constructor(
    private val cartRepo: CartRepo,
    configRepo: ConfigRepo
) : BaseViewModel() {

    val config = configRepo.getLocalConfig()!!
    private val _shouldLoadCart = MutableLiveData<Boolean>()

    private val _amountPayable = MutableLiveData<Int>()
    val amountPayable: LiveData<Int> = _amountPayable

    val cartItems: LiveData<Resource<List<CartItem>>> = _shouldLoadCart.switchMap {
        cartRepo.getCartItems()
            .onEach {
                if (it is Resource.Success) {
                    
                }
            }
            .asLiveData(viewModelScope.coroutineContext)
    }

    init {
        loadCart()
    }


    fun loadCart() {
        _shouldLoadCart.value = true
    }
}