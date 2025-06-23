package com.example.currencyconverter.data.repository

import com.example.currencyconverter.data.dataSource.remote.RatesService
import com.example.currencyconverter.data.dataSource.remote.dto.RateDto
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Rate
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

class RatesRepositoryImplTest {

    private lateinit var ratesService: RatesService
    private lateinit var ratesRepository: RatesRepositoryImpl

    @Before
    fun setUp() {
        ratesService = mock()
        ratesRepository = RatesRepositoryImpl(ratesService)
    }

    @Test
    fun `getRates returns mapped rates`() = runBlocking {
        val dtoList = listOf(RateDto(currency = "USD", value = 1.0))
        val expected = listOf(Rate(currency = Currency.USD, value = 1.0))
        `when`(ratesService.getRates("USD", 1.0)).thenReturn(dtoList)

        val result = ratesRepository.getRates("USD", 1.0)

        assertEquals(expected, result)
    }
}