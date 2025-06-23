package com.example.currencyconverter.ui.exchange

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.ui.currencies.getFlagResource
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import com.example.currencyconverter.utils.formatCurrency
import com.example.currencyconverter.utils.getCurrencyFullName
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeScreen(
    onNavigateBack: () -> Unit
) {
    CurrencyConverterTheme {
        val viewModel = hiltViewModel<ExchangeViewModel>()
        val state by viewModel.state.collectAsState()

        val exchangeCompleted by viewModel.exchangeCompleted.collectAsState()

        LaunchedEffect(exchangeCompleted) {
            if (exchangeCompleted) {
                onNavigateBack()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("${state.fromCurrency.name} to ${state.toCurrency.name}") },
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
                    .fillMaxWidth()
            ) {
                Text(
                    text = "1 ${state.fromCurrency.name} = ${String.format(Locale.US, "%.2f", state.exchangeRate)} ${state.toCurrency.name}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = getFlagResource(state.toCurrency)),
                        contentDescription = "${state.toCurrency.name} flag",
                        modifier = Modifier.size(40.dp)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {
                        Text(state.toCurrency.name, style = MaterialTheme.typography.titleMedium)
                        Text(getCurrencyFullName(state.toCurrency), style = MaterialTheme.typography.bodySmall)
                        val toBalance = state.accounts.find { it.currency == state.toCurrency }?.amount
                        if (toBalance != null) {
                            Text("Balance: ${formatCurrency(state.toCurrency, toBalance)}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        Text("+${String.format(Locale.US, "%.2f", state.toAmount)}", style = MaterialTheme.typography.titleMedium)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = getFlagResource(state.fromCurrency)),
                        contentDescription = "${state.fromCurrency.name} flag",
                        modifier = Modifier.size(40.dp)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {
                        Text(state.fromCurrency.name, style = MaterialTheme.typography.titleMedium)
                        Text(getCurrencyFullName(state.fromCurrency), style = MaterialTheme.typography.bodySmall)
                        val fromBalance = state.accounts.find { it.currency == state.fromCurrency }?.amount
                        if (fromBalance != null) {
                            Text("Balance: ${formatCurrency(state.fromCurrency, fromBalance)}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        Text("-${String.format(Locale.US, "%.2f", state.fromAmount)}", style = MaterialTheme.typography.titleMedium)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.performExchange()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Купить ${state.toCurrency.name} за ${state.fromCurrency.name}")
                }
            }
        }
    }
}