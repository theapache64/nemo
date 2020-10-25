package com.theapache64.nemo.feature.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.theapache64.nemo.R
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by theapache64 : Oct 25 Sun,2020 @ 14:05
 */
@RunWith(AndroidJUnit4::class)
class SplashActivityTest {
    @Test
    fun isSyncConfigProgressVisible() {
        val splashActivity = ActivityScenario.launch(SplashActivity::class.java)
        assertDisplayed(R.id.tv_app_name)
        assertDisplayed(R.id.pb_config_sync)
        splashActivity.close()
    }
}