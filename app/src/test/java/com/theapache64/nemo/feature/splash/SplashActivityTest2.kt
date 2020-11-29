package com.theapache64.nemo.feature.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * Created by theapache64 : Nov 29 Sun,2020 @ 13:20
 */
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(AndroidJUnit4::class)
class SplashActivityTest2 {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun fooTest() {
        val splashActivity = ActivityScenario.launch(SplashActivity::class.java)
    }
}