package com.theapache64.nemo.feature.cart

import javax.inject.Inject
import androidx.lifecycle.*
import com.theapache64.nemo.data.repository.CartRepo
import com.theapache64.nemo.data.repository.ConfigRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.nemo.utils.calladapter.flow.Resource
import com.theapache64.nemo.utils.livedata.SingleLiveEvent
import com.theapache64.nemo.utils.test.EspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 14:52
 */
@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepo: CartRepo,
    configRepo: ConfigRepo
) : BaseViewModel(), CartAdapter.Callback {

    val config = configRepo.getLocalConfig()!!
    private val _shouldLoadCart = MutableLiveData<Boolean>()
    private var cartItems: MutableList<CartItem>? = null

    private val _shouldNotifyItemChanged = MutableLiveData<Int>()
    val shouldNotifyItemChanged: LiveData<Int> = _shouldNotifyItemChanged

    private val _shouldNotifyItemRemoved = MutableLiveData<Int>()
    val shouldNotifyItemRemoved: LiveData<Int> = _shouldNotifyItemRemoved

    private val _shouldShowCartEmpty = SingleLiveEvent<Boolean>()
    val shouldShowCartEmpty: LiveData<Boolean> = _shouldShowCartEmpty

    private val _amountPayable = MutableLiveData<Int>()
    val amountPayable: LiveData<Int> = _amountPayable

    val cartItemResponse: LiveData<Resource<List<CartItem>>> = _shouldLoadCart.switchMap {
        cartRepo.getCartItems()
            .onEach {
                when (it) {
                    is Resource.Loading -> {
                        EspressoIdlingResource.increment()
                    }
                    is Resource.Success -> {
                        cartItems = it.data.toMutableList()

                        // Calculating amountPayable
                        refreshPrice()
                        EspressoIdlingResource.decrement()
                    }
                    is Resource.Error -> {
                        EspressoIdlingResource.decrement()
                    }
                }
            }
            .filter {
                if (it is Resource.Error && it.errorCode == CartRepo.ERROR_CART_EMPTY) {
                    _shouldShowCartEmpty.value = true
                    false
                } else {
                    true
                }
            }
            .asLiveData(viewModelScope.coroutineContext)
    }

    private fun refreshPrice() {
        _amountPayable.value =
            cartItems?.sumBy { product -> product.product.price * product.cartEntity.count } ?: 0
    }

    init {
        loadCart()
    }


    fun loadCart() {
        _shouldLoadCart.value = true
    }

    override fun onQuantityChanged(position: Int, count: Int) {
        cartItems?.get(position)?.let { cartItem ->
            cartItem.cartEntity.count = count
            viewModelScope.launch {
                cartRepo.update(cartItem.cartEntity)
                _shouldNotifyItemChanged.value = position
                refreshPrice()
            }
        }
    }

    override fun onRemoveClicked(position: Int) {
        viewModelScope.launch {
            cartItems?.get(position)?.let { cartItem ->
                cartRepo.remove(cartItem.cartEntity)
                cartItems?.removeAt(position)
                _shouldNotifyItemRemoved.value = position
                refreshPrice()

                if (cartItems.isNullOrEmpty()) {
                    // No cart items
                    _shouldShowCartEmpty.value = true
                }
            }
        }
    }

    private val _shouldLaunchAddressList = SingleLiveEvent<Boolean>()
    val shouldLaunchAddressList: LiveData<Boolean> = _shouldLaunchAddressList
    fun onPlaceOrderClicked() {
        _shouldLaunchAddressList.value = true
    }
}