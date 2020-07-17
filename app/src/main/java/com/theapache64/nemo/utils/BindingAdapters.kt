package com.theapache64.nemo.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * Created by theapache64 : Jul 18 Sat,2020 @ 01:21
 * Copyright (c) 2020
 * All rights reserved
 */
@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, imageUrl: String) {
    Glide.with(imageView.context)
        .load(imageUrl)
        .into(imageView)
}