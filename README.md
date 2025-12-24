# MarketTrack - Mobile Stock & Profit Tracking App

## 📌 Project Description
MarketTrack is a mobile application designed for small and medium-sized retail stores to manage their inventory and monitor financial performance efficiently. The app simplifies daily operations by allowing store owners to track product stocks, register sales, and automatically calculate daily profits and losses.

This project was developed as the Final Project for the Mobile Application Development course.

## 👥 Team Members
* **Tayfun Özgür** - 220709707
* **Emre Kırdım** - 220709051

## 🛠 Tech Stack
* **Language:** Java
* **UI:** XML Layouts (Material Design)
* **Database:** Room Database (SQLite)
* **Architecture:** MVC / Android Architecture Components
* **Asynchronous Processing:** Java Executors (Background Threads)
* **Navigation:** Android Jetpack Navigation Component

## 📱 Features Implemented
### 1. Inventory Management (CRUD)
* **Add Product:** Users can add new items with Purchase Price, Selling Price, and Stock Quantity.
* **View Stock:** Real-time list of all products with their current stock levels.
* **Update Product:** Edit existing product details (Price, Name).
* **Delete Product:** Remove items from the inventory.

### 2. Sales System
* **Record Sale:** A dedicated interface to select products and enter quantities sold.
* **Auto-Stock Deduction:** System automatically reduces inventory count upon sale.
* **Validation:** Prevents selling more items than currently in stock.

### 3. Financial Reports
* **Dashboard:** Displays **Total Revenue**, **Total Cost**, and **Net Profit**.
* **Real-time Calculation:** Uses historical sales data to generate instant financial summaries.

### 4. Smart Alerts
* **Low Stock Alert:** Products with low stock (≤10 items) are visually highlighted in **RED** on the inventory screen to warn the user.

## 🚀 How to Run
1.  Clone this repository.
2.  Open the project in **Android Studio**.
3.  Sync Gradle files.
4.  Run on an Emulator or Physical Device.

## 🧪 Testing
* Includes Unit Tests for Product logic (Stock calculation, Profit formulas).