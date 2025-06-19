package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.dataSource.room.transaction.dao.TransactionDao
import com.example.currencyconverter.domain.entity.Transaction
import com.example.currencyconverter.domain.mapper.toDbo
import com.example.currencyconverter.domain.mapper.toDomain
import com.example.currencyconverter.domain.repository.TransactionRepository

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override suspend fun insertTransactions(vararg transactions: Transaction) {
        transactionDao.insertAll(*transactions.map { it.toDbo() }.toTypedArray())
    }

    override suspend fun getTransactions(): List<Transaction> {
        return transactionDao.getAll().toDomain()
    }
}