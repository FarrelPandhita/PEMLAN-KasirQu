# 🛒 KasirQu POS (Point of Sale) System

A robust, enterprise-grade Desktop Cashier / Point of Sale application built as a high-quality collaborative project. KasirQu is engineered with a strict **Backend-First, Hybrid Architecture** ensuring absolute separation of concerns, transactional atomicity, and smooth modular scalability.

---

## 🏗️ Architecture Design & Design Patterns

KasirQu is structured to prevent tangled business logic, secure database transactions, and avoid merge conflicts.

```
       [ Client GUI (Swing Page / Dialogs) ]
                        │
                        ▼
            [ Facade Contract Layer ]
         (InventoryFacade, TransactionFacade)
                        │
                        ▼
         [ Service Layer (Business Logic) ]
          (Create / Read / Update / Delete)
                        │
                        ▼
    [ Repository Layer (SQL Database Operations) ]
                        │
                        ▼
           [ MySQL Database Instance ]
```

*   **Facade Pattern**: The GUI components (such as `ItemListPage` and `KasirPage`) communicate **exclusively** with Service Facades (`InventoryFacade`, `CartFacade`, `TransactionFacade`) rather than directly accessing database repositories or lower-level services.
*   **Repository Pattern**: Data persistence logic is strictly separated from business use cases. 
*   **Fail-Fast Configuration**: Enforces a secure, zero-hardcode configuration policy using `.env` at the root directory. If the environment is incomplete, the application halts immediately with clear diagnostics.
*   **Transactional Atomicity**: All checkout operations run inside SQL transactions. If any step fails (e.g. stock reduction or log entry), the database instantly rolls back to guarantee data integrity.

---

## 🛠️ Tech Stack & Requirements

*   **Language**: Java 17+
*   **GUI Framework**: Java Swing (Custom premium flat theme styles)
*   **Database**: MySQL 8.0+
*   **Build Tool**: Maven

---

## ⚙️ Quick Start & Local Setup

### 1. Database Initialization
1. Open your MySQL client (e.g., DBeaver, MySQL Workbench, CLI).
2. Import and execute the base database script:
   *   [database/masterDB.sql](database/masterDB.sql)
3. **CRITICAL STEP (Database Migrations)**:
   Ensure you run the database schema updates found in the migrations folder to add required columns like `nama_operator` inside the `transaksi` table:
   *   [database/migrations/V2_add_nama_operator.sql](database/migrations/V2_add_nama_operator.sql)

### 2. Configure Environment Variables
Copy `.env.example` into a new file named `.env` at the root directory of the project, and adjust the values to match your local database settings:
```ini
DB_HOST=localhost
DB_PORT=3306
DB_NAME=db_kasir_dev
DB_USER=root
DB_PASSWORD=your_mysql_password
```
*(Note: If `.env` is missing or incomplete, the application will crash safely at boot with a specific configuration alert).*

### 3. Launching the Application on Windows

If Maven (`mvn`) is registered in your system environment variable path, simply run:
```bash
mvn clean compile exec:java -D"exec.mainClass=com.kasirqu.Main"
```

If the terminal reports that `'mvn' is not recognized`, use the NetBeans bundled Maven executable directly inside PowerShell:
```powershell
& "C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" clean compile exec:java "-Dexec.mainClass=com.kasirqu.Main"
```

---

## ⚡ Main Core Features

1.  **Daftar Item Toko (Inventory List)**:
    *   **Dynamic Categories**: The categories are dynamically queried and loaded from the DB rather than hardcoded.
    *   **Real-time Filters**: Search items by name, filter by dynamic Category name, or look up stock status (Cukup / Menipis / Habis).
    *   **Full CRUD**: Add, Edit, and Soft Delete items directly through unified popups.
2.  **Kasir POS (Cashier & Cart)**:
    *   **Interactive Cart**: Add items by scanning or typing the barcode/item code. Update quantities or remove items instantly.
    *   **Automated Financial Calculations**: Automatically computes the Subtotal, Pajak (11%), and Grand Total.
    *   **Atomicity-secured Checkout**: Generates unique invoice numbers (e.g. `TRX-20260530-0001`), registers log entries inside the stock movement logs, and decrements available product stock automatically upon a successful purchase.

---

## 🤝 Contribution Guidelines
Development follows strict code organization principles. Please review the [CONTRIBUTING.md](CONTRIBUTING.md) file and checkout structural guides inside the `docs/` folder. All feature contributions should be built on branch `develop` and verified through integration testing.
