package com.theapache64.nemo.feature.products

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.whenever
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.theapache64.nemo.R
import com.theapache64.nemo.data.remote.Category
import com.theapache64.nemo.data.remote.Config
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.data.repository.ConfigRepo
import com.theapache64.nemo.di.module.ApiModule
import com.theapache64.nemo.productsSuccessFlow
import com.theapache64.nemo.utils.test.IdlingRule
import com.theapache64.nemo.utils.test.MainCoroutineRule
import com.theapache64.nemo.utils.test.monitorActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

/**
 * Created by theapache64 : Dec 05 Sat,2020 @ 20:33
 */
@UninstallModules(ApiModule::class)
@HiltAndroidTest
@LargeTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class ProductsActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val fakeNemoApi: NemoApi = Mockito.mock(NemoApi::class.java)

    @BindValue
    @JvmField
    val configRepo: ConfigRepo = Mockito.mock(ConfigRepo::class.java)

    @get:Rule
    val idlingRule = IdlingRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun givenProducts_whenGoodProducts_thenProductsShown() {

        val productsPerPage = 10

        whenever(configRepo.getLocalConfig()).thenReturn(
            Config(
                totalProducts = 1000,
                productsPerPage = productsPerPage,
                currency = "$",
                deliveryCharge = 10,
                totalPages = 10
            )
        )

        whenever(fakeNemoApi.getProducts(productsPerPage, 0, "Category 1"))
            .thenReturn(productsSuccessFlow)

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val totalProducts = 100
        val category =
            Category(1, "Category 1", "https://picsum.photos/id/1/300/300", totalProducts)
        val intent = ProductsActivity.getStartIntent(context, category)
        ActivityScenario.launch<ProductsActivity>(intent).run {
            idlingRule.dataBindingIdlingResource.monitorActivity(this)

            assertDisplayed(R.id.rv_products)
            assertRecyclerViewItemCount(R.id.rv_products, 10)
            assertDisplayed("$totalProducts items available")
        }
    }
    // Error products shows error
    // Empty products shows empty
    // Scroll down and pagination should work
}