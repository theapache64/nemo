package com.theapache64.nemo.utils.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 02:02
 */

/**
 * FIXME: Temp fix for  -> https://github.com/theapache64/twinkill/issues/21
 */
@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    if (url != null) {
        com.theapache64.twinkill.utils.bindingadapter.loadImage(imageView, url)
    }
}