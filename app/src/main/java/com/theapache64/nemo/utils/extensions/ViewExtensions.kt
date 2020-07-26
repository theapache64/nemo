package com.theapache64.nemo.utils.extensions

import android.view.View

/**
 * Created by theapache64 : Jul 26 Sun,2020 @ 21:23
 */
fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}