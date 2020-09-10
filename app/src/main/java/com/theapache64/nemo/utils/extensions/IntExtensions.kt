package com.theapache64.nemo.utils.extensions

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 18:53
 */
fun Int.get(singular: String, plural: String = singular + "s"): String {
    return if (this > 1) {
        plural
    } else {
        singular
    }
}