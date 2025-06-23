package com.example.currencyconverter.ui.currencies

import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Rate
import com.example.currencyconverter.domain.repository.AccountRepository
import com.example.currencyconverter.domain.repository.RatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class CurrenciesViewModelTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var ratesRepository: RatesRepository
    private lateinit var accountRepository: AccountRepository
    private lateinit var viewModel: CurrenciesViewModel

    @Before
    fun setup() = runTest {
        ratesRepository = mock()
        accountRepository = mock()

        `when`(accountRepository.getAccountsFlow()).thenReturn(flowOf(emptyList()))
        `when`(accountRepository.getAccounts()).thenReturn(emptyList())

        viewModel = CurrenciesViewModel(ratesRepository, accountRepository)
    }

    @Test
    fun `selectCurrency updates state correctly`() = runTest {
        // Given
        val currency = Currency.EUR
        val rates = listOf(Rate(currency = Currency.EUR, value = 1.0))

        `when`(ratesRepository.getRates(currency.name, 1.0)).thenReturn(rates)

        // When
        viewModel.selectCurrency(currency)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(currency, state.selectedCurrency)
        assertEquals(rates, state.rates)
    }

    @Test
    fun `updateInputAmount updates state correctly`() = runTest {
        val amount = 100.0
        val rates = listOf(Rate(currency = Currency.USD, value = amount))

        `when`(ratesRepository.getRates("USD", amount)).thenReturn(rates)

        viewModel.toggleAmountInputMode()
        viewModel.updateInputAmount(amount)

        advanceUntilIdle()

        val state = viewModel.state.value
        println("Actual inputAmount: ${state.inputAmount}, Expected: $amount")
        println("Actual rates: ${state.rates}, Expected: $rates")

        assertEquals(amount, state.inputAmount, 0.0)
        assertEquals(rates, state.rates)
    }
}

@ExperimentalCoroutinesApi
class MainCoroutineRule : TestWatcher() {
    val testDispatcher = StandardTestDispatcher()

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}