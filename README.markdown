# Currency Converter

**Currency Converter** is an Android app for currency conversion with
real-time rate updates, exchange functionality, and transaction history.
Built with Clean Architecture, Jetpack Compose, and modern Android libraries.

---

## 🧰 Key Features

* **Currencies Screen**:
  * Displays all currencies with flags, names, codes, and balances.
  * Updates rates every second (simulated via RemoteRatesService).
  * Supports currency selection and amount input for exchange.
* **Exchange Screen**:
  * Shows buy/sell amounts, exchange rate, and account balances.
  * Performs currency exchange and updates account balances.
* **Transactions Screen**:
  * Lists all exchange transactions with details (amounts, date, time).
* Persistent storage using Room for accounts and transactions.
* Navigation via Jetpack Compose Navigation.

---

## 📁 Project Structure

```
currencyconverter/
├── data/                 # Data layer
│   ├── dataSource/       # Data sources
│   │   ├── remote/       # Simulated remote API
│   │   │   ├── dto/      # Data transfer objects
│   │   ├── room/         # Room database
│   │   │   ├── account/  # Account DAO and DBO
│   │   │   ├── converter/# Type converters
│   │   │   ├── transaction/ # Transaction DAO and DBO
│   ├── repository/       # Repository implementations
├── di/                   # Dependency injection (Hilt)
├── domain/               # Business logic layer
│   ├── entity/           # Domain models (Account, Currency, Rate)
│   ├── mapper/           # DTO ↔ Domain converters
│   ├── repository/       # Repository interfaces
├── ui/                   # UI layer (Jetpack Compose)
│   ├── currencies/       # Currencies screen and ViewModel
│   ├── exchange/         # Exchange screen and ViewModel
│   ├── navigation/       # Navigation setup
│   ├── theme/            # App theming
│   ├── transactions/     # Transactions screen and ViewModel
├── utils/                # Utility functions (formatters, etc.)
```

---

## ⚙️ Technologies Used

* **Kotlin**, **Jetpack Compose** for UI.
* **Room** for local storage.
* **Dagger Hilt** for dependency injection.
* **Navigation Compose** for routing.
* **Kotlin Coroutines + Flow** for async operations and state.
* **MVVM + Clean Architecture** for modular design.

---

## 📊 Technical Highlights

* **Dynamic Rate Updates**:
  * Rates refresh every second using coroutines with retry on errors.
* **Filtered Currency List**:
  * Shows only currencies with sufficient balance in amount input mode.
* **Unified UI Design**:
  * Consistent Card-based UI for currencies, exchange, and transactions.
* **State Persistence**:
  * Uses SavedStateHandle in ViewModel to restore state after process death.

---

## 📃 Code Style and Conventions

* Clear separation of UI and business logic via MVVM.
* Centralized exchange logic in ExchangeViewModel.
* State and events managed with StateFlow.
* ViewModels are free of UI or Android context dependencies.

---

## 🚀 Getting Started

1. Install Android Studio (latest stable version recommended).
2. Clone the repository:

   ```bash
   git clone https://github.com/[YourUsername]/CurrencyConverter
   ```
3. Build and run the app:

   ```bash
   ./gradlew assembleDebug
   ```
4. Main entry point: `CurrenciesScreen`.

---

## 📸 Screenshots

Below are screenshots showcasing the app's key screens:

| Currencies Screen 1 | Currencies Screen 2 | Exchange Screen | Transactions Screen |
|---------------------|---------------------|-----------------|---------------------|
| <img src="screenshots/currencies_screen1.jpg" width="200"/> | <img src="screenshots/currencies_screen2.jpg" width="200"/> | <img src="screenshots/exchange_screen.jpg" width="200"/> | <img src="screenshots/transactions_screen.jpg" width="200"/> |


---

## 📌 Notes

* All data is stored locally using Room, linked to user accounts.
* No external API is used; rates are simulated via RemoteRatesServiceImpl.