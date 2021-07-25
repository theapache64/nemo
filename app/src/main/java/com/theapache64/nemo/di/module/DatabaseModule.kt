package com.theapache64.nemo.di.module

import android.content.Context
import androidx.room.Room
import com.theapache64.nemo.data.local.NemoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 15:18
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): NemoDatabase {
        return Room.databaseBuilder(context, NemoDatabase::class.java, "nemo.db")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    fun provideAddressesDao(nemoDatabase: NemoDatabase) = nemoDatabase.addressesDao()
}
