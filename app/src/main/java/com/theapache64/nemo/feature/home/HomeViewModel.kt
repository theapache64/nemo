package com.theapache64.nemo.feature.home

import javax.inject.Inject
import androidx.lifecycle.*
import com.theapache64.nemo.data.remote.Category
import com.theapache64.nemo.data.repository.BannersRepo
import com.theapache64.nemo.data.repository.CartRepo
import com.theapache64.nemo.data.repository.CategoriesRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.nemo.utils.calladapter.flow.Resource
import com.theapache64.nemo.utils.livedata.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 07:10
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bannersRepo: BannersRepo,
    private val categoriesRepo: CategoriesRepo,
    private val cartRepo: CartRepo
) : BaseViewModel(), LifecycleObserver {

    private val shouldRefreshPage = SingleLiveEvent<Boolean>()

    private val _addCartCountBadge = MutableLiveData<Int>()
    val addCartCountBadge: LiveData<Int> = _addCartCountBadge

    private val _shouldRemoveCartCountBadge = MutableLiveData<Boolean>()
    val shouldRemoveCartCountBadge: LiveData<Boolean> = _shouldRemoveCartCountBadge

    private val _shouldLaunchCategory = SingleLiveEvent<Category>()
    val shouldLaunchCategory: LiveData<Category> = _shouldLaunchCategory

    private val _shouldLaunchProduct = SingleLiveEvent<Int>()
    val shouldLaunchProduct: LiveData<Int> = _shouldLaunchProduct

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

    private fun refreshCartCount() {
        viewModelScope.launch {
            cartRepo.getCartCount().let { cartCount->
                if (cartCount > 0) {
                    _addCartCountBadge.postValue(cartCount)
                } else {
                    _shouldRemoveCartCountBadge.postValue(true)
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onLifecycleResumed() {
        refreshCartCount()
    }


    fun onBannerClicked(position: Int) {
        val banner = (banners.value as Resource.Success).data[position]

        if (banner.categoryId != null && categories.value is Resource.Success) {
            // Finding category
            (categories.value as Resource.Success).data.find {
                it.id == banner.categoryId
            }?.let { category ->
                // Found category
                _shouldLaunchCategory.value = category
            }
        } else if (banner.productId != null) {
            _shouldLaunchProduct.value = banner.productId!!
        }
    }

    fun onCategoryClicked(position: Int) {
        if (categories.value is Resource.Success) {
            val category = (categories.value as Resource.Success).data[position]
            _shouldLaunchCategory.value = category
        }
    }
}