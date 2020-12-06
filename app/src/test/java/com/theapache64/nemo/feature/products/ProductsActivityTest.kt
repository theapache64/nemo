package com.theapache64.nemo.feature.products

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.di.module.ApiModule
import com.theapache64.nemo.utils.test.IdlingRule
import com.theapache64.nemo.utils.test.MainCoroutineRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

/**
 * Created by theapache64 : Dec 05 Sat,2020 @ 20:33
 */
@UninstallModules(ApiModule::class)
@HiltAndroidTest
@LargeTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class ProductsActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val fakeNemoApi: NemoApi = Mockito.mock(NemoApi::class.java)

    @get:Rule
    val idlingRule = IdlingRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun givenProducts_whenGoodProducts_thenProductsShown() {
        assert(true)
    }
    // Error products shows error
    // Empty products shows empty
    // Scroll down and pagination should work
}