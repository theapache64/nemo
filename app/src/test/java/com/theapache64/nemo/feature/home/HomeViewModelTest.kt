package com.theapache64.nemo.feature.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.theapache64.expekt.should
import com.theapache64.nemo.bannerSuccessFlow
import com.theapache64.nemo.data.repository.BannersRepo
import com.theapache64.nemo.utils.test.IdlingRule
import com.theapache64.nemo.utils.test.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

/**
 * Created by theapache64 : Nov 23 Mon,2020 @ 23:19
 */
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()


    @Test
    fun `Click on banner launch product`() {
        val bannersRepo: BannersRepo = mock()
        whenever(bannersRepo.getBanners()).thenReturn(bannerSuccessFlow)

        val homeViewModel = HomeViewModel(
            bannersRepo,
            mock(),
            mock()
        )
        homeViewModel.onBannerClicked(0)
        homeViewModel.shouldLaunchCategory.value?.id.should.equal(0)
    }

}