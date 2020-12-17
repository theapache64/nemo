package com.theapache64.nemo

import com.theapache64.nemo.data.remote.Category
import com.theapache64.nemo.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by theapache64 : Oct 24 Sat,2020 @ 17:52
 */
object FakeCategoryDataStore {

    const val FAKE_CATEGORIES_COUNT = 10

    val categoriesSuccessFlow = getCategories(FAKE_CATEGORIES_COUNT)
    val categoriesEmptySuccessFlow = getCategories(0)

    private fun getCategories(count: Int): Flow<Resource<List<Category>>> {
        return flow<Resource<List<Category>>> {
            // Loading
            emit(Resource.Loading())


            val fakeCategories = mutableListOf<Category>().apply {
                repeat(count) {
                    add(
                        Category(
                            it,
                            "Category $it",
                            "https://picsum.photos/id/1%${it}/300/300",
                            it * 10
                        )
                    )
                }
            }
            // then success
            emit(Resource.Success(null, fakeCategories))
        }
    }

    val categoriesErrorFlow = flow<Resource<List<Category>>> {
        emit(Resource.Loading())
        emit(Resource.Error("This is some fake banner error"))
    }

}






