package com.theapache64.nemo.di.module

import com.theapache64.nemo.data.local.NemoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Created by theapache64 : Dec 17 Thu,2020 @ 21:43
 */
@Module
@InstallIn(ApplicationComponent::class)
object CartModule {
    @Provides
    fun provideCartDao(nemoDatabase: NemoDatabase) = nemoDatabase.cartDao()
}