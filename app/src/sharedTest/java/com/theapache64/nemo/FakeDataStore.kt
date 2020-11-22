package com.theapache64.nemo

import com.theapache64.nemo.data.remote.Config
import com.theapache64.nemo.utils.calladapter.flow.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

/**
 * Created by theapache64 : Oct 24 Sat,2020 @ 17:52
 */
val fakeConfig = Config(
    100,
    10,
    "$",
    1,
    10
)

val fakeSuccessConfigFlow = flow<Resource<Config>> {
    emit(Resource.Loading())
    emit(Resource.Success(null, fakeConfig))
}

val fakeErrorConfigFlow = flow<Resource<Config>> {
    emit(Resource.Loading())
    delay(2000)
    emit(Resource.Error("This is some fake error"))
}