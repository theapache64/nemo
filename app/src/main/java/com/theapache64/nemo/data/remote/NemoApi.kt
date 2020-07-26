package com.theapache64.nemo.data.remote

import com.theapache64.nemo.utils.flow.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

/**
 * Created by theapache64 : Jul 17 Fri,2020 @ 22:33
 * Copyright (c) 2020
 * All rights reserved
 */
interface NemoApi {

    @GET("products")
    fun getProducts(): Flow<Resource<List<Product>>>
}