package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.domain.entity.Account
import com.example.currencyconverter.domain.mapper.toDbo
import com.example.currencyconverter.domain.mapper.toDomain
import com.example.currencyconverter.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountRepositoryImpl(
    private val accountDao: AccountDao
) : AccountRepository {
    override suspend fun insertAccounts(vararg  accounts: Account) {
        accountDao.insertAll(*accounts.map { it.toDbo() }.toTypedArray())
    }

    override suspend fun getAccounts(): List<Account> {
        return accountDao.getAll().toDomain()
    }

    override fun getAccountsFlow(): Flow<List<Account>> {
        return accountDao.getAllAsFlow().map { it.toDomain() }
    }
}