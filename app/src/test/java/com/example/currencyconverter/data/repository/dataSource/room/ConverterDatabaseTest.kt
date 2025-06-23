package com.example.currencyconverter.data.repository.dataSource.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.currencyconverter.data.dataSource.room.ConverterDatabase
import com.example.currencyconverter.data.dataSource.room.account.dao.AccountDao
import com.example.currencyconverter.data.dataSource.room.account.dbo.AccountDbo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ConverterDatabaseTest {

    private lateinit var accountDao: AccountDao
    private lateinit var db: ConverterDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ConverterDatabase::class.java).build()
        accountDao = db.accountDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun `write and read account`() = runBlocking {
        val account = AccountDbo(code = "USD", amount = 100.0)
        accountDao.insertAll(account)
        val accounts = accountDao.getAll()

        assertEquals(listOf(account), accounts)
    }
}