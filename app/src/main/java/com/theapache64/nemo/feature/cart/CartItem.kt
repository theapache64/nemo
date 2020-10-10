package com.theapache64.nemo.feature.cart

import com.theapache64.nemo.data.local.table.cart.CartEntity
import com.theapache64.nemo.data.remote.Product

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 16:57
 */
class CartItem(
    val product: Product,
    val cartEntity: CartEntity
)