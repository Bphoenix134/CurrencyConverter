package com.example.currencyconverter.utils

import com.example.currencyconverter.domain.entity.Currency
import java.util.Locale

fun getCurrencyFullName(currency: Currency): String {
    return when (currency) {
        Currency.USD -> "US Dollar"
        Currency.EUR -> "Euro"
        Currency.GBP -> "Great Britain Pound"
        Currency.AUD -> "Australian Dollar"
        Currency.BGN -> "Bulgarian Lev"
        Currency.BRL -> "Brazilian Real"
        Currency.CAD -> "Canadian Dollar"
        Currency.CHF -> "Swiss Franc"
        Currency.CNY -> "Chinese Yuan"
        Currency.CZK -> "Czech Koruna"
        Currency.DKK -> "Danish Krone"
        Currency.HKD -> "Hong Kong Dollar"
        Currency.HRK -> "Croatian Kuna"
        Currency.HUF -> "Hungarian Forint"
        Currency.IDR -> "Indonesian Rupiah"
        Currency.ILS -> "Israeli New Shekel"
        Currency.INR -> "Indian Rupee"
        Currency.ISK -> "Icelandic Króna"
        Currency.JPY -> "Japanese Yen"
        Currency.KRW -> "South Korean Won"
        Currency.MXN -> "Mexican Peso"
        Currency.MYR -> "Malaysian Ringgit"
        Currency.NOK -> "Norwegian Krone"
        Currency.NZD -> "New Zealand Dollar"
        Currency.PHP -> "Philippine Peso"
        Currency.PLN -> "Polish Zloty"
        Currency.RON -> "Romanian Leu"
        Currency.RUB -> "Russian Ruble"
        Currency.SEK -> "Swedish Krona"
        Currency.SGD -> "Singapore Dollar"
        Currency.THB -> "Thai Baht"
        Currency.TRY -> "Turkish Lira"
        Currency.ZAR -> "South African Rand"
    }
}

fun formatCurrency(currency: Currency, value: Double): String {
    return when (currency) {
        Currency.USD -> "$%.2f".format(Locale.US, value)
        Currency.EUR -> "€%.2f".format(Locale.US, value)
        Currency.GBP -> "£%.2f".format(Locale.US, value)
        Currency.AUD -> "A$%.2f".format(Locale.US, value)
        Currency.BGN -> "лв%.2f".format(Locale.US, value)
        Currency.BRL -> "R$%.2f".format(Locale.US, value)
        Currency.CAD -> "CA$%.2f".format(Locale.US, value)
        Currency.CHF -> "CHF %.2f".format(Locale.US, value)
        Currency.CNY -> "¥%.2f".format(Locale.US, value)
        Currency.CZK -> "Kč%.2f".format(Locale.US, value)
        Currency.DKK -> "kr%.2f".format(Locale.US, value)
        Currency.HKD -> "HK$%.2f".format(Locale.US, value)
        Currency.HRK -> "kn%.2f".format(Locale.US, value)
        Currency.HUF -> "Ft%.2f".format(Locale.US, value)
        Currency.IDR -> "Rp%.2f".format(Locale.US, value)
        Currency.ILS -> "₪%.2f".format(Locale.US, value)
        Currency.INR -> "₹%.2f".format(Locale.US, value)
        Currency.ISK -> "kr%.2f".format(Locale.US, value)
        Currency.JPY -> "¥%.2f".format(Locale.US, value)
        Currency.KRW -> "₩%.2f".format(Locale.US, value)
        Currency.MXN -> "Mex$%.2f".format(Locale.US, value)
        Currency.MYR -> "RM%.2f".format(Locale.US, value)
        Currency.NOK -> "kr%.2f".format(Locale.US, value)
        Currency.NZD -> "NZ$%.2f".format(Locale.US, value)
        Currency.PHP -> "₱%.2f".format(Locale.US, value)
        Currency.PLN -> "zł%.2f".format(Locale.US, value)
        Currency.RON -> "lei%.2f".format(Locale.US, value)
        Currency.RUB -> "₽%.2f".format(Locale.US, value)
        Currency.SEK -> "kr%.2f".format(Locale.US, value)
        Currency.SGD -> "S$%.2f".format(Locale.US, value)
        Currency.THB -> "฿%.2f".format(Locale.US, value)
        Currency.TRY -> "₺%.2f".format(Locale.US, value)
        Currency.ZAR -> "R %.2f".format(Locale.US, value)
    }
}