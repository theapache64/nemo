package com.theapache64.nemo.di.module

import com.theapache64.nemo.data.remote.NemoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by theapache64 : Nov 22 Sun,2020 @ 16:08
 */
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideNemoApi(retrofit: Retrofit): NemoApi {
        return retrofit.create(NemoApi::class.java)
    }
}