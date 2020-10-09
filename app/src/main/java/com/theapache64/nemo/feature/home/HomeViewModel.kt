package com.theapache64.nemo.feature.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.theapache64.nemo.data.repository.BannersRepo
import com.theapache64.nemo.data.repository.CartRepo
import com.theapache64.nemo.data.repository.CategoriesRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.nemo.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.launch

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 07:10
 */
class HomeViewModel @ViewModelInject constructor(
    private val bannersRepo: BannersRepo,
    private val categoriesRepo: CategoriesRepo,
    private val cartRepo: CartRepo
) : BaseViewModel(), LifecycleObserver {

    private val shouldRefreshPage = SingleLiveEvent<Boolean>()

    private val _addCartCountBadge = MutableLiveData<Int>()
    val addCartCountBadge: LiveData<Int> = _addCartCountBadge

    private val _shouldRemoveCartCountBadge = MutableLiveData<Boolean>()
    val shouldRemoveCartCountBadge: LiveData<Boolean> = _shouldRemoveCartCountBadge

    val banners = shouldRefreshPage.switchMap {
        bannersRepo.getBanners()
            .asLiveData(viewModelScope.coroutineContext)
    }

    val categories = shouldRefreshPage.switchMap {
        categoriesRepo.getCategories()
            .asLiveData(viewModelScope.coroutineContext)
    }

    init {
        shouldRefreshPage.value = true
    }

    fun refreshPage() {
        shouldRefreshPage.value = true
    }

    fun refreshCartCount() {
        viewModelScope.launch {
            cartRepo.getCartCount().let { cartCount->
                if (cartCount > 0) {
                    _addCartCountBadge.value = cartCount
                } else {
                    _shouldRemoveCartCountBadge.value = true
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onLifecycleResumed() {
        refreshCartCount()
    }
}