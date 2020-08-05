package com.theapache64.nemo.feature.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.theapache64.nemo.data.repositories.ConfigRepo
import com.theapache64.nemo.feature.base.BaseViewModel
import com.theapache64.twinkill.network.utils.retrofit.adapters.flow.Resource
import com.theapache64.twinkill.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by theapache64 : Jul 26 Sun,2020 @ 22:07
 */
class SplashViewModel @ViewModelInject constructor(
    private val configRepo: ConfigRepo
) : BaseViewModel() {


    private val _shouldGoToProducts = SingleLiveEvent<Boolean>()
    val shouldGoToProducts: LiveData<Boolean> = _shouldGoToProducts

    private val _shouldShowConfigSyncError = SingleLiveEvent<Boolean>()
    val shouldShowConfigSyncError: LiveData<Boolean> = _shouldShowConfigSyncError

    private val _shouldShowProgress = SingleLiveEvent<Boolean>()
    val shouldShowProgress: LiveData<Boolean> = _shouldShowProgress

    init {
        syncConfig()
    }

    private fun syncConfig() {
        viewModelScope.launch {
            configRepo
                .getRemoteConfig()
                .collect {
                    when (it) {
                        is Resource.Loading -> {
                            _shouldShowProgress.value = true
                        }

                        is Resource.Success -> {
                            configRepo.saveConfigToLocal(it.data)
                            _shouldGoToProducts.value = true
                        }

                        is Resource.Error -> {
                            _shouldShowProgress.value = false
                            _shouldShowConfigSyncError.value = true
                        }
                    }
                }
        }
    }

    fun onRetryClicked() {
        syncConfig()
    }
}