package com.theapache64.nemo.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 14:22
 */
class UserRepo @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_CART = "cart"
    }

    /**
     * To get cart's product ids
     *
     * @return Set<Int>
     */
    fun getCart(): Set<Int> {
        return sharedPreferences.getString(KEY_CART, "")
            ?.split(",")
            ?.filter { it.isNotEmpty() }
            ?.map {
                it.toInt()
            }
            ?.toSet() ?: setOf()
    }


    /**
     * To add given product to cart
     *
     * @param productId
     */
    fun addToCart(productId: Int) {
        val cart = getCart().toMutableSet()
        cart.add(productId)
        sharedPreferences.edit {
            putString(KEY_CART, cart.joinToString(","))
        }
    }
}