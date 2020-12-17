package com.theapache64.nemo.feature.productdetail

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaSwipeRefreshInteractions.refresh
import com.theapache64.nemo.FakeProductDataStore
import com.theapache64.nemo.R
import com.theapache64.nemo.data.local.table.cart.CartDao
import com.theapache64.nemo.data.local.table.cart.CartEntity
import com.theapache64.nemo.data.remote.Config
import com.theapache64.nemo.data.remote.NemoApi
import com.theapache64.nemo.data.repository.ConfigRepo
import com.theapache64.nemo.di.module.ApiModule
import com.theapache64.nemo.feature.cart.CartActivity
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
import org.mockito.Mockito

/**
 * Created by theapache64 : Dec 08 Tue,2020 @ 08:09
 */
@UninstallModules(ApiModule::class, CartModule::class)
@HiltAndroidTest
@LargeTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class ProductDetailActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val fakeNemoApi: NemoApi = Mockito.mock(NemoApi::class.java)

    @BindValue
    @JvmField
    val configRepo: ConfigRepo = Mockito.mock(ConfigRepo::class.java)

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

    // Good product show image, title, rating, price, details, buy now button
    @Test
    fun givenProduct_whenGoodProduct_thenImageTitleRatingPriceAndBuyNowShown() {
        val productId = 1

        whenever(fakeNemoApi.getProduct(productId))
            .thenReturn(FakeProductDataStore.productSuccessFlow)

        whenever(cartDao.getCartProductsFlow()).thenReturn(flowOf(listOf()))

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ActivityScenario.launch<ProductDetailActivity>(
            ProductDetailActivity.getStartIntent(
                context,
                productId
            )
        ).run {
            idlingRule.dataBindingIdlingResource.monitorActivity(this)
            assertDisplayed(R.id.iv_product_image)
            assertDisplayed(R.id.tv_product_title)
            assertDisplayed(R.id.mrb_product_rating)
            assertDisplayed(R.id.tv_price)
            assertDisplayed(R.id.b_buy_now)
        }
    }

    // If product already exist in cart, GOTO cart should show
    @Test
    fun givenProduct_whenExistInCart_thenGoToCartDisplayed() {
        val productId = 1

        whenever(fakeNemoApi.getProduct(productId))
            .thenReturn(FakeProductDataStore.productSuccessFlow)

        whenever(cartDao.getCartProductsFlow()).thenReturn(
            flowOf(listOf(CartEntity(productId, 10)))
        )

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ActivityScenario.launch<ProductDetailActivity>(
            ProductDetailActivity.getStartIntent(
                context,
                productId
            )
        ).run {
            idlingRule.dataBindingIdlingResource.monitorActivity(this)
            assertDisplayed(R.id.b_go_to_cart)
            assertNotDisplayed(R.id.b_add_to_cart)
        }

    }

    // If product doesn't exist in cart, 'Add to cart' should show
    @Test
    fun givenProduct_whenNotInCart_thenAddToCartDisplayed() {
        val productId = 1

        whenever(fakeNemoApi.getProduct(productId))
            .thenReturn(FakeProductDataStore.productSuccessFlow)

        // empty products in cart
        whenever(cartDao.getCartProductsFlow()).thenReturn(flowOf(listOf()))

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ActivityScenario.launch<ProductDetailActivity>(
            ProductDetailActivity.getStartIntent(
                context,
                productId
            )
        ).run {
            idlingRule.dataBindingIdlingResource.monitorActivity(this)
            assertDisplayed(R.id.b_add_to_cart)
            assertNotDisplayed(R.id.b_go_to_cart)
        }
    }

    // Clicking add to cart should show go to cart
    @Test
    fun givenProductDetailPage_whenAddToCartClicked_thenGoToCartDisplayed() {
        val productId = 1

        whenever(fakeNemoApi.getProduct(productId))
            .thenReturn(FakeProductDataStore.productSuccessFlow)

        // empty products in cart
        whenever(cartDao.getCartProductsFlow()).thenReturn(flowOf(listOf()))

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ActivityScenario.launch<ProductDetailActivity>(
            ProductDetailActivity.getStartIntent(
                context,
                productId
            )
        ).run {
            idlingRule.dataBindingIdlingResource.monitorActivity(this)
            assertDisplayed(R.id.b_add_to_cart)
            clickOn(R.id.b_add_to_cart)
            assertNotDisplayed(R.id.b_add_to_cart)
            assertDisplayed(R.id.b_go_to_cart)
        }
    }

    // Clicking go to cart should launch cart activity
    @Test
    fun givenProductDetailPage_whenGoToCartClicked_thenCartActivityDisplayed() {
        val productId = 1

        whenever(fakeNemoApi.getProduct(productId))
            .thenReturn(FakeProductDataStore.productSuccessFlow)

        // 1 product in cart
        whenever(cartDao.getCartProductsFlow()).thenReturn(
            flowOf(
                listOf(
                    CartEntity(productId, 10)
                )
            )
        )

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ActivityScenario.launch<ProductDetailActivity>(
            ProductDetailActivity.getStartIntent(
                context,
                productId
            )
        ).run {
            idlingRule.dataBindingIdlingResource.monitorActivity(this)
            assertDisplayed(R.id.b_go_to_cart)
            Intents.init()
            clickOn(R.id.b_go_to_cart)
            intended(hasComponent(CartActivity::class.java.name))
            Intents.release()
        }
    }

    // Swipe top to down should refresh page
    @Test
    fun givenProductDetailPage_whenSwipeDown_thenPageRefreshed() {
        val productId = 1

        whenever(fakeNemoApi.getProduct(productId))
            .thenReturn(FakeProductDataStore.productSuccessFlow)

        whenever(cartDao.getCartProductsFlow()).thenReturn(
            flowOf(
                listOf(
                )
            )
        )

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        ActivityScenario.launch<ProductDetailActivity>(
            ProductDetailActivity.getStartIntent(
                context,
                productId
            )
        ).run {
            idlingRule.dataBindingIdlingResource.monitorActivity(this)
            assertDisplayed("Product 1")

            // Now change data
            whenever(fakeNemoApi.getProduct(productId))
                .thenReturn(FakeProductDataStore.productSuccessFlow2)
            refresh(R.id.csrl_product)
            assertDisplayed("Product 2")
        }
    }

    // Clicking buy now should go to order summary

}