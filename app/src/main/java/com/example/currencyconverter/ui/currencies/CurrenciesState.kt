package com.example.currencyconverter.ui.currencies

import com.example.currencyconverter.domain.entity.Account
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Rate

data class CurrenciesState(
    val selectedCurrency: Currency = Currency.USD,
    val rates: List<Rate> = emptyList(),
    val filteredRates: List<Rate> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val isAmountInputMode: Boolean = false,
    val inputAmount: Double = 1.0
)
