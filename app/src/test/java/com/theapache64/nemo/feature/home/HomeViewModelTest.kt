package com.theapache64.nemo.feature.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.theapache64.expekt.should
import com.theapache64.nemo.FakeBannerDataStore
import com.theapache64.nemo.FakeCategoryDataStore
import com.theapache64.nemo.data.repository.BannersRepo
import com.theapache64.nemo.data.repository.CategoriesRepo
import com.theapache64.nemo.utils.test.MainCoroutineRule
import com.theapache64.nemo.utils.test.getOrAwaitValue
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

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun `Click on product banner, launch product details screen`() {
        val bannersRepo: BannersRepo = mock()
        whenever(bannersRepo.getBanners()).thenReturn(FakeBannerDataStore.bannerSuccessFlow)

        val homeViewModel = HomeViewModel(
            bannersRepo,
            mock(),
            mock()
        )
        homeViewModel.banners.getOrAwaitValue {
            // clicking 5th item
            homeViewModel.onBannerClicked(FakeBannerDataStore.BANNER_ITEM_PRODUCT_POSITION)
            // checking launch for 5th item has fired
            homeViewModel.shouldLaunchProduct.value.should
                .equal(FakeBannerDataStore.BANNER_ITEM_PRODUCT_POSITION)
        }
    }

    @Test
    fun `Click on category banner, launch product list`() {
        val categoriesRepo: CategoriesRepo = mock()
        whenever(categoriesRepo.getCategories()).thenReturn(FakeCategoryDataStore.categoriesSuccessFlow)

        val homeViewModel = HomeViewModel(
            mock(),
            categoriesRepo,
            mock()
        )
        homeViewModel.categories.getOrAwaitValue {
            // clicking 5th item
            homeViewModel.onCategoryClicked(FakeBannerDataStore.BANNER_ITEM_CATEGORY_POSITION)
            // checking launch for 5th item has fired
            homeViewModel.shouldLaunchCategory.value!!.id.should
                .equal(FakeBannerDataStore.BANNER_ITEM_CATEGORY_POSITION)
        }
    }
}