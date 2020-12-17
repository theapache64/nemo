package com.theapache64.nemo

import com.theapache64.nemo.data.remote.Config
import com.theapache64.nemo.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.flow

object FakeConfigDataStore {
    /**
     * Created by theapache64 : Oct 24 Sat,2020 @ 17:52
     */
    val configSuccessFlow = flow<Resource<Config>> {

        /* Fake Config*/
        val fakeConfig = Config(
            100,
            10,
            "$",
            1,
            10
        )

        emit(Resource.Loading())
        emit(Resource.Success(null, fakeConfig))
    }

    val configErrorFlow = flow<Resource<Config>> {
        emit(Resource.Loading())
        emit(Resource.Error("This is some fake error"))
    }
}







