package com.example.currencyconverter.ui.exchange

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeScreen(
    onNavigateBack: () -> Unit
) {
    CurrencyConverterTheme {
        val viewModel = hiltViewModel<ExchangeViewModel>()
        val state by viewModel.state.collectAsState()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Обмен") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Exchange Rate: 1 ${state.fromCurrency} = ${String.format(Locale.US, "%.4f", state.exchangeRate)} ${state.toCurrency}")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Buy: ${String.format(Locale.US, "%.2f", state.fromAmount)} ${state.fromCurrency}")
            Text("Pay: ${String.format(Locale.US, "%.2f", state.toAmount)} ${state.toCurrency}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.performExchange()
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Купить")
            }
        }
    }
}
}