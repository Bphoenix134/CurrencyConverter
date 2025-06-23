package com.example.currencyconverter.ui.exchange

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.entity.Account
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Transaction
import com.example.currencyconverter.domain.repository.AccountRepository
import com.example.currencyconverter.domain.repository.RatesRepository
import com.example.currencyconverter.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val ratesRepository: RatesRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ExchangeState())
    val state: StateFlow<ExchangeState> = _state

    init {
        val toCurrencyStr = savedStateHandle.get<String>("toCurrency") ?: "USD"
        val fromCurrencyStr = savedStateHandle.get<String>("fromCurrency") ?: "EUR"
        val amount = savedStateHandle.get<Float>("amount")?.toDouble() ?: 1.0

        try {
            val toCurrency = Currency.valueOf(toCurrencyStr)
            val fromCurrency = Currency.valueOf(fromCurrencyStr)
            loadExchangeData(fromCurrency, toCurrency, amount)
            loadAccounts()
        } catch (e: IllegalArgumentException) {
            Log.e("ExchangeViewModel", "Invalid currency: to=$toCurrencyStr, from=$fromCurrencyStr", e)
            loadExchangeData(Currency.EUR, Currency.USD, amount)
        }
    }

    private fun loadExchangeData(fromCurrency: Currency, toCurrency: Currency, amount: Double) {
        viewModelScope.launch {
            try {
                val rates = ratesRepository.getRates(fromCurrency.name, 1.0)
                val toRate = rates.find { it.currency == toCurrency }?.value ?: 0.0
                val fromAmount = amount / toRate
                _state.value = ExchangeState(
                    fromCurrency = fromCurrency,
                    toCurrency = toCurrency,
                    fromAmount = fromAmount,
                    toAmount = amount,
                    exchangeRate = toRate
                )
            } catch (e: Exception) {
                Log.e("ExchangeViewModel", "Failed to load exchange data", e)
                _state.value = ExchangeState(
                    fromCurrency = fromCurrency,
                    toCurrency = toCurrency,
                    fromAmount = amount,
                    toAmount = 0.0,
                    exchangeRate = 0.0
                )
            }
        }
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            val accounts = accountRepository.getAccounts()
            _state.value = _state.value.copy(accounts = accounts)
        }
    }

    fun performExchange() {
        viewModelScope.launch {
            val state = _state.value
            val accounts = state.accounts
            val fromAccount = accounts.find { it.currency == state.fromCurrency }
            val toAccount = accounts.find { it.currency == state.toCurrency }

            if (fromAccount != null && fromAccount.amount >= state.fromAmount) {
                val updatedAccounts = mutableListOf<Account>()
                updatedAccounts.add(fromAccount.copy(amount = fromAccount.amount - state.fromAmount))
                if (toAccount != null) {
                    updatedAccounts.add(toAccount.copy(amount = toAccount.amount + state.toAmount))
                } else {
                    updatedAccounts.add(Account(currency = state.toCurrency, amount = state.toAmount))
                }
                accountRepository.insertAccounts(*updatedAccounts.toTypedArray())

                val transaction = Transaction(
                    id = 0,
                    fromCurrency = state.fromCurrency,
                    toCurrency = state.toCurrency,
                    fromAmount = state.fromAmount,
                    toAmount = state.toAmount,
                    dateTime = LocalDateTime.now()
                )
                transactionRepository.insertTransactions(transaction)
            } else {
                Log.w("ExchangeViewModel", "Insufficient funds or account not found")
            }
        }
    }
}