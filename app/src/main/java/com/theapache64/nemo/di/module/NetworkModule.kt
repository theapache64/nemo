package com.theapache64.nemo.di.module

import com.theapache64.nemo.data.remote.NemoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by theapache64 : Jul 17 Fri,2020 @ 21:17
 * Copyright (c) 2020
 * All rights reserved
 */
@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideNemoApi(): NemoApi {
        return Retrofit.Builder()
            .baseUrl("https://v2-api.sheety.co/0700786fe358bfb8e616e4d097fc7dc4/nemo/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(NemoApi::class.java)
    }

}