package com.theapache64.nemo.di.module

import com.squareup.moshi.Moshi
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.utils.AppConfig
import com.theapache64.nemo.utils.calladapter.flow.FlowResourceCallAdapterFactory
import com.github.theapache64.retrosheet.RetrosheetInterceptor
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
object NetworkModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .build()
    }


    @Singleton
    @Provides
    fun provideRetrosheetInterceptor(): RetrosheetInterceptor {
        return RetrosheetInterceptor.Builder()
            .setLogging(true)
            .addSheet(
                AppConfig.SHEET_BANNERS,
                "id", "image_url", "product_id", "product_name"
            )
            .addSheet(
                AppConfig.SHEET_CATEGORIES,
                "id", "category_name", "image_url", "total_products"
            )
            .addSheet(
                AppConfig.SHEET_PRODUCTS,
                "id",
                "category_name",
                "title",
                "more_details",
                "thumb_url",
                "image_url",
                "is_out_of_stock",
                "rating",
                "no_of_reviews",
                "price"
            )
            .addSheet(
                AppConfig.SHEET_CONFIG,
                "total_products",
                "products_per_page",
                "total_pages",
                "currency",
                "delivery_charge"
            )
            .addForm(
                AppConfig.FORM_APP_OPEN,
                "https://docs.google.com/forms/d/e/1FAIpQLSeFkXRNcPgm1e3SYyMv0OcZJUj_POQJfkVbyFbSiDhVXo_Fkw/viewform?usp=sf_link"
            )
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
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://docs.google.com/spreadsheets/d/1IcZTH6-g7cZeht_xr82SHJOuJXD_p55QueMrZcnsAvQ/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(FlowResourceCallAdapterFactory())
            .build()
    }
}