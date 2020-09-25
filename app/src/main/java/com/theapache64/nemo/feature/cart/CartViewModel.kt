package com.theapache64.nemo.feature.cart

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.theapache64.nemo.data.repository.CartRepo
import com.theapache64.nemo.data.repository.ConfigRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.nemo.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 14:52
 */
class CartViewModel @ViewModelInject constructor(
    private val cartRepo: CartRepo,
    configRepo: ConfigRepo
) : BaseViewModel(), CartAdapter.Callback {

    val config = configRepo.getLocalConfig()!!
    private val _shouldLoadCart = MutableLiveData<Boolean>()
    private var cartItems: List<CartItem>? = null

    private val _shouldNotifyItemChanged = MutableLiveData<Int>()
    val shouldNotifyItemChanged: LiveData<Int> = _shouldNotifyItemChanged

    private val _shouldNotifyItemRemoved = MutableLiveData<Int>()
    val shouldNotifyItemRemoved: LiveData<Int> = _shouldNotifyItemRemoved

    private val _amountPayable = MutableLiveData<Int>()
    val amountPayable: LiveData<Int> = _amountPayable

    val cartItemResponse: LiveData<Resource<List<CartItem>>> = _shouldLoadCart.switchMap {
        cartRepo.getCartItems()
            .onEach {
                if (it is Resource.Success) {
                    cartItems = it.data
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

    override fun onQuantityChanged(position: Int, count: Int) {
        cartItems?.get(position)?.let { cartItem ->
            cartItem.cartProduct.count = count
            viewModelScope.launch {
                cartRepo.update(cartItem.cartProduct)
                _shouldNotifyItemChanged.value = position
            }
        }
    }

    override fun onRemoveClicked(position: Int) {
        cartItems?.get(position)?.let { cartItem ->
            // TODO :
        }
    }
}