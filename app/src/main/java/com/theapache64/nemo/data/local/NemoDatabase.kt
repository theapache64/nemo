package com.theapache64.nemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.theapache64.nemo.data.local.table.addresses.AddressEntity
import com.theapache64.nemo.data.local.table.addresses.AddressesDao
import com.theapache64.nemo.data.local.table.cart.CartDao
import com.theapache64.nemo.data.local.table.cart.CartEntity

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 15:10
 */
@Database(entities = [CartEntity::class, AddressEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class NemoDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun addressesDao(): AddressesDao
}