package com.example.currencyconverter.domain.repository

import com.example.currencyconverter.domain.entity.Rate

interface RatesRepository {
    suspend fun getRates(baseCurrency: String, amount: Double): List<Rate>
}