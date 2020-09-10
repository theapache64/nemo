package com.theapache64.nemo.data.remote


import com.theapache64.nemo.utils.AppConfig
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

    @GET(AppConfig.SHEET_CONFIG)
    fun getConfig(): Flow<Resource<Config>>


    @Read("SELECT * WHERE category_name = :category_name ORDER BY id DESC LIMIT :products_per_page OFFSET :offset")
    @GET(AppConfig.SHEET_PRODUCTS)
    fun getProducts(
        @Query("products_per_page") productsPerPage: Int,
        @Query("offset") offset: Int,
        @Query("category_name") categoryName : String
    ): Flow<Resource<List<Product>>>

    @Read("SELECT * WHERE id = :productId")
    @GET(AppConfig.SHEET_PRODUCTS)
    fun getProduct(
        @Query("productId") productId: Int
    ): Flow<Resource<Product>>

    @Write
    @POST(AppConfig.FORM_APP_OPEN)
    suspend fun reportAppOpen(@Body appOpen: AppOpen): AppOpen

    @Read("SELECT * WHERE total_products > 0")
    @GET(AppConfig.SHEET_CATEGORIES)
    fun getCategories(): Flow<Resource<List<Category>>>

    @Read("SELECT *")
    @GET(AppConfig.SHEET_BANNERS)
    fun getBanners(): Flow<Resource<List<Banner>>>
}