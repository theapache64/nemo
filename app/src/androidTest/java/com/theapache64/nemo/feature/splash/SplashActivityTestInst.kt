package com.theapache64.nemo.feature.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.whenever
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import com.schibsted.spain.barista.interaction.BaristaSwipeRefreshInteractions.refresh
import com.theapache64.nemo.R
import com.theapache64.nemo.configErrorFlow
import com.theapache64.nemo.data.local.table.cart.CartEntity
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.data.repository.CartRepo
import com.theapache64.nemo.di.module.ApiModule
import com.theapache64.nemo.feature.productdetail.ProductDetailActivity
import com.theapache64.nemo.productSuccessFlow
import com.theapache64.nemo.productSuccessFlow2
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
import org.mockito.Mockito
import org.mockito.Mockito.`when`

/**
 * Created by theapache64 : Oct 25 Sun,2020 @ 14:05
 */
@UninstallModules(ApiModule::class)
@HiltAndroidTest
@LargeTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class SplashActivityTestInst {

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
    fun givenSplash_whenBadConfig_thenConfigSyncError() {
        whenever(fakeNemoApi.getConfig()).thenReturn(configErrorFlow)
        val splashActivity = ActivityScenario.launch(SplashActivity::class.java)
        idlingRule.dataBindingIdlingResource.monitorActivity(splashActivity)
        assertDisplayed(R.string.splash_sync_error_title)
        splashActivity.close()
    }

    @Test
    fun givenProductDetailPage_whenSwipeDown_thenPageRefreshed() {
        val productId = 1

        whenever(fakeNemoApi.getProduct(productId))
            .thenReturn(productSuccessFlow)


        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ActivityScenario.launch<ProductDetailActivity>(
            ProductDetailActivity.getStartIntent(
                context,
                productId
            )
        ).run {
            idlingRule.dataBindingIdlingResource.monitorActivity(this)
            sleep(5000)
            assertDisplayed("Product 1")

            // Now change data
            whenever(fakeNemoApi.getProduct(productId))
                .thenReturn(productSuccessFlow2)
            refresh(R.id.csrl_product)
            assertDisplayed("Product 2")
        }
    }
}