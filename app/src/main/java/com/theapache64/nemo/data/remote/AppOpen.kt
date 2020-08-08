package com.theapache64.nemo.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * Created by theapache64 : Aug 08 Sat,2020 @ 21:25
 */
@JsonClass(generateAdapter = true)
data class AppOpen(
    @Json(name = "device_name")
    val deviceName: String
)