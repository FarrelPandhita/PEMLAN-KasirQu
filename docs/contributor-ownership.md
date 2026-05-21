# Contributor Ownership & Responsibility Matrix

To prevent blocking dependencies and merge conflicts, responsibilities are strictly siloed.

## 1. The CREATE Developer
**Responsibilities:**
- Implementing logic to add new products to inventory.
- Implementing logic to create new transactions.
- Implementing logic to insert transaction items.

**Owned Paths:**
- `src/main/java/com/kasirqu/services/create/`
- `src/main/java/com/kasirqu/repositories/create/`

## 2. The READ Developer
**Responsibilities:**
- Fetching product lists for inventory display.
- Fetching specific product details for cart additions.
- Fetching transaction history and receipts.
- Handling database pagination logic (LIMIT/OFFSET).

**Owned Paths:**
- `src/main/java/com/kasirqu/services/read/`
- `src/main/java/com/kasirqu/repositories/read/`

## 3. The UPDATE Developer
**Responsibilities:**
- Updating product details (Name, Price).
- Deducting stock upon transaction checkout.
- Managing temporary cart state updates (increasing/decreasing quantity).

**Owned Paths:**
- `src/main/java/com/kasirqu/services/update/`
- `src/main/java/com/kasirqu/repositories/update/`

## 4. The DELETE Developer
**Responsibilities:**
- Removing products from inventory.
- Clearing cart items.
- Implementing "Soft Delete" logic if decided.

**Owned Paths:**
- `src/main/java/com/kasirqu/services/delete/`
- `src/main/java/com/kasirqu/repositories/delete/`

## 5. The GUI Integrator (Facade Developer)
**Responsibilities:**
- Designing Java Swing views (Forms, Tables, Buttons).
- Orchestrating the calls to the Service Layer via Facades.
- Catching exceptions from the Service layer and displaying JOptionPane dialogs.

**Owned Paths:**
- `src/main/java/com/kasirqu/gui/`
- `src/main/java/com/kasirqu/facades/`

## Collaborative Risk Assessment
**Risk:** The Facade developer is blocked because the Service layer is not finished.
**Mitigation:** Define clear "Service Contracts" (Interface signatures). The Facade developer can mock the service responses temporarily (e.g., returning hardcoded lists) while the CRUD developers finish the database implementation.
