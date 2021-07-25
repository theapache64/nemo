package com.theapache64.nemo.feature.productdetail

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions
import com.theapache64.nemo.FakeProductDataStore
import com.theapache64.nemo.data.local.table.cart.CartDao
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.di.module.ApiModule
import com.theapache64.nemo.di.module.CartModule
import com.theapache64.nemo.utils.test.IdlingRule
import com.theapache64.nemo.utils.test.MainCoroutineRule
import com.theapache64.nemo.utils.test.monitorActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by theapache64 : Dec 18 Fri,2020 @ 01:31
 */
@UninstallModules(ApiModule::class, CartModule::class)
@HiltAndroidTest
@LargeTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class ProductDetailActivityInsTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val fakeNemoApi: NemoApi = mock()

    @BindValue
    @JvmField
    val cartDao: CartDao = mock()

    @get:Rule
    val idlingRule = IdlingRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun givenProductDetailPage_whenSwipeDown_thenPageRefreshed() {
        val productId = 1

        whenever(fakeNemoApi.getProduct(productId))
            .thenReturn(FakeProductDataStore.productSuccessFlow)

        whenever(cartDao.getCartProductsFlow()).thenReturn(flowOf(listOf()))

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ActivityScenario.launch<ProductDetailActivity>(
            ProductDetailActivity.getStartIntent(
                context,
                productId
            )
        ).run {
            idlingRule.dataBindingIdlingResource.monitorActivity(this)
            BaristaVisibilityAssertions.assertDisplayed("Product 1")

            // Now change data
            whenever(fakeNemoApi.getProduct(productId))
                .thenReturn(FakeProductDataStore.productSuccessFlow2)
        }
    }
}