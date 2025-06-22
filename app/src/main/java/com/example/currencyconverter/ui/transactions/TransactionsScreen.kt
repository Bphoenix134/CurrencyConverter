package com.example.currencyconverter.ui.transactions

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import com.example.currencyconverter.domain.entity.Transaction
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen() {
    CurrencyConverterTheme {
        val viewModel = hiltViewModel<TransactionsViewModel>()
        val state by viewModel.state.collectAsState()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Транзакции") }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {
                items(state.transactions) { transaction ->
                    TransactionItem(transaction = transaction)
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Text("${transaction.fromCurrency} -> ${transaction.toCurrency}")
            Text("${String.format(Locale.US, "%.2f", transaction.fromAmount)} -> ${String.format(Locale.US, "%.2f", transaction.toAmount)}")
            Text(transaction.dateTime.toString())
        }
    }
}
