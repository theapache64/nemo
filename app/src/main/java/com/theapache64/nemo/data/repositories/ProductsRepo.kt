package com.theapache64.nemo.data.repositories

import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.data.remote.Product
import com.theapache64.nemo.utils.flow.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by theapache64 : Jul 18 Sat,2020 @ 00:23
 * Copyright (c) 2020
 * All rights reserved
 */
@Singleton
class ProductsRepo @Inject constructor(
    private val nemoApi: NemoApi,
    private val configRepo: ConfigRepo
) {
    /**
     * To get products
     */
    fun getProducts(page: Int): Flow<Resource<List<Product>>> {
        val config = configRepo.getLocalConfig()!!
        val offset = (page - 1) * config.productsPerPage
        return nemoApi.getProducts(
            config.productsPerPage,
            offset
        )
    }



    fun getProduct(productId: Int) = nemoApi.getProduct(productId)
}