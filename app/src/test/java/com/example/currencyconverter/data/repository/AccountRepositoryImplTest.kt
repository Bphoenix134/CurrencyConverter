package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import com.example.currencyconverter.domain.entity.Account
import com.example.currencyconverter.domain.entity.Currency
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

class AccountRepositoryImplTest {

    private lateinit var accountDao: AccountDao
    private lateinit var accountRepository: AccountRepositoryImpl

    @Before
    fun setUp() {
        accountDao = mock()
        accountRepository = AccountRepositoryImpl(accountDao)
    }

    @Test
    fun `getAccounts returns mapped accounts`() = runBlocking {
        val dboList = listOf(AccountDbo(code = "USD", amount = 100.0))
        val expected = listOf(Account(currency = Currency.USD, amount = 100.0))
        `when`(accountDao.getAll()).thenReturn(dboList)

        val result = accountRepository.getAccounts()

        assertEquals(expected, result)
    }

    @Test
    fun `getAccountsFlow returns mapped accounts flow`() = runBlocking {
        val dboList = listOf(AccountDbo(code = "USD", amount = 100.0))
        val expected = listOf(Account(currency = Currency.USD, amount = 100.0))
        `when`(accountDao.getAllAsFlow()).thenReturn(flowOf(dboList))

        accountRepository.getAccountsFlow().collect { result ->
            assertEquals(expected, result)
        }
    }
}