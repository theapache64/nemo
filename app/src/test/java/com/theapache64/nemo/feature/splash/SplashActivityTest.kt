package com.theapache64.nemo.feature.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nhaarman.mockitokotlin2.whenever
import com.theapache64.nemo.FakeConfigDataStore
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.di.module.ApiModule
import com.theapache64.nemo.feature.home.HomeActivity
import com.theapache64.nemo.utils.test.IdlingRule
import com.theapache64.nemo.utils.test.MainCoroutineRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

/**
 * Created by theapache64 : Oct 25 Sun,2020 @ 14:05
 */
@UninstallModules(ApiModule::class)
@HiltAndroidTest
@LargeTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class SplashActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val fakeNemoApi: NemoApi = mock(NemoApi::class.java)

    @get:Rule
    val idlingRule = IdlingRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun givenSplash_whenGoodConfig_thenHome() {
        whenever(fakeNemoApi.getConfig()).thenReturn(FakeConfigDataStore.configSuccessFlow)

        // Fix for home to not to crash
        whenever(fakeNemoApi.getBanners()).thenReturn(flowOf())
        whenever(fakeNemoApi.getCategories()).thenReturn(flowOf())

        Intents.init()
        val splashActivity = ActivityScenario.launch(SplashActivity::class.java)
        intended(hasComponent(HomeActivity::class.java.name))
        Intents.release()
        splashActivity.close()
    }

}