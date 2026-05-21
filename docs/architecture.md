# System Architecture

## Core Architectural Principles
KasirQu utilizes a Backend-First, Hybrid Architecture. It enforces a strict separation of concerns to prevent tangled business logic, ensure modularity, and dramatically reduce merge conflicts among team members.

### Strict Data Flow
The data flow must adhere strictly to the following unidirectional pattern:

`GUI` -> `Facade / Coordinator Layer` -> `CRUD Service Layer` -> `Repository Layer` -> `Database`

### Prohibited Patterns (Anti-Patterns)
- **Direct Database Access from GUI**: `GUI` -> `SQL` (Strictly Prohibited)
- **Skipping Service/Facade Layers**: `GUI` -> `Repository` (Strictly Prohibited)
- **Business Logic in Presentation**: Performing stock validation, calculations, or data formatting inside Java Swing event listeners.

## The Hybrid Architecture

### Academic Requirement (CRUD Ownership)
University requirements dictate that developers own specific CRUD operations (CREATE, READ, UPDATE, DELETE).

### Engineering Solution (Isolated File Ownership)
A naive approach would be creating a monolithic `ProductService.java` where all four developers write their respective CRUD methods. This will inevitably result in severe merge conflicts, specifically on imports, class scope variables, and method boundaries.

To solve this, we use Isolated File Ownership:
Instead of one service, we have four distinct services.
- `CreateInventoryService.java`
- `ReadInventoryService.java`
- `UpdateInventoryService.java`
- `DeleteInventoryService.java`

Each developer only touches their designated service file, eliminating Git conflicts.

## Layer Definitions

### 1. Presentation Layer (GUI)
Responsible only for rendering the Java Swing UI and passing user inputs to the Facade layer. It should remain "dumb" regarding business rules.

### 2. Facade / Coordinator Layer
The Facade pattern is used to orchestrate complex operations that require multiple CRUD services. The GUI interacts exclusively with facades.
**Examples:**
- `InventoryFacade`: Combines Create, Read, Update, Delete inventory services.
- `CartFacade`: Manages temporary cart state.
- `TransactionFacade`: Handles the checkout process.

**Pseudo-Flow Example: Checkout**
```java
// Inside TransactionFacade.java
public void checkout(Cart cart, String operatorName) {
    // 1. ReadInventoryService: Validate current stock
    // 2. CreateTransactionService: Create transaction record
    // 3. CreateTransactionItemService: Insert items
    // 4. UpdateInventoryService: Deduct stock
    // 5. CartFacade: Clear cart
}
```

### 3. Service Layer
Contains the core business logic. Validates inputs, formats data, and throws exceptions if rules are violated. Services are strictly separated by operation type (Create, Read, Update, Delete).

### 4. Repository Layer
Handles direct interaction with the MySQL database using standard JDBC (`java.sql.*`). Contains SQL queries. Separated similarly to services to ensure modularity.

### 5. Database Layer
The MySQL database schema, represented by the source of truth file `masterDB.sql`.

## Benefits of this Architecture
1. **Zero Conflict**: Developers work in completely isolated files.
2. **Clear Contracts**: The GUI developer knows exactly what methods the Facade will expose without worrying about the underlying database logic.
3. **Testability**: Isolated services can be unit-tested independently without mocking massive monolithic classes.
