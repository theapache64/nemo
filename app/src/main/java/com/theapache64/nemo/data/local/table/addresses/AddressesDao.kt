package com.theapache64.nemo.data.local.table.addresses

import androidx.room.Dao
import androidx.room.Query

/**
 * Created by theapache64 : Oct 10 Sat,2020 @ 19:13
 */
@Dao
interface AddressesDao {

    @Query("SELECT * FROM addresses")
    suspend fun getAddressed(): List<AddressEntity>
}