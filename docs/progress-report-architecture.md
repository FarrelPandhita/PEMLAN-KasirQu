# Progress Report: System Architecture

**Document Intended For:** Academic Lecturer / Course Assessor
**Project:** KasirQu (Desktop Cashier/POS)
**Course:** Pemrograman Lanjut (Advanced Programming)

## Executive Summary
This document outlines the architectural decisions made for the KasirQu Point of Sale application. The architecture was specifically engineered to balance the academic requirements of the assignment (CRUD-based task division) with real-world software engineering practices aimed at minimizing integration issues and Git merge conflicts.

## Architectural Paradigm: The Hybrid Model

### The Problem with Standard MVC in Academic Teams
In a standard Model-View-Controller (MVC) or standard Service-Repository pattern, multiple developers assigned to different CRUD tasks (Create, Read, Update, Delete) would simultaneously modify monolithic classes (e.g., `ProductController`, `ProductService`). This leads to severe merge conflicts, delaying development and causing frustration.

### Our Solution: Isolated File Ownership
We implemented a **Backend-First, Unidirectional Architecture** combined with **Isolated File Ownership**.

1. **Separation by Operation**: Instead of a single `InventoryService`, the logic is separated into `CreateInventoryService`, `ReadInventoryService`, etc. Each team member owns a specific directory path corresponding to their assigned CRUD operation.
2. **Facade Pattern**: To hide this backend complexity from the presentation layer, we implemented the Facade structural pattern. The GUI developer interacts only with an `InventoryFacade`, which internally delegates calls to the isolated CRUD services.
3. **Thin GUI**: The Java Swing frontend contains zero business logic and zero database connectivity. It solely handles user events and delegates them to the Facades.

## Database Normalization Strategy
The database schema (`masterDB.sql`) adheres to normalization principles. Specifically, we decoupled the transaction record from the purchased items.

- `transactions`: Stores metadata (Operator, Total, Timestamp).
- `transaction_items`: Stores the specific products bought. Crucially, it stores `price_at_purchase` to maintain historical financial integrity even if the master product price changes in the future.

## Collaboration and Version Control Strategy
- **Branching**: We utilize a simplified Git Flow. A `develop` branch serves as the integration environment. Developers work on flat feature branches (`feature/create`, `feature/read`).
- **Conflict Mitigation**: Because developers own strictly isolated files, merging feature branches into `develop` yields minimal to zero conflicts.
- **Environment Management**: Database credentials are strictly externalized using a `.env` file to accommodate different local MySQL setups among team members without causing source code conflicts.

## Conclusion
The architecture established provides a robust, scalable, and conflict-free foundation. It clearly delineates responsibilities, enforces separation of concerns, and fulfills all academic criteria while exposing the team to industry-standard patterns (Facade, Repository, Environment Configuration).
