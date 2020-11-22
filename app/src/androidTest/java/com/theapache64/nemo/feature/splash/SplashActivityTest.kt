package com.theapache64.nemo.feature.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.theapache64.nemo.R
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.di.module.ApiModule
import com.theapache64.nemo.configErrorFlow
import com.theapache64.nemo.configSuccessFlow
import com.theapache64.nemo.feature.home.HomeActivity
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
 * Created by theapache64 : Oct 25 Sun,2020 @ 14:05
 */
@UninstallModules(ApiModule::class)
@HiltAndroidTest
class SplashActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val idlingRule = IdlingRule()

    @BindValue
    @JvmField
    val fakeNemoApi: NemoApi = mock(NemoApi::class.java)

    @Test
    fun givenSplash_whenGoodConfig_thenHome() {
        `when`(fakeNemoApi.getConfig()).thenReturn(configSuccessFlow)

        // Fix for home to not to crash
        `when`(fakeNemoApi.getBanners()).thenReturn(flowOf())
        `when`(fakeNemoApi.getCategories()).thenReturn(flowOf())

        Intents.init()
        val splashActivity = ActivityScenario.launch(SplashActivity::class.java)
        intended(hasComponent(HomeActivity::class.java.name))
        Intents.release()
        splashActivity.close()
    }

    @Test
    fun givenSplash_whenBadConfig_thenConfigSyncError() {
        `when`(fakeNemoApi.getConfig()).thenReturn(configErrorFlow)
        val splashActivity = ActivityScenario.launch(SplashActivity::class.java)
        idlingRule.dataBindingIdlingResource.monitorActivity(splashActivity)
        assertDisplayed(R.string.splash_sync_error_title)
        splashActivity.close()
    }

}