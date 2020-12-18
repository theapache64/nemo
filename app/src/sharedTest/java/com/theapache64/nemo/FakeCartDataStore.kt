package com.theapache64.nemo

import com.theapache64.nemo.data.local.table.cart.CartEntity
import kotlinx.coroutines.flow.flow

/**
 * Created by theapache64 : Dec 19 Sat,2020 @ 02:40
 */
object FakeCartDataStore {
    val cart10Products by lazy {
        flow<List<CartEntity>> {
            val cartList = mutableListOf<CartEntity>().apply {
                repeat(10) {
                    add(CartEntity(it, it * 2))
                }
            }
            emit(cartList)
        }
    }
}