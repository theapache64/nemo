package com.theapache64.nemo.data.remote

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


/**
 * Created by theapache64 : Sep 08 Tue,2020 @ 18:55
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class Category(
    @Json(name = "id")
    val id: Int, // 1
    @Json(name = "category_name")
    val categoryName: String, // Category 1
    @Json(name = "image_url")
    val imageUrl: String,
    @Json(name = "total_products")
    val totalProducts: Int
) : Parcelable