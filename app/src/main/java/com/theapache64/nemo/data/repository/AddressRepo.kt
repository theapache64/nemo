package com.theapache64.nemo.data.repository

import com.theapache64.nemo.data.local.table.addresses.AddressesDao
import javax.inject.Inject

/**
 * Created by theapache64 : Oct 10 Sat,2020 @ 19:56
 */
class AddressRepo @Inject constructor(
    private val addressesDao: AddressesDao
) {
    suspend fun getAddresses() = addressesDao.getAddressed()
}