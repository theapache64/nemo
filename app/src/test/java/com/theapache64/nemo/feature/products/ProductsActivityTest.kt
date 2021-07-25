package com.theapache64.nemo.feature.products

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.whenever
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition
import com.theapache64.nemo.FakeProductsDataStore
import com.theapache64.nemo.R
import com.theapache64.nemo.data.remote.Category
import com.theapache64.nemo.data.remote.Config
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.data.repository.ConfigRepo
import com.theapache64.nemo.di.module.ApiModule
import com.theapache64.nemo.utils.test.IdlingRule
import com.theapache64.nemo.utils.test.MainCoroutineRule
import com.theapache64.nemo.utils.test.monitorActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
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

    companion object {
        private const val PRODUCTS_PER_PAGE = 10
        private const val TOTAL_PRODUCTS_IN_CATEGORY = 100

        val category =
            Category(
                1,
                "Category 1",
                "https://picsum.photos/id/1/300/300",
                TOTAL_PRODUCTS_IN_CATEGORY
            )

    }


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


    @Before
    fun init() {

        whenever(configRepo.getLocalConfig()).thenReturn(
            Config(
                totalProducts = 1000,
                productsPerPage = PRODUCTS_PER_PAGE,
                currency = "$",
                deliveryCharge = 10,
                totalPages = 10
            )
        )
    }

    @Test
    fun givenProducts_whenGoodProducts_thenProductsShownAndPaginationWorks() {


        // First page
        whenever(fakeNemoApi.getProducts(PRODUCTS_PER_PAGE, 0, category.categoryName))
            .thenReturn(FakeProductsDataStore.productsSuccessFlow)

        // Second page
        whenever(
            fakeNemoApi.getProducts(
                PRODUCTS_PER_PAGE,
                PRODUCTS_PER_PAGE,
                category.categoryName
            )
        )
            .thenReturn(FakeProductsDataStore.productsSuccessFlow)

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = ProductsActivity.getStartIntent(context, category)
        ActivityScenario.launch<ProductsActivity>(intent).run {
            idlingRule.dataBindingIdlingResource.monitorActivity(this)

            assertDisplayed(R.id.rv_products)
            assertRecyclerViewItemCount(R.id.rv_products, PRODUCTS_PER_PAGE)
            assertDisplayed("${category.totalProducts} items available")

            // Checking if pagination works

            // Scroll to last item
            scrollListToPosition(R.id.rv_products, PRODUCTS_PER_PAGE - 1)

            // Checking count
            assertRecyclerViewItemCount(R.id.rv_products, PRODUCTS_PER_PAGE * 2)

            // Checking loading not showing
            assertNotDisplayed(R.id.lv_products)
        }
    }

    // Error products shows error
    @Test
    fun givenProducts_whenBadProducts_thenErrorShown() {

        // Setting mock data
        whenever(fakeNemoApi.getProducts(PRODUCTS_PER_PAGE, 0, category.categoryName))
            .thenReturn(FakeProductsDataStore.productsErrorFlow)

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = ProductsActivity.getStartIntent(context, category)
        ActivityScenario.launch<ProductsActivity>(intent).run {
            idlingRule.dataBindingIdlingResource.monitorActivity(this)

            // Check error displayed
            assertDisplayed(FakeProductsDataStore.PRODUCTS_ERROR_MESSAGE)
            assertNotDisplayed(R.id.rv_products)
        }
    }
}