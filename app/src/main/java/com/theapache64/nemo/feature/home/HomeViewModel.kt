package com.theapache64.nemo.feature.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.theapache64.nemo.data.repository.BannersRepo
import com.theapache64.nemo.data.repository.CategoriesRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.nemo.utils.livedata.SingleLiveEvent

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 07:10
 */
class HomeViewModel @ViewModelInject constructor(
    private val bannersRepo: BannersRepo,
    private val categoriesRepo: CategoriesRepo
) : BaseViewModel() {

    private val shouldRefresh = SingleLiveEvent<Boolean>()

    val banners = shouldRefresh.switchMap {
        bannersRepo.getBanners()
            .asLiveData(viewModelScope.coroutineContext)
    }

    val categories = shouldRefresh.switchMap {
        categoriesRepo.getCategories()
            .asLiveData(viewModelScope.coroutineContext)
    }

    init {
        shouldRefresh.value = true
    }

    fun refresh() {
        shouldRefresh.value = true
    }

}