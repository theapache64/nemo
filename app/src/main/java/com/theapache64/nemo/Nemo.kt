package com.theapache64.nemo

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.theapache64.twinkill.TwinKill
import com.theapache64.twinkill.googlesans.GoogleSans
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by theapache64 : Jul 17 Fri,2020 @ 20:24
 * Copyright (c) 2020
 * All rights reserved
 */
@HiltAndroidApp
class Nemo : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )

        TwinKill.init(
            TwinKill.builder()
                .setDefaultFont(GoogleSans.Regular)
                .build()
        )
    }
}