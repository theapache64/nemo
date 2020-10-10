package com.theapache64.nemo.utils

import androidx.annotation.StringRes
import com.theapache64.nemo.R
import com.theapache64.nemo.data.local.table.addresses.AddressEntity

/**
 * Created by theapache64 : Oct 10 Sat,2020 @ 19:39
 */
object AddressUtils {
    @StringRes
    fun getType(addressEntity: AddressEntity): Int {
        return when (addressEntity.type) {
            AddressEntity.Type.HOME -> R.string.address_type_home
            AddressEntity.Type.OFFICE -> R.string.address_type_office
        }
    }
}