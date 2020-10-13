package com.theapache64.nemo.data.local

import androidx.room.TypeConverter
import com.theapache64.nemo.data.local.table.addresses.AddressEntity

/**
 * Created by theapache64 : Oct 13 Tue,2020 @ 23:40
 */
class Converters {

    @TypeConverter
    fun fromAddressType(type: AddressEntity.Type): String {
        return type.name
    }

    @TypeConverter
    fun toAddressType(addressTypeName: String): AddressEntity.Type {
        return AddressEntity.Type.valueOf(addressTypeName)
    }
}