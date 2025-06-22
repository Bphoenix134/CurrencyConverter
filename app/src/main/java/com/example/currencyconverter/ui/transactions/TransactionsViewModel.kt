package com.example.currencyconverter.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionsRepository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsState())
    val state: StateFlow<TransactionsState> = _state

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            val transactions = transactionsRepository.getTransactions()
            _state.value = TransactionsState(transactions = transactions)
        }
    }
}