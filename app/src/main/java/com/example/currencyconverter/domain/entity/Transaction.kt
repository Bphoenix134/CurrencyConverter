package com.example.currencyconverter.domain.entity

import java.time.LocalDateTime

data class Transaction(
    val id: Int,
    val fromCurrency: Currency,
    val toCurrency: Currency,
    val fromAmount: Double,
    val toAmount: Double,
    val dateTime: LocalDateTime
)
