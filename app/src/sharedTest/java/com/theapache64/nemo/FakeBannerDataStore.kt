package com.theapache64.nemo

import com.theapache64.nemo.data.remote.Banner
import com.theapache64.nemo.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by theapache64 : Oct 24 Sat,2020 @ 17:52
 */

const val FAKE_BANNER_COUNT = 10

val bannerSuccessFlow = getBanners(FAKE_BANNER_COUNT)
val bannerEmptySuccessFlow = getBanners(0)

const val BANNER_ITEM_PRODUCT_POSITION = 2
const val BANNER_ITEM_CATEGORY_POSITION = 4

private fun getBanners(count: Int): Flow<Resource<List<Banner>>> {
    return flow<Resource<List<Banner>>> {
        // Loading
        emit(Resource.Loading())


        val fakeBanners = mutableListOf<Banner>().apply {
            repeat(count) {

                val productId = if (it == BANNER_ITEM_PRODUCT_POSITION) {
                    it
                } else {
                    null
                }

                val categoryId = if (it == BANNER_ITEM_CATEGORY_POSITION) {
                    it
                } else {
                    null
                }

                add(
                    Banner(
                        id = it,
                        imageUrl = "https://picsum.photos/id/1%${it}/300/300",
                        productId = productId,
                        categoryId = categoryId,
                        productName = "Product $it"
                    )
                )
            }
        }
        // then success
        emit(Resource.Success(null, fakeBanners))
    }
}

val bannerErrorFlow = flow<Resource<List<Banner>>> {
    emit(Resource.Loading())
    emit(Resource.Error("This is some fake banner error"))
}







