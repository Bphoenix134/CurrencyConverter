package com.example.currencyconverter.ui.currencies

import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Rate
import com.example.currencyconverter.domain.repository.AccountRepository
import com.example.currencyconverter.domain.repository.RatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

class CurrenciesViewModelTest {

    private lateinit var ratesRepository: RatesRepository
    private lateinit var accountRepository: AccountRepository
    private lateinit var viewModel: CurrenciesViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        ratesRepository = mock()
        accountRepository = mock()
        viewModel = CurrenciesViewModel(ratesRepository, accountRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `selectCurrency updates state correctly`() = runTest(testDispatcher) {
        val currency = Currency.EUR
        val rates = listOf(Rate(currency = Currency.EUR, value = 1.0))
        `when`(ratesRepository.getRates("EUR", 1.0)).thenReturn(rates)
        `when`(accountRepository.getAccountsFlow()).thenReturn(flowOf(emptyList()))

        viewModel.selectCurrency(currency)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(currency, state.selectedCurrency)
        assertEquals(rates, state.rates)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updateInputAmount updates state correctly`() = runTest(testDispatcher) {
        val amount = 100.0
        val rates = listOf(Rate(currency = Currency.USD, value = amount))
        `when`(ratesRepository.getRates("USD", amount)).thenReturn(rates)
        `when`(accountRepository.getAccountsFlow()).thenReturn(flowOf(emptyList()))

        viewModel.toggleAmountInputMode()
        viewModel.updateInputAmount(amount)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(amount, state.inputAmount, 0.0)
        assertEquals(rates, state.rates)
    }
}