package com.theapache64.nemo.data.repositories

import com.theapache64.nemo.data.remote.NemoApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by theapache64 : Jul 18 Sat,2020 @ 00:23
 * Copyright (c) 2020
 * All rights reserved
 */
@Singleton
class ProductsRepository @Inject constructor(
    private val nemoApi: NemoApi
) {
     fun getProducts() = nemoApi.getProducts()
}