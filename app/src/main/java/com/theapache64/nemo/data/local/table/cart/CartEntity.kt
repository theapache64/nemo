package com.theapache64.nemo.data.local.table.cart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 15:11
 */
@Entity(
    tableName = "cart",
    indices = [
        Index("product_id", unique = true)
    ]
)
data class CartEntity(
    @ColumnInfo(name = "product_id")
    val productId: Int,
    @ColumnInfo(name = "count")
    var count: Int
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}