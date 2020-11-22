package com.theapache64.nemo.feature.home

import androidx.test.core.app.ActivityScenario
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.theapache64.nemo.R
import com.theapache64.nemo.bannerEmptySuccessFlow
import com.theapache64.nemo.bannerSuccessFlow
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.di.module.ApiModule
import com.theapache64.nemo.utils.test.IdlingRule
import com.theapache64.nemo.utils.test.monitorActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Created by theapache64 : Nov 22 Sun,2020 @ 10:30
 */
@UninstallModules(ApiModule::class)
@HiltAndroidTest
class HomeActivityTest {

    @get:Rule
    val idlingRule = IdlingRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val nemoApi: NemoApi = mock(NemoApi::class.java)

    @Test
    fun givenBanners_whenGoodBanners_thenBannerDisplayed() {

        // Fake nemo api
        `when`(nemoApi.getBanners()).thenReturn(bannerSuccessFlow)
        `when`(nemoApi.getCategories()).thenReturn(flowOf())

        val homeActivity = ActivityScenario.launch(HomeActivity::class.java)
        idlingRule.dataBindingIdlingResource.monitorActivity(homeActivity)

        assertDisplayed(R.id.bvp_home)
    }

    @Test
    fun givenBanners_whenEmptyBanners_thenBannerNotDisplayed() {

        // Fake nemo api
        `when`(nemoApi.getBanners()).thenReturn(bannerEmptySuccessFlow)
        `when`(nemoApi.getCategories()).thenReturn(flowOf())

        val homeActivity = ActivityScenario.launch(HomeActivity::class.java)
        idlingRule.dataBindingIdlingResource.monitorActivity(homeActivity)

        assertNotDisplayed(R.id.bvp_home)
    }
}