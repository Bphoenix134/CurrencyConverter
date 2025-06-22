package com.example.currencyconverter.ui.transactions

import com.example.currencyconverter.domain.entity.Transaction

data class TransactionsState(
    val transactions: List<Transaction> = emptyList()
)
