package com.example.currencyconverter.di

import com.example.currencyconverter.data.dataSource.remote.RatesService
import com.example.currencyconverter.data.dataSource.remote.RemoteRatesServiceImpl
import com.example.currencyconverter.data.dataSource.room.ConverterDatabase
import com.example.currencyconverter.data.repository.AccountRepositoryImpl
import com.example.currencyconverter.data.repository.RatesRepositoryImpl
import com.example.currencyconverter.data.repository.TransactionRepositoryImpl
import com.example.currencyconverter.domain.repository.AccountRepository
import com.example.currencyconverter.domain.repository.RatesRepository
import com.example.currencyconverter.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRatesService(): RatesService {
        return RemoteRatesServiceImpl()
    }

    @Provides
    @Singleton
    fun provideRatesRepository(ratesService: RatesService): RatesRepository {
        return RatesRepositoryImpl(ratesService)
    }

    @Provides
    @Singleton
    fun provideAccountRepository(database: ConverterDatabase): AccountRepository {
        return AccountRepositoryImpl(database.accountDao())
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(database: ConverterDatabase): TransactionRepository {
        return TransactionRepositoryImpl(database.transactionDao())
    }
}