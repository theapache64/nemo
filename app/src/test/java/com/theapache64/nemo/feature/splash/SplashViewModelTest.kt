package com.theapache64.nemo.feature.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.theapache64.expekt.should
import com.theapache64.nemo.data.repository.AnalyticsRepo
import com.theapache64.nemo.data.repository.ConfigRepo
import com.theapache64.topcorn.utils.test.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
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
    fun `On splash, If config success, app should go to home`() {

        // Prepare success data
        `when`(fakeConfigRepo.getRemoteConfig()).thenReturn(fakeSuccessConfigFlow)
        splashViewModel.init()

        val actualResult = splashViewModel.shouldGoToHome.value
        actualResult.should.`true`
    }

    @Test
    fun `On splash, if config error, app should not go to home and should show sync error`() {


        // Prepare fake error data
        `when`(fakeConfigRepo.getRemoteConfig()).thenReturn(fakeErrorConfigFlow)
        splashViewModel.init()

        // Not moved
        val actualResult = splashViewModel.shouldGoToHome.value
        actualResult.should.`null`

        // Dialog shown
        splashViewModel.shouldShowConfigSyncError.value.should.`true`
    }
}