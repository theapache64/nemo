package com.theapache64.nemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.theapache64.nemo.data.local.table.cart.CartDao
import com.theapache64.nemo.data.local.table.cart.CartEntity

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 15:10
 */
@Database(entities = [CartEntity::class], version = 1)
abstract class NemoDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}