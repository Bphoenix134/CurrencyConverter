package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.domain.entity.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun insertAccounts(vararg accounts: Account)
    suspend fun getAccounts(): List<Account>
    fun getAccountsFlow(): Flow<List<Account>>
}