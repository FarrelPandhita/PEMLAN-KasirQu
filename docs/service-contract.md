# Service Contract (Backend Signatures)

This document outlines the expected method signatures that the CRUD developers must implement. The Facade developer will rely on these exact signatures.

## Models (Shared)

```java
public class Product {
    private int id;
    private String skuCode;
    private String name;
    private double price;
    private int stock;
    // Getters, Setters, Constructors
}

public class CartItem {
    private Product product;
    private int quantity;
    // Getters, Setters, getSubtotal()
}
```

## Services Contracts

### CREATE Developer
```java
public class CreateInventoryService {
    // Inserts new product into database
    public void addProduct(Product product) throws DuplicateSkuException, SQLException;
}

public class CreateTransactionService {
    // Creates transaction record and returns generated ID
    public int createTransaction(String operatorName, double totalAmount) throws SQLException;
    
    // Inserts items linked to transaction ID
    public void addTransactionItems(int transactionId, List<CartItem> items) throws SQLException;
}
```

### READ Developer
```java
public class ReadInventoryService {
    // Fetches products with pagination support
    public List<Product> getProducts(int limit, int offset) throws SQLException;
    
    // Search products by name or SKU
    public List<Product> searchProducts(String keyword) throws SQLException;
    
    // Get total count for pagination math
    public int getTotalProductCount() throws SQLException;
}
```

### UPDATE Developer
```java
public class UpdateInventoryService {
    // Updates name/price of existing product
    public void updateProductDetails(Product product) throws SQLException;
    
    // Deducts stock after successful checkout
    public void deductStock(int productId, int quantityToDeduct) throws InsufficientStockException, SQLException;
}
```

### DELETE Developer
```java
public class DeleteInventoryService {
    // Deletes product entirely (or soft delete if flag is used)
    public void deleteProduct(int productId) throws ProductInUseException, SQLException;
}
```

## Note to Facade Developer
You can mock these classes while waiting for the actual implementation.
```java
public List<Product> getProducts(int limit, int offset) {
    // Temporary mock data for UI testing
    return Arrays.asList(new Product(1, "SKU01", "Mock Item", 10000, 50));
}
```
