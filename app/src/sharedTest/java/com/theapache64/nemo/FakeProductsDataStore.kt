package com.theapache64.nemo

import com.theapache64.nemo.data.remote.Product
import com.theapache64.nemo.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by theapache64 : Dec 06 Sun,2020 @ 18:51
 */
object FakeProductsDataStore {

    private const val PAGE_1_PRODUCTS_COUNT = 10

    val productsSuccessFlow by lazy {
        getProducts(PAGE_1_PRODUCTS_COUNT)
    }
    const val PRODUCTS_ERROR_MESSAGE = "This is some error data"


    fun getProducts(count: Int): Flow<Resource<List<Product>>> {
        return flow<Resource<List<Product>>> {
            val fakeProducts = mutableListOf<Product>().apply {

                val moreDetails = """
                        Color : Black
                        Size : Large
                        Total Reviews : 10,200
                    """.trimIndent()

                repeat(count) { repIndex ->
                    val isEven = repIndex % 2 == 0
                    add(
                        Product(
                            repIndex,
                            "Product $repIndex",
                            moreDetails.takeIf { isEven },
                            3,
                            repIndex * 100,
                            "https://picsum.photos/id/1%${repIndex}/300/300",
                            "https://picsum.photos/id/1%${repIndex}/300/300",
                            repIndex * 3
                        )
                    )
                }
            }

            emit(Resource.Loading())
            emit(Resource.Success(null, fakeProducts))
        }
    }

    val productsErrorFlow = flow<Resource<List<Product>>> {
        emit(Resource.Loading())
        emit(Resource.Error(PRODUCTS_ERROR_MESSAGE))
    }
}