package com.theapache64.nemo.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * Created by theapache64 : Sep 08 Tue,2020 @ 18:55
 */
@JsonClass(generateAdapter = true)
data class Category(
    @Json(name = "id")
    val id: Int, // 1
    @Json(name = "category_name")
    val categoryName: String, // Category 1
    @Json(name = "image_url")
    val imageUrl: String
)