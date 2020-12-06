package com.theapache64.nemo.feature.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.theapache64.expekt.should
import com.theapache64.nemo.configErrorFlow
import com.theapache64.nemo.configSuccessFlow
import com.theapache64.nemo.data.repository.AnalyticsRepo
import com.theapache64.nemo.data.repository.ConfigRepo
import com.theapache64.nemo.utils.test.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * Created by theapache64 : Oct 24 Sat,2020 @ 15:38
 */
class SplashViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesRule = MainCoroutineRule()

    private lateinit var splashViewModel: SplashViewModel

    private val fakeConfigRepo = mock(ConfigRepo::class.java)
    private val fakeAnalyticsRepo = mock(AnalyticsRepo::class.java)

    @Before
    fun before() {
        splashViewModel = SplashViewModel(
            fakeConfigRepo,
            fakeAnalyticsRepo
        )
    }

    @Test
    fun `In splash, If config success, app should go to home`() {

        // Prepare success data
        whenever(fakeConfigRepo.getRemoteConfig()).thenReturn(configSuccessFlow)
        splashViewModel.init()

        val actualResult = splashViewModel.shouldGoToHome.value
        actualResult.should.`true`
    }

    @Test
    fun `In splash, if config error, app should not go to home and should show sync error`() {


        // Prepare fake error data
        whenever(fakeConfigRepo.getRemoteConfig()).thenReturn(configErrorFlow)
        splashViewModel.init()

        // Not moved
        val actualResult = splashViewModel.shouldGoToHome.value
        actualResult.should.`null`

        // Dialog shown
        splashViewModel.shouldShowConfigSyncError.value.should.`true`
    }
}