# Coding Standards

To maintain academic clarity and readability, all code must adhere to the following standards.

## Naming Conventions

### Classes
Use **PascalCase**. Classes should be nouns.
- `ProductModel`
- `CreateInventoryService`

### Methods
Use **camelCase**. Methods should be verbs indicating the action.
- `addProduct()`
- `calculateTotal()`

### Variables
Use **camelCase**. Use descriptive names. Avoid single-letter variables except in short loops.
- `productName` (Good)
- `pn` (Bad)
- `i` (Acceptable in `for` loops)

### Database Entities/Tables
Use **snake_case** for table and column names.
- Table: `transaction_items`
- Column: `price_at_purchase`

## Anti-Patterns to Avoid

### 1. Magic Numbers
Do not use raw numbers in logic. Define them as constants.
```java
// BAD
if (cart.size() > 10) { ... }

// GOOD
private static final int MAX_CART_ITEMS = 10;
if (cart.size() > MAX_CART_ITEMS) { ... }
```

### 2. Catching Generic Exceptions
Do not catch generic `Exception` unless at the absolute top layer (GUI facade). Catch specific SQL or custom business exceptions.
```java
// BAD
try { ... } catch (Exception e) { e.printStackTrace(); }

// GOOD
try { ... } catch (SQLException e) { throw new DatabaseConnectionException("Failed to reach DB", e); }
```

### 3. Business Logic in UI Listeners
```java
// BAD (Inside a JButton ActionListener)
int stock = db.executeQuery("SELECT stock FROM products...");
if (stock > 0) { ... }

// GOOD
try {
    transactionFacade.checkout(currentCart);
} catch (InsufficientStockException e) {
    JOptionPane.showMessageDialog(this, e.getMessage());
}
```

## SQL Standards
- Always use `PreparedStatement` to prevent SQL Injection. **Never** concatenate strings to build SQL queries.
```java
// STRICTLY PROHIBITED
String query = "SELECT * FROM products WHERE name = '" + userInput + "'";

// MANDATORY
String query = "SELECT * FROM products WHERE name = ?";
PreparedStatement ps = connection.prepareStatement(query);
ps.setString(1, userInput);
```
