package com.example.currencyconverter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.currencyconverter.ui.currencies.CurrenciesScreen
import com.example.currencyconverter.ui.exchange.ExchangeScreen
import com.example.currencyconverter.ui.transactions.TransactionsScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Destinations.Currencies.route) {
        composable(Destinations.Currencies.route) {
            CurrenciesScreen(
                onNavigateToExchange = { toCurrency, fromCurrency, amount ->
                    navController.navigate(
                        "${Destinations.Exchange.route}/$toCurrency/$fromCurrency/$amount"
                    )
                },
                onNavigateToTransactions = { navController.navigate(Destinations.Transactions.route) }
            )
        }
        composable(
            route = "${Destinations.Exchange.route}/{toCurrency}/{fromCurrency}/{amount}",
            arguments = listOf(
                navArgument("toCurrency") { type = NavType.StringType },
                navArgument("fromCurrency") { type = NavType.StringType },
                navArgument("amount") { type = NavType.FloatType }
            )
        ) {
            ExchangeScreen(onNavigateBack = { navController.popBackStack(Destinations.Currencies.route, false) })
        }
        composable(Destinations.Transactions.route) {
            TransactionsScreen()
        }
    }
}