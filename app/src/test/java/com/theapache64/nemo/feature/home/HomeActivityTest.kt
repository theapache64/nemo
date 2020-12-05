package com.theapache64.nemo.feature.home

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import com.theapache64.nemo.*
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.di.module.ApiModule
import com.theapache64.nemo.feature.products.ProductsActivity
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
 * Created by theapache64 : Nov 22 Sun,2020 @ 10:30
 */
@UninstallModules(ApiModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @get:Rule
    val idlingRule = IdlingRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val nemoApi: NemoApi = mock()

    @Test
    fun givenBanners_whenGoodBanners_thenBannerDisplayed() {

        // Fake nemo api
        whenever(nemoApi.getBanners()).thenReturn(bannerSuccessFlow)
        whenever(nemoApi.getCategories()).thenReturn(flowOf())

        val homeActivity = ActivityScenario.launch(HomeActivity::class.java)
        idlingRule.dataBindingIdlingResource.monitorActivity(homeActivity)

        assertDisplayed(R.id.bvp_home_banner)
    }

    @Test
    fun givenBanners_whenEmptyBanners_thenBannerNotDisplayed() {

        // Fake nemo api
        whenever(nemoApi.getBanners()).thenReturn(bannerEmptySuccessFlow)
        whenever(nemoApi.getCategories()).thenReturn(flowOf())

        val homeActivity = ActivityScenario.launch(HomeActivity::class.java)
        idlingRule.dataBindingIdlingResource.monitorActivity(homeActivity)

        assertNotDisplayed(R.id.bvp_home_banner)
    }

    @Test
    fun givenBanners_whenClicked_thenProductDetailLaunched() {
        // Fake nemo api
        whenever(nemoApi.getBanners()).thenReturn(bannerSuccessFlow)
        whenever(nemoApi.getCategories()).thenReturn(categoriesSuccessFlow)
        whenever(nemoApi.getProducts(any(), any(), any())).thenReturn(flowOf())

        val homeActivity = ActivityScenario.launch(HomeActivity::class.java)
        idlingRule.dataBindingIdlingResource.monitorActivity(homeActivity)

        Intents.init()
        clickOn(R.id.bvp_home_banner)
        intended(hasComponent(ProductsActivity::class.java.name))
        intended(hasExtraWithKey(ProductsActivity.KEY_CATEGORY))
        Intents.release()
    }

    @Test
    fun givenCategories_whenGoodCategories_thenCategoriesDisplayed() {

        // Fake nemo api
        whenever(nemoApi.getBanners()).thenReturn(bannerSuccessFlow)
        whenever(nemoApi.getCategories()).thenReturn(categoriesSuccessFlow)

        val homeActivity = ActivityScenario.launch(HomeActivity::class.java)
        idlingRule.dataBindingIdlingResource.monitorActivity(homeActivity)

        assertDisplayed(R.id.rv_categories)
        assertDisplayed(R.id.tv_label_categories)
    }

    @Test
    fun givenCategories_whenBadCategories_thenBothCategoriesAndBannerNotDisplayed() {

        // Fake nemo api
        whenever(nemoApi.getBanners()).thenReturn(bannerSuccessFlow)
        whenever(nemoApi.getCategories()).thenReturn(categoriesErrorFlow)

        val homeActivity = ActivityScenario.launch(HomeActivity::class.java)
        idlingRule.dataBindingIdlingResource.monitorActivity(homeActivity)

        assertNotDisplayed(R.id.rv_categories)
        assertNotDisplayed(R.id.tv_label_categories)
        assertNotDisplayed(R.id.bvp_home_banner)
    }
}