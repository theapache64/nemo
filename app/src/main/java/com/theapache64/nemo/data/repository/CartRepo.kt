package com.theapache64.nemo.data.repository

import com.theapache64.nemo.data.local.table.cart.CartDao
import com.theapache64.nemo.data.local.table.cart.CartEntity
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.feature.cart.CartItem
import com.theapache64.nemo.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by theapache64 : Sep 19 Sat,2020 @ 14:22
 */
class CartRepo @Inject constructor(
    private val cartDao: CartDao,
    private val nemoApi: NemoApi
) {
    companion object {
        const val ERROR_CART_EMPTY = 123
        const val ERROR_REMOTE_UPDATED = 234
    }

    /**
     * To get cart's product ids
     *
     * @return Set<Int>
     */
    suspend fun getCartProducts(): List<CartEntity> {
        return cartDao.getCart()
    }


    /**
     * To add given product to cart
     *
     * @param productId
     */
    suspend fun addToCart(productId: Int) {
        cartDao.addToCart(CartEntity(productId, 1))
    }

    fun getCartItems() = flow<Resource<List<CartItem>>> {

        // Loading
        emit(Resource.Loading())

        val cartProducts = getCartProducts()

        if (cartProducts.isNotEmpty()) {

            val ids = cartProducts.map { it.productId }.joinToString("|")
            nemoApi.getProducts("($ids)")
                .collect {
                    when (it) {

                        is Resource.Loading -> {
                            Timber.d("getCartItems: Loading remote cart items")
                        }

                        is Resource.Success -> {
                            val remoteProducts = it.data
                            if (remoteProducts.isEmpty()) {
                                // Remote ids are changed TSH.
                                emit(
                                    Resource.Error(
                                        "Uhh ho!! Cart broken. Please try again",
                                        ERROR_REMOTE_UPDATED
                                    )
                                )
                            } else {
                                // Got products
                                val cartItems = remoteProducts.map { product ->

                                    val cartProduct =
                                        cartProducts.find { cartProduct -> cartProduct.productId == product.id }!!

                                    CartItem(
                                        product,
                                        cartProduct
                                    )
                                }

                                emit(Resource.Success("OK", cartItems))
                            }
                        }

                        is Resource.Error -> {
                            emit(Resource.Error(it.errorData, it.errorCode))
                        }

                    }
                }

        } else {
            emit(Resource.Error("Cart is empty!", ERROR_CART_EMPTY))
        }
    }

    suspend fun update(cartEntity: CartEntity) {
        cartDao.updateCart(cartEntity)
    }

    suspend fun remove(cartEntity: CartEntity) {
        cartDao.remove(cartEntity)
    }

    suspend fun getCartCount(): Int = cartDao.getCount()
}