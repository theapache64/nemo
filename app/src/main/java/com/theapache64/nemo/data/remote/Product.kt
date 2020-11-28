package com.theapache64.nemo.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * Created by theapache64 : Jul 17 Fri,2020 @ 21:26
 * Copyright (c) 2020
 * All rights reserved
 */
@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "id")
    val id: Int, // 1
    @Json(name = "title")
    val title: String, // Guppy
    @Json(name = "more_details")
    val moreDetailsString: String?,
    @Json(name = "rating")
    val rating: Int,
    @Json(name = "price")
    val price: Int,
    @Json(name = "thumb_url")
    val thumbUrl: String,
    @Json(name = "image_url")
    val imageUrl: String,
    @Json(name = "no_of_reviews")
    val noOfReviews: Int
) {
    val moreDetails = mutableListOf<Detail>()

    init {
        // Parsing more details
        if (!moreDetailsString.isNullOrBlank()) {
            val lines = moreDetailsString.split("\n")
            for (line in lines) {
                val s1 = line.split(":")
                if (s1.size == 2) {
                    val key = s1[0]
                    val value = s1[1]
                    moreDetails.add(Detail(key, value))
                }
            }
        }
    }

    class Detail(
        val key: String,
        val value: String
    )
}