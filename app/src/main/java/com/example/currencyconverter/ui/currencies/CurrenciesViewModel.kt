package com.example.currencyconverter.ui.currencies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Account
import com.example.currencyconverter.domain.repository.RatesRepository
import com.example.currencyconverter.domain.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val ratesRepository: RatesRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CurrenciesState())
    val state: StateFlow<CurrenciesState> = _state

    private var ratesJob: Job? = null

    init {
        initializeAccount()
        startRatesUpdate()
        collectAccounts()
    }

    private fun initializeAccount() {
        viewModelScope.launch {
            val accounts = accountRepository.getAccounts()
            if (accounts.isEmpty()) {
                accountRepository.insertAccounts(
                    Account(currency = Currency.RUB, amount = 75000.0)
                )
            }
            _state.value = _state.value.copy(selectedCurrency = Currency.USD)
        }
    }

    private fun collectAccounts() {
        viewModelScope.launch {
            accountRepository.getAccountsFlow().collectLatest { accounts ->
                _state.value = _state.value.copy(accounts = accounts)
                updateFilteredRates()
            }
        }
    }

    private fun startRatesUpdate() {
        ratesJob?.cancel()
        ratesJob = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    val amount = if (_state.value.isAmountInputMode) _state.value.inputAmount else 1.0
                    val rates = ratesRepository.getRates(
                        baseCurrency = _state.value.selectedCurrency.name,
                        amount = amount
                    )
                    _state.value = _state.value.copy(rates = rates)
                    updateFilteredRates()
                } catch (e: Exception) {
//                    Log.d("CurrenciesViewModel", "$e")
                }
                delay(1000)
            }
        }
    }

    private fun updateFilteredRates() {
        val rates = _state.value.rates
        val accounts = _state.value.accounts
        val selectedCurrency = _state.value.selectedCurrency

        val filteredRates = if (_state.value.isAmountInputMode) {
            rates.filter { rate ->
                if (rate.currency == selectedCurrency) true
                else {
                    val account = accounts.find { it.currency == rate.currency }
                    account != null && account.amount >= rate.value
                }
            }
        } else {
            rates
        }

        _state.value = _state.value.copy(filteredRates = filteredRates)
    }

    suspend fun loadRatesOnce() {
        val amount = if (_state.value.isAmountInputMode) _state.value.inputAmount else 1.0
        val rates = ratesRepository.getRates(
            baseCurrency = _state.value.selectedCurrency.name,
            amount = amount
        )
        _state.value = _state.value.copy(rates = rates)
        updateFilteredRates()
    }

    fun selectCurrency(currency: Currency) {
        if (!_state.value.isAmountInputMode) {
            _state.value = _state.value.copy(selectedCurrency = currency)
            viewModelScope.launch {
                loadRatesOnce()
            }
            startRatesUpdate()
        }
    }

    fun updateInputAmount(amount: Double) {
        _state.value = _state.value.copy(inputAmount = amount)
        viewModelScope.launch {
            loadRatesOnce()
        }
        startRatesUpdate()
    }

    fun toggleAmountInputMode() {
        _state.value = _state.value.copy(
            isAmountInputMode = !_state.value.isAmountInputMode,
            inputAmount = 1.0
        )
        startRatesUpdate()
    }

    fun resetInputAmount() {
        _state.value = _state.value.copy(
            isAmountInputMode = false,
            inputAmount = 1.0
        )
        startRatesUpdate()
    }
}