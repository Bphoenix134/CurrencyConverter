package com.example.currencyconverter.ui.currencies

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.currencyconverter.utils.*

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
    val fullName = getCurrencyFullName(rate.currency)
    val formattedAmount = formatCurrency(rate.currency, amount)
    val formattedBalance = balance?.let { "Balance: ${formatCurrency(rate.currency, it)}" }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = getFlagResource(rate.currency)),
                contentDescription = "Flag",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = rate.currency.name, style = MaterialTheme.typography.titleMedium)
                Text(text = fullName, style = MaterialTheme.typography.bodySmall)
                if (formattedBalance != null) {
                    Text(text = formattedBalance, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))

            if (isAmountInputMode) {
                var amountText by remember { mutableStateOf(amount.toString()) }
                Column(horizontalAlignment = Alignment.End) {
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { text ->
                            amountText = text
                            text.toDoubleOrNull()?.let { onUpdateAmount(it) }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.width(120.dp)
                    )
                    IconButton(onClick = onResetAmount) {
                        Icon(
                            painter = painterResource(R.drawable.ic_clear),
                            contentDescription = "Reset"
                        )
                    }
                }
            } else {
                Text(
                    text = formattedAmount,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.clickable { if (isSelected) onToggleAmountInputMode() }
                )
            }
        }
    }
}

@Composable
fun getFlagResource(currency: Currency): Int {
    return when (currency) {
        Currency.USD -> R.drawable.flag_us
        Currency.EUR -> R.drawable.flag_eu
        Currency.GBP -> R.drawable.flag_gb
        Currency.AUD -> R.drawable.flag_au
        Currency.BGN -> R.drawable.flag_bg
        Currency.BRL -> R.drawable.flag_br
        Currency.CAD -> R.drawable.flag_ca
        Currency.CHF -> R.drawable.flag_ch
        Currency.CNY -> R.drawable.flag_cn
        Currency.CZK -> R.drawable.flag_cz
        Currency.DKK -> R.drawable.flag_dk
        Currency.HKD -> R.drawable.flag_hk
        Currency.HRK -> R.drawable.flag_hr
        Currency.HUF -> R.drawable.flag_hu
        Currency.IDR -> R.drawable.flag_id
        Currency.ILS -> R.drawable.flag_il
        Currency.INR -> R.drawable.flag_in
        Currency.ISK -> R.drawable.flag_is
        Currency.JPY -> R.drawable.flag_jp
        Currency.KRW -> R.drawable.flag_kr
        Currency.MXN -> R.drawable.flag_br
        Currency.MYR -> R.drawable.flag_my
        Currency.NOK -> R.drawable.flag_no
        Currency.NZD -> R.drawable.flag_nz
        Currency.PHP -> R.drawable.flag_ph
        Currency.PLN -> R.drawable.flag_pl
        Currency.RON -> R.drawable.flag_ro
        Currency.RUB -> R.drawable.flag_ru
        Currency.SEK -> R.drawable.flag_se
        Currency.SGD -> R.drawable.flag_sg
        Currency.THB -> R.drawable.flag_th
        Currency.TRY -> R.drawable.flag_tr
        Currency.ZAR -> R.drawable.flag_za
        else -> R.drawable.flag_default
    }
}