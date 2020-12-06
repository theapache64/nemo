package com.theapache64.nemo.feature.home

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.schibsted.spain.barista.interaction.BaristaClickInteractions
import com.theapache64.nemo.R
import com.theapache64.nemo.bannerSuccessFlow
import com.theapache64.nemo.categoriesSuccessFlow
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

/**
 * Created by theapache64 : Dec 06 Sun,2020 @ 19:49
 */
@UninstallModules(ApiModule::class)
@HiltAndroidTest
class HomeActivityTestInst {

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
    fun   givenBanners_whenClicked_thenProductsLaunched() {
        // Fake nemo api
        whenever(nemoApi.getBanners()).thenReturn(bannerSuccessFlow)
        whenever(nemoApi.getCategories()).thenReturn(categoriesSuccessFlow)
        whenever(nemoApi.getProducts(any(), any(), any())).thenReturn(flowOf())

        val homeActivity = ActivityScenario.launch(HomeActivity::class.java)
        idlingRule.dataBindingIdlingResource.monitorActivity(homeActivity)

        Intents.init()
        BaristaClickInteractions.clickOn(R.id.bvp_home_banner)
        Intents.intended(IntentMatchers.hasComponent(ProductsActivity::class.java.name))
        Intents.intended(IntentMatchers.hasExtraWithKey(ProductsActivity.KEY_CATEGORY))
        Intents.release()
    }
}