# GUI Integration Contract

The GUI (Graphical User Interface) is the presentation layer. It must be as thin as possible, acting only as a bridge between the User and the Facade Layer.

## Strict Rules for GUI Developer

1. **No Database Connections**: The GUI must never instantiate a `java.sql.Connection`.
2. **No Business Logic**: The GUI must not calculate subtotals, validate stock limits, or determine if an item exists.
3. **Use Facades Only**: The GUI should only hold references to Facade classes (`InventoryFacade`, `TransactionFacade`). It should not interact with Service classes directly.
4. **State Management**: Temporary states (like items in the current cart) can be managed via the `CartFacade` or within a dedicated GUI state object, but NOT in a database table.

## Exception Handling in GUI

The backend services will throw exceptions (e.g., `InsufficientStockException`, `SQLException`). The GUI is responsible for catching these exceptions and displaying user-friendly messages.

**Example Implementation:**
```java
private void btnCheckoutActionPerformed(java.awt.event.ActionEvent evt) {
    String operatorName = txtOperatorName.getText();
    
    if (operatorName.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Operator name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Attempt checkout via Facade
        transactionFacade.checkout(currentCart, operatorName);
        
        // On success
        JOptionPane.showMessageDialog(this, "Transaction Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        currentCart.clear();
        refreshCartTable();
        
    } catch (InsufficientStockException e) {
        // Handled business logic error
        JOptionPane.showMessageDialog(this, e.getMessage(), "Stock Error", JOptionPane.WARNING_MESSAGE);
        
    } catch (Exception e) {
        // Catch-all for database or unexpected errors
        JOptionPane.showMessageDialog(this, "System Error: " + e.getMessage(), "Critical Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace(); // Log to console for debugging
    }
}
```

## GUI Threading (SwingWorker)
For operations that take time (like fetching a massive list of products), do not block the Event Dispatch Thread (EDT). Consider using `SwingWorker` or at minimum, ensuring the UI shows a loading state if delays occur. (Optional for MVP, but recommended).
