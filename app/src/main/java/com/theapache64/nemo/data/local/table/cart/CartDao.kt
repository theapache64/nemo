package com.theapache64.nemo.data.local.table.cart

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    fun getCartProductsFlow(): Flow<List<CartEntity>>

    @Insert
    suspend fun addToCart(cartEntity: CartEntity)

    @Update
    suspend fun updateCart(cartEntity: CartEntity)

    @Delete
    suspend fun remove(cartEntity: CartEntity)

    @Query("SELECT COUNT(id) FROM cart")
    suspend fun getCount(): Int
}