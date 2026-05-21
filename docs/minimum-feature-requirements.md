# Minimum Feature Requirements (MVP Scope)

This document defines the exact scope of the Minimum Viable Product (MVP) for KasirQu. Features outside this list should be deferred to post-MVP development.

## A. Inventory Management
1. **Add Product**: Form to input SKU, Name, Price, and Initial Stock.
2. **View Products (Read)**: A JTable displaying the inventory.
3. **Pagination**: The table must display exactly 10 items per page. Includes "Next" and "Previous" buttons.
4. **Search**: A text field to filter products by Name or SKU dynamically.
5. **Update Product**: Ability to edit an existing product's Name or Price.
6. **Stock Management**: Ability to manually adjust stock levels (e.g., restock +50).
7. **Delete Product**: Remove a product from the database (Hard delete is acceptable for MVP, Soft delete is a bonus).

## B. Cashier Main Interface (Cart)
1. **Add to Cart**: Select a product from inventory and add it to the active cart.
2. **Adjust Quantity**: Increase or decrease the quantity of an item in the cart.
3. **Auto-Remove**: If an item's quantity reaches 0, it is automatically removed from the cart.
4. **Clear Cart**: A single button to empty the entire cart.
5. **Cancel Purchase**: Resets the cart and returns to default state.
6. **Checkout**: Finalizes the purchase. Deducts stock in the database and creates a transaction record.
7. **Invoice/Nota Generation**: Display a simple text-based receipt (in a JTextArea or dialog) after successful checkout.

## C. Operator Session
1. **Login/Input**: A simple startup dialog asking for the Cashier/Operator's name.
2. **Session Persistence**: The operator's name is stored in memory while the app runs.
3. **Receipt Attribution**: The operator's name must appear on the generated invoice and be saved in the database transaction record.

## D. Transaction History
1. **Store Transaction**: Save the overarching transaction details (Total, Time, Operator).
2. **Store Items**: Save the specific items bought in that transaction (`transaction_items`), locking in the price at the time of purchase.
3. **View History**: A basic view allowing a manager/user to see past transactions.

## Out of Scope (Do Not Build for MVP)
- User Authentication (Passwords/Roles) - Just ask for the Operator Name.
- Export to PDF/Excel.
- Barcode Scanner Integration (Manual search/click is sufficient).
- Advanced Analytics/Graphs.
