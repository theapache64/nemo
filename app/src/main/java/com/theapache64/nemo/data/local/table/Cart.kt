package com.theapache64.nemo.data.local.table

import androidx.room.*

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 15:11
 */
@Entity(
    tableName = "cart",
    indices = [
        Index("product_id", unique = true)
    ]
)
data class CartProduct(
    @ColumnInfo(name = "product_id")
    val productId: Int,
    @ColumnInfo(name = "count")
    var count: Int
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    suspend fun getCart(): List<CartProduct>

    @Insert
    suspend fun addToCart(cartProduct: CartProduct)

    @Update
    suspend fun updateCart(cartProduct: CartProduct)

    @Delete
    fun remove(cartProduct: CartProduct)
}