package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.domain.entity.Transaction

interface TransactionRepository {
    suspend fun insertTransactions(vararg transactions: Transaction)
    suspend fun getTransactions(): List<Transaction>
}