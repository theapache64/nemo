package com.theapache64.nemo.feature.cart

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.theapache64.nemo.FakeCartDataStore
import com.theapache64.nemo.R
import com.theapache64.nemo.data.local.table.cart.CartDao
import com.theapache64.nemo.data.remote.Config
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.data.repository.ConfigRepo
import com.theapache64.nemo.di.module.ApiModule
import com.theapache64.nemo.di.module.CartModule
import com.theapache64.nemo.utils.test.IdlingRule
import com.theapache64.nemo.utils.test.MainCoroutineRule
import com.theapache64.nemo.utils.test.monitorActivity
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by theapache64 : Dec 19 Sat,2020 @ 01:49
 */
@UninstallModules(ApiModule::class, CartModule::class)
@HiltAndroidTest
@LargeTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class CartActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val fakeNemoApi: NemoApi = mock()

    @BindValue
    @JvmField
    val configRepo: ConfigRepo = mock()

    @BindValue
    @JvmField
    val cartDao: CartDao = mock()

    @get:Rule
    val idlingRule = IdlingRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun init() {

        whenever(configRepo.getLocalConfig()).thenReturn(
            Config(
                totalProducts = 1000,
                productsPerPage = 10,
                currency = "$",
                deliveryCharge = 10,
                totalPages = 10
            )
        )
    }

    // No products show no products UI
    @Test
    fun givenCart_whenCartIsEmpty_thenCartEmptyShown() {
        whenever(cartDao.getCartProductsFlow()).thenReturn(flowOf(listOf()))
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val cartEmptyString = context.getString(R.string.cart_error_empty_cart)
        ActivityScenario.launch<CartActivity>(CartActivity.getStartIntent(context))
            .let { cartActivity ->
                idlingRule.dataBindingIdlingResource.monitorActivity(cartActivity)
                assertDisplayed(cartEmptyString)
                assertNotDisplayed(R.id.rv_cart)
                assertNotDisplayed(R.id.mcv_amount_payable)
            }
    }

    // Products show with same count
    @Test
    fun givenCart_whenGiven10Products_then10ProductsShown() {

        whenever(cartDao.getCartProductsFlow()).thenReturn(FakeCartDataStore.cart10Products)

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ActivityScenario.launch<CartActivity>(CartActivity.getStartIntent(context))
            .let { cartActivity ->
                idlingRule.dataBindingIdlingResource.monitorActivity(cartActivity)
                assertDisplayed(R.id.rv_cart)
                assertDisplayed(R.id.mcv_amount_payable)
                assertRecyclerViewItemCount(R.id.rv_cart, 10)
            }
    }

    // Click on plus button increments the item price and cart price
    // Click on minus button decrements the item price and cart price
    // Click on remove item from the list
    // Click on remove on last item shows no items in cart
    // Click on place order should launch address
}