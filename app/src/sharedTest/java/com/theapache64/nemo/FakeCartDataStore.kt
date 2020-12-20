 package com.theapache64.nemo

import com.theapache64.nemo.data.local.table.cart.CartEntity
import kotlinx.coroutines.flow.flowOf

/**
 * Created by theapache64 : Dec 19 Sat,2020 @ 02:40
 */
object FakeCartDataStore {

    const val PRODUCTS_COUNT = 10
    val cart10Products by lazy {
        val cartList = mutableListOf<CartEntity>().apply {
            repeat(PRODUCTS_COUNT) {
                add(CartEntity(it, it * 2))
            }
        }

        return@lazy flowOf(cartList)
    }

    val cart1Product by lazy {
        return@lazy flowOf(listOf(CartEntity(1, 10)))
    }
}