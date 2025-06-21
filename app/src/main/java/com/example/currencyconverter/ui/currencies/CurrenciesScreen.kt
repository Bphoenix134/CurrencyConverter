package com.example.currencyconverter.ui.currencies

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.R
import com.example.currencyconverter.domain.entity.Currency
import com.example.currencyconverter.domain.entity.Rate
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrenciesScreen(
    onNavigateToExchange: (Currency, Currency, Float) -> Unit,
    onNavigateToTransactions: () -> Unit
) {
    CurrencyConverterTheme {
        val viewModel = hiltViewModel<CurrenciesViewModel>()
        val state by viewModel.state.collectAsState()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Валюты") },
                    actions = {
                        IconButton(onClick = onNavigateToTransactions) {
                            Icon(
                                painter = painterResource(R.drawable.ic_transactions),
                                contentDescription = "Транзакции"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            CurrenciesContent(
                state = state,
                onSelectCurrency = { viewModel.selectCurrency(it) },
                onToggleAmountInputMode = { viewModel.toggleAmountInputMode() },
                onUpdateAmount = { viewModel.updateInputAmount(it) },
                onResetAmount = { viewModel.resetInputAmount() },
                onProceedToExchange = { currency ->
                    onNavigateToExchange(state.selectedCurrency, currency, state.inputAmount.toFloat())
                },
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
fun CurrenciesContent(
    state: CurrenciesState,
    onSelectCurrency: (Currency) -> Unit,
    onToggleAmountInputMode: () -> Unit,
    onUpdateAmount: (Double) -> Unit,
    onResetAmount: () -> Unit,
    onProceedToExchange: (Currency) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(state.filteredRates) { rate ->
                CurrencyItem(
                    rate = rate,
                    amount = rate.value,
                    balance = state.accounts.find { it.currency == rate.currency }?.amount,
                    isSelected = rate.currency == state.selectedCurrency,
                    isAmountInputMode = state.isAmountInputMode && rate.currency == state.selectedCurrency,
                    onClick = {
                        if (state.isAmountInputMode && rate.currency != state.selectedCurrency) {
                            onProceedToExchange(rate.currency)
                        } else {
                            onSelectCurrency(rate.currency)
                        }
                    },
                    onToggleAmountInputMode = onToggleAmountInputMode,
                    onUpdateAmount = onUpdateAmount,
                    onResetAmount = onResetAmount
                )
            }
        }
    }
}

@Composable
fun CurrencyItem(
    rate: Rate,
    amount: Double,
    balance: Double?,
    isSelected: Boolean,
    isAmountInputMode: Boolean,
    onClick: () -> Unit,
    onToggleAmountInputMode: () -> Unit,
    onUpdateAmount: (Double) -> Unit,
    onResetAmount: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = getFlagResource(rate.currency)),
            contentDescription = "Flag",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = rate.currency.name)
            Text(text = "${rate.currency}: ${String.format(Locale.US, "%.2f", amount)}")
            balance?.let {
                Text(text = "Баланс: ${String.format(Locale.US, "%.2f", it)}")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (isAmountInputMode) {
            var amountText by remember { mutableStateOf(amount.toString()) }
            OutlinedTextField(
                value = amountText,
                onValueChange = { text ->
                    amountText = text
                    text.toDoubleOrNull()?.let { onUpdateAmount(it) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(100.dp)
            )
            IconButton(onClick = onResetAmount) {
                Icon(
                    painter = painterResource(R.drawable.ic_clear),
                    contentDescription = "Reset"
                )
            }
        } else if (isSelected) {
            Text(
                text = "1",
                modifier = Modifier.clickable { onToggleAmountInputMode() }
            )
        }
    }
}

@Composable
fun getFlagResource(currency: Currency): Int {
    return when (currency) {
        Currency.USD -> R.drawable.flag_us
        Currency.RUB -> R.drawable.flag_ru
        Currency.EUR -> R.drawable.flag_eu
        else -> R.drawable.flag_default
    }
}