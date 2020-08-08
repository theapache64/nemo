package com.theapache64.nemo.data.repositories

import android.os.Build
import com.theapache64.nemo.data.remote.AppOpen
import com.theapache64.nemo.data.remote.NemoApi
import javax.inject.Inject

/**
 * Created by theapache64 : Aug 08 Sat,2020 @ 21:28
 */
class AnalyticsRepo @Inject constructor(
    private val nemoApi: NemoApi
) {
    suspend fun reportAppOpen() {
        nemoApi.reportAppOpen(AppOpen(Build.DEVICE))
    }
}