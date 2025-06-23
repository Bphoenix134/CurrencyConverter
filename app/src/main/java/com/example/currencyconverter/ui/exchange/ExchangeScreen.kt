package com.example.currencyconverter.ui.exchange

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.domain.entity.Transaction
import com.example.currencyconverter.ui.currencies.getFlagResource
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import com.example.currencyconverter.ui.transactions.TransactionsViewModel
import com.example.currencyconverter.utils.formatCurrency
import com.example.currencyconverter.utils.getCurrencyFullName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeScreen(onNavigateBack: () -> Unit) {
    CurrencyConverterTheme {
        val viewModel = hiltViewModel<ExchangeViewModel>()
        val state by viewModel.state.collectAsState()
        val exchangeCompleted by viewModel.exchangeCompleted.collectAsState()

        LaunchedEffect(exchangeCompleted) {
            if (exchangeCompleted) onNavigateBack()
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Обмен валют") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ExchangeItem(
                    currency = state.toCurrency,
                    amount = state.toAmount,
                    isIncoming = true,
                    balance = state.accounts.find { it.currency == state.toCurrency }?.amount
                )
                ExchangeItem(
                    currency = state.fromCurrency,
                    amount = state.fromAmount,
                    isIncoming = false,
                    balance = state.accounts.find { it.currency == state.fromCurrency }?.amount
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { viewModel.performExchange() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Купить ${state.toCurrency.name} за ${state.fromCurrency.name}")
                }
            }
        }
    }
}

@Composable
fun ExchangeItem(currency: com.example.currencyconverter.domain.entity.Currency, amount: Double, isIncoming: Boolean, balance: Double?) {
    val prefix = if (isIncoming) "+" else "-"
    val formattedAmount = "$prefix${String.format(Locale.US, "%.2f", amount)}"
    val formattedBalance = balance?.let { "Balance: ${formatCurrency(currency, it)}" }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = getFlagResource(currency)),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(currency.name, style = MaterialTheme.typography.titleMedium)
                Text(getCurrencyFullName(currency), style = MaterialTheme.typography.bodySmall)
                formattedBalance?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(formattedAmount, style = MaterialTheme.typography.titleMedium)
        }
    }
}