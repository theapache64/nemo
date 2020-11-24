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

private fun getBanners(count: Int): Flow<Resource<List<Banner>>> {
    return flow<Resource<List<Banner>>> {
        // Loading
        emit(Resource.Loading())


        val fakeBanners = mutableListOf<Banner>().apply {
            repeat(count) {
                add(
                    Banner(
                        it,
                        "https://picsum.photos/id/1%${it}/300/300",
                        it,
                        it,
                        "Product $it"
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







