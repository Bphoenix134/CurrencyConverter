package com.example.currencyconverter.domain.mapper

import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Rate
import org.junit.Assert.assertEquals
import org.junit.Test

class RateMapperTest {

    @Test
    fun `RateDto to Rate maps correctly`() {
        val dto = RateDto(currency = "USD", value = 1.0)
        val expected = Rate(currency = Currency.USD, value = 1.0)

        val result = dto.toDomain()

        assertEquals(expected, result)
    }

    @Test
    fun `List of RateDto to List of Rate maps correctly`() {
        val dtoList = listOf(
            RateDto(currency = "USD", value = 1.0),
            RateDto(currency = "EUR", value = 0.85)
        )
        val expected = listOf(
            Rate(currency = Currency.USD, value = 1.0),
            Rate(currency = Currency.EUR, value = 0.85)
        )

        val result = dtoList.toDomain()

        assertEquals(expected, result)
    }
}