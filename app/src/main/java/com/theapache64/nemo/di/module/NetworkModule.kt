package com.theapache64.nemo.di.module

import com.squareup.moshi.Moshi
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.utils.flow.FlowResourceCallAdapterFactory
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
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Singleton
    @Provides
    fun provideRetrosheetInterceptor(): RetrosheetInterceptor {
        return RetrosheetInterceptor.Builder()
            .addSheet(
                "products",
                "id", "title", "image_url", "is_out_of_stock", "rating", "price"
            )
            .setLogging(true)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(retrosheetInterceptor: RetrosheetInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(retrosheetInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideNemoApi(okHttpClient: OkHttpClient, moshi: Moshi): NemoApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://docs.google.com/spreadsheets/d/1IcZTH6-g7cZeht_xr82SHJOuJXD_p55QueMrZcnsAvQ/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(FlowResourceCallAdapterFactory())
            .build()
            .create(NemoApi::class.java)
    }

}