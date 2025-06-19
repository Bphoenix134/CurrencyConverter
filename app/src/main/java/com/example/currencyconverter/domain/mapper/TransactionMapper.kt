package com.example.currencyconverter.domain.mapper

import com.example.currencyconverter.data.dataSource.room.transaction.dbo.TransactionDbo
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Transaction

fun TransactionDbo.toDomain(): Transaction {
    return Transaction(
        id = id,
        fromCurrency = Currency.valueOf(from),
        toCurrency = Currency.valueOf(to),
        fromAmount = fromAmount,
        toAmount = toAmount,
        dateTime = dateTime
    )
}

fun Transaction.toDbo(): TransactionDbo {
    return TransactionDbo(
        id = id,
        from = fromCurrency.name,
        to = toCurrency.name,
        fromAmount = fromAmount,
        toAmount = toAmount,
        dateTime = dateTime
    )
}

fun List<TransactionDbo>.toDomain(): List<Transaction> = map { it.toDomain() }