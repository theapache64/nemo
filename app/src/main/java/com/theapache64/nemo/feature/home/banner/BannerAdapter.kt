package com.theapache64.nemo.feature.home.banner

import android.view.View
import com.theapache64.nemo.R
import com.theapache64.nemo.data.remote.Banner
import com.zhpan.bannerview.BaseBannerAdapter

/**
 * Created by theapache64 : Sep 09 Wed,2020 @ 08:23
 */
class BannerAdapter : BaseBannerAdapter<Banner, BannerViewHolder>() {

    override fun createViewHolder(itemView: View, viewType: Int): BannerViewHolder {
        return BannerViewHolder(itemView)
    }

    override fun onBind(holder: BannerViewHolder, data: Banner, position: Int, pageSize: Int) {
        holder.bindData(data, position, pageSize)
    }

    override fun getLayoutId(viewType: Int): Int = R.layout.item_home_banner
}