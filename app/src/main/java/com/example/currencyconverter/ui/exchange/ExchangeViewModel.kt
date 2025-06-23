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

    private val _exchangeCompleted = MutableStateFlow(false)
    val exchangeCompleted: StateFlow<Boolean> = _exchangeCompleted

    init {
        val toCurrencyStr = savedStateHandle.get<String>("toCurrency") ?: "USD"
        val fromCurrencyStr = savedStateHandle.get<String>("fromCurrency") ?: "EUR"
        val amount = savedStateHandle.get<Float>("amount")?.toDouble() ?: 1.0

        try {
            val toCurrency = Currency.valueOf(toCurrencyStr)
            val fromCurrency = Currency.valueOf(fromCurrencyStr)
            loadEverything(fromCurrency, toCurrency, amount)
        } catch (e: IllegalArgumentException) {
            Log.e("ExchangeViewModel", "Invalid currency: to=$toCurrencyStr, from=$fromCurrencyStr", e)
        }
    }

    private fun loadEverything(fromCurrency: Currency, toCurrency: Currency, amount: Double) {
        viewModelScope.launch {
            val accounts = accountRepository.getAccounts()

            val rates = ratesRepository.getRates(fromCurrency.name, 1.0)
            val toRate = rates.find { it.currency == toCurrency }?.value ?: 0.0
            val fromAmount = amount / toRate

            _state.value = ExchangeState(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
                fromAmount = fromAmount,
                toAmount = amount,
                exchangeRate = toRate,
                accounts = accounts
            )
        }
    }

    fun performExchange() {
        viewModelScope.launch {
            val currentState = _state.value

            if (currentState.accounts.isEmpty()) {
                Log.w("ExchangeViewModel", "Accounts not loaded yet")
                return@launch
            }

            val fromAccount = currentState.accounts.find { it.currency == currentState.fromCurrency }
            val toAccount = currentState.accounts.find { it.currency == currentState.toCurrency }

            if (fromAccount != null && fromAccount.amount >= currentState.fromAmount) {
                val updatedAccounts = mutableListOf<Account>()
                updatedAccounts.add(fromAccount.copy(amount = fromAccount.amount - currentState.fromAmount))
                if (toAccount != null) {
                    updatedAccounts.add(toAccount.copy(amount = toAccount.amount + currentState.toAmount))
                } else {
                    updatedAccounts.add(Account(currency = currentState.toCurrency, amount = currentState.toAmount))
                }

                accountRepository.insertAccounts(*updatedAccounts.toTypedArray())

                val transaction = Transaction(
                    id = 0,
                    fromCurrency = currentState.fromCurrency,
                    toCurrency = currentState.toCurrency,
                    fromAmount = currentState.fromAmount,
                    toAmount = currentState.toAmount,
                    dateTime = LocalDateTime.now()
                )
                transactionRepository.insertTransactions(transaction)

                _exchangeCompleted.value = true

                Log.d("ExchangeViewModel", "Transaction saved")
            } else {
                Log.w("ExchangeViewModel", "Insufficient funds or account not found")
            }
        }
    }
}