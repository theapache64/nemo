package com.theapache64.nemo

import com.theapache64.nemo.data.remote.Product
import com.theapache64.nemo.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.flow

/**
 * Created by theapache64 : Dec 06 Sun,2020 @ 18:51
 */
object FakeProductDataStore {

    val productSuccessFlow = getProduct(1)
    val productSuccessFlow2 = getProduct(2)

    private fun getProduct(id: Int) = flow {

        val fakeProduct = Product(
            id,
            "Product $id",
            """Color : Black
           Size : Large
           Total Reviews : 10,200""".trimIndent(),
            3,
            1000,
            "https://picsum.photos/id/1/300/300",
            "https://picsum.photos/id/1/300/300",
            3
        )


        emit(Resource.Loading())
        emit(Resource.Success(null, fakeProduct))
    }
}