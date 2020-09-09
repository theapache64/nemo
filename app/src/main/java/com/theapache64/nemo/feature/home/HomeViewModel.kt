package com.theapache64.nemo.feature.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.theapache64.nemo.data.remote.Banner
import com.theapache64.nemo.data.repositories.BannersRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.nemo.utils.calladapter.flow.Resource
import com.theapache64.nemo.utils.livedata.SingleLiveEvent

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 07:10
 */
class HomeViewModel @ViewModelInject constructor(
    private val bannersRepo: BannersRepo
) : BaseViewModel() {

    private val shouldRefresh = SingleLiveEvent<Boolean>()

    val banners: LiveData<Resource<List<Banner>>> = shouldRefresh.switchMap {
        bannersRepo.getBanners().asLiveData()
    }

    init {
        shouldRefresh.value = true
    }

    fun refresh() {
        shouldRefresh.value = true
    }

}