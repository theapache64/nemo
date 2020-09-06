package com.theapache64.nemo.data.remote


import com.theapache64.nemo.utils.calladapter.flow.Resource
import com.theapache64.retrosheet.core.Read
import com.theapache64.retrosheet.core.Write
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by theapache64 : Jul 17 Fri,2020 @ 22:33
 * Copyright (c) 2020
 * All rights reserved
 */
interface NemoApi {

    @GET("config")
    fun getConfig(): Flow<Resource<Config>>

    @Read("SELECT * ORDER BY id DESC LIMIT :products_per_page OFFSET :offset")
    @GET("products")
    fun getProducts(
        @Query("products_per_page") productsPerPage: Int,
        @Query("offset") offset: Int
    ): Flow<Resource<List<Product>>>

    @Read("SELECT * WHERE id = :productId")
    @GET("products")
    fun getProduct(
        @Query("productId") productId: Int
    ): Flow<Resource<Product>>

    @Write
    @POST("app_open")
    suspend fun reportAppOpen(@Body appOpen: AppOpen): AppOpen
}