package com.theapache64.nemo.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 07:53
 */
@JsonClass(generateAdapter = true)
data class Banner(
    @Json(name = "id")
    val id: Int, // 1
    @Json(name = "image_url")
    val imageUrl: String,
    @Json(name = "product_id")
    val productId: Int, // 1
    @Json(name = "product_name")
    val productName: String
)