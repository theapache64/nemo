package com.theapache64.nemo.di.module

import android.content.Context
import androidx.room.Room
import com.theapache64.nemo.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * Created by theapache64 : Aug 05 Wed,2020 @ 22:59
 */
@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "com.theapache64.nemo_db"
    ).build()

    @Singleton
    @Provides
    fun provideProductsDao(appDatabase: AppDatabase) = appDatabase.productsDao()
}