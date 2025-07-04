package com.example.currencyconverter.ui.exchange

import com.example.currencyconverter.domain.entity.Currency

data class ExchangeState(
    val fromCurrency: Currency = Currency.EUR,
    val toCurrency: Currency = Currency.USD,
    val fromAmount: Double = 0.0,
    val toAmount: Double = 0.0,
    val exchangeRate: Double = 0.0,
    val accounts: List<com.example.currencyconverter.domain.entity.Account> = emptyList()
)
