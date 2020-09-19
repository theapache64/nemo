package com.theapache64.nemo.data.repository

import com.theapache64.nemo.data.remote.NemoApi
import javax.inject.Inject

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 07:55
 */
class BannersRepo @Inject constructor(
    private val nemoApi: NemoApi
) {
    fun getBanners() = nemoApi.getBanners()
}