package com.theapache64.nemo.di.module

import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.retrosheet.RetrosheetInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
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
    fun provideRetrosheetInterceptor(): RetrosheetInterceptor {
        return RetrosheetInterceptor.Builder()
            .addSmartQueryMap(
                "products", mapOf(
                    "id" to "A",
                    "title" to "B",
                    "image_url" to "C",
                    "price" to "D",
                    "quantity" to "E"
                )
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(retrosheetInterceptor: RetrosheetInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RetrosheetInterceptor.Builder().build())
            .build()
    }

    @Singleton
    @Provides
    fun provideNemoApi(okHttpClient: OkHttpClient): NemoApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://docs.google.com/spreadsheets/d/1IcZTH6-g7cZeht_xr82SHJOuJXD_p55QueMrZcnsAvQ/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(NemoApi::class.java)
    }

}