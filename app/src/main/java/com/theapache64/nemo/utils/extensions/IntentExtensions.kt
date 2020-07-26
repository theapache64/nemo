package com.theapache64.nemo.utils.extensions

import android.content.Intent

/**
 * Created by theapache64 : Jul 27 Mon,2020 @ 00:26
 */
fun Intent.getIntExtraOrThrow(key: String): Int {
    val value = getIntExtra(key, -1)
    require(value != -1) { "'${key}' missing in intent" }
    return value
}