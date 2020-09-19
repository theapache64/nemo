package com.theapache64.nemo.data.repository

import com.theapache64.nemo.data.local.table.CartDao
import com.theapache64.nemo.data.local.table.CartProduct
import javax.inject.Inject

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 14:22
 */
class CartRepo @Inject constructor(
    private val cartDao: CartDao
) {
    /**
     * To get cart's product ids
     *
     * @return Set<Int>
     */
    suspend fun getCart(): List<CartProduct> {
        return cartDao.getCart()
    }


    /**
     * To add given product to cart
     *
     * @param productId
     */
    suspend fun addToCart(productId: Int) {
        cartDao.addToCart(CartProduct(productId, 1))
    }
}