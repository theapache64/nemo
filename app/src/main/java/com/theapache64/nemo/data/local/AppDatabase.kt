package com.theapache64.nemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.theapache64.nemo.data.local.daos.ProductsDao
import com.theapache64.nemo.data.remote.Product

/**
 * Created by theapache64 : Aug 05 Wed,2020 @ 23:00
 */
@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
}