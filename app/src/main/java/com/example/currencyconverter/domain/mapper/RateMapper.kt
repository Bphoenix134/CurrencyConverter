package com.example.currencyconverter.domain.mapper

import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Rate

fun RateDto.toDomain(): Rate {
    return Rate(
        currency = Currency.valueOf(currency),
        value = value
    )
}

fun List<RateDto>.toDomain(): List<Rate> = map { it.toDomain() }