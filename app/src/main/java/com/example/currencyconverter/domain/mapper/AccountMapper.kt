package com.example.currencyconverter.domain.mapper

import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.domain.entity.Account
import com.example.currencyconverter.domain.entity.Currency

fun AccountDbo.toDomain(): Account {
    return Account(
        currency = Currency.valueOf(code),
        amount = amount
    )
}

fun Account.toDbo(): AccountDbo {
    return AccountDbo(
        code = currency.name,
        amount = amount
    )
}

fun List<AccountDbo>.toDomain(): List<Account> = map { it.toDomain() }