package com.theapache64.nemo.feature.home

import androidx.test.core.app.ActivityScenario
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.theapache64.nemo.R
import com.theapache64.nemo.utils.test.IdlingRule
import com.theapache64.nemo.utils.test.monitorActivity
import org.junit.Rule
import org.junit.Test

/**
 * Created by theapache64 : Nov 22 Sun,2020 @ 10:30
 */
class HomeActivityTest {

    @get:Rule
    val idlingRule = IdlingRule()

    @Test
    fun isBannerWorks() {
        val userActivity = ActivityScenario.launch(HomeActivity::class.java)
        idlingRule.dataBindingIdlingResource.monitorActivity(userActivity)
        assertDisplayed(R.id.bvp_home)
    }
}