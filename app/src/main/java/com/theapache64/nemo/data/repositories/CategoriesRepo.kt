package com.theapache64.nemo.data.repositories

import com.theapache64.nemo.data.remote.NemoApi
import javax.inject.Inject

/**
 * Created by theapache64 : Sep 08 Tue,2020 @ 18:59
 */
class CategoriesRepo @Inject constructor(
    private val nemoApi: NemoApi
) {
    fun getCategories() = nemoApi.getCategories()
}