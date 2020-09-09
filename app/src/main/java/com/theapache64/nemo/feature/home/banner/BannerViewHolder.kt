package com.theapache64.nemo.feature.home.banner

import android.view.View
import android.widget.ImageView
import com.theapache64.nemo.R
import com.theapache64.nemo.data.remote.Banner
import com.theapache64.nemo.utils.extensions.loadImage
import com.zhpan.bannerview.BaseViewHolder

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 08:19
 */
class BannerViewHolder(itemView: View) : BaseViewHolder<Banner>(itemView) {

    override fun bindData(banner: Banner, position: Int, pageSize: Int) {
        val ivBanner = findView<ImageView>(R.id.iv_banner)
        loadImage(
            ivBanner,
            banner.imageUrl
        )
    }
}