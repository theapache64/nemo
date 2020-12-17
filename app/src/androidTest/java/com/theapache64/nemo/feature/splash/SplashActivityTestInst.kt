package com.theapache64.nemo.feature.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import com.schibsted.spain.barista.interaction.BaristaSwipeRefreshInteractions.refresh
import com.theapache64.nemo.FakeConfigDataStore
import com.theapache64.nemo.FakeProductDataStore
import com.theapache64.nemo.R
import com.theapache64.nemo.data.local.table.cart.CartDao
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.di.module.ApiModule
import com.theapache64.nemo.di.module.CartModule
import com.theapache64.nemo.feature.productdetail.ProductDetailActivity
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
 * Created by theapache64 : Oct 25 Sun,2020 @ 14:05
 */
@UninstallModules(ApiModule::class, CartModule::class)
@HiltAndroidTest
@LargeTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class SplashActivityTestInst {

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
    fun givenSplash_whenBadConfig_thenConfigSyncError() {
        whenever(fakeNemoApi.getConfig()).thenReturn(FakeConfigDataStore.configErrorFlow)
        val splashActivity = ActivityScenario.launch(SplashActivity::class.java)
        idlingRule.dataBindingIdlingResource.monitorActivity(splashActivity)
        assertDisplayed(R.string.splash_sync_error_title)
        splashActivity.close()
    }

  
}