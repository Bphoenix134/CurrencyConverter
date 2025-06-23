package com.example.currencyconverter.domain.mapper

import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.domain.entity.Account
import com.example.currencyconverter.domain.entity.Currency
import org.junit.Assert.assertEquals
import org.junit.Test

class AccountMapperTest {

    @Test
    fun `AccountDbo to Account maps correctly`() {
        val dbo = AccountDbo(code = "USD", amount = 100.0)
        val expected = Account(currency = Currency.USD, amount = 100.0)

        val result = dbo.toDomain()

        assertEquals(expected, result)
    }

    @Test
    fun `Account to AccountDbo maps correctly`() {
        val account = Account(currency = Currency.EUR, amount = 200.0)
        val expected = AccountDbo(code = "EUR", amount = 200.0)

        val result = account.toDbo()

        assertEquals(expected, result)
    }

    @Test
    fun `List of AccountDbo to List of Account maps correctly`() {
        val dboList = listOf(
            AccountDbo(code = "USD", amount = 100.0),
            AccountDbo(code = "EUR", amount = 200.0)
        )
        val expected = listOf(
            Account(currency = Currency.USD, amount = 100.0),
            Account(currency = Currency.EUR, amount = 200.0)
        )

        val result = dboList.toDomain()

        assertEquals(expected, result)
    }
}