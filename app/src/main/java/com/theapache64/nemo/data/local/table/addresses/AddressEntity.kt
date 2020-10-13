package com.theapache64.nemo.data.local.table.addresses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Created by theapache64 : Oct 10 Sat,2020 @ 13:41
 */
@Entity(
    tableName = "addresses"
)
data class AddressEntity(
    @ColumnInfo(name = "pin_code")
    val pinCode: Int,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "road_name")
    val roadName: String,
    @ColumnInfo(name = "city")
    val city: String,
    @ColumnInfo(name = "state")
    val state: String,
    @ColumnInfo(name = "landmark")
    val landmark: String?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "mobile")
    val mobile: Int,
    @ColumnInfo(name = "mobile_2")
    val mobile2: Int?,
    @ColumnInfo(name = "type")
    val type: Type
) {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @Ignore
    var isSelected: Boolean = false

    enum class Type {
        HOME, OFFICE
    }
}
