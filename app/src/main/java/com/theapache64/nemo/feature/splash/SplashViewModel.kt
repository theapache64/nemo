package com.theapache64.nemo.feature.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import com.theapache64.nemo.feature.base.BaseViewModel
import kotlinx.coroutines.delay

/**
 * Created by theapache64 : Jul 26 Sun,2020 @ 22:07
 */
class SplashViewModel @ViewModelInject constructor(
) : BaseViewModel() {

    val shouldGoToProducts = liveData {
        delay(1500)
        emit(true)
    }
}