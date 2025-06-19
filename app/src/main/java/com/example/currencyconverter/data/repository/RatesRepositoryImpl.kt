package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.dataSource.remote.RatesService
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Rate
import com.example.currencyconverter.domain.mapper.toDomain
import com.example.currencyconverter.domain.repository.RatesRepository

class RatesRepositoryImpl(
    private val ratesService: RatesService
) : RatesRepository {
    override suspend fun  getRates(baseCurrency: String, amount: Double): List<Rate> {
        return ratesService.getRates(baseCurrency, amount).toDomain()
    }
}