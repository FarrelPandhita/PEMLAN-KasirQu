# Database Architecture

## Source of Truth
The definitive source of truth for the database schema is `database/masterDB.sql`. 
Any local development database must be generated directly from this file.

## Schema Design and Normalization

To support a POS system accurately, the database must separate the concept of a "Transaction Record" from the "Purchased Items". Relying on a single table for transactions violates normalization rules and prevents accurate historical tracking.

### Recommended Tables Strategy

#### 1. `products`
Stores current inventory.
- `id` (PK)
- `sku_code` (Unique)
- `name`
- `price`
- `stock`
- `created_at`
- `updated_at`

#### 2. `transactions`
Records the overarching metadata of a single checkout event.
- `id` (PK)
- `invoice_number` (Unique)
- `operator_name`
- `total_amount`
- `created_at`

#### 3. `transaction_items`
Records the specific items purchased during a transaction.
- `id` (PK)
- `transaction_id` (FK to transactions.id)
- `product_id` (FK to products.id)
- `quantity`
- `price_at_purchase` (Crucial for historical accuracy if product prices change)
- `subtotal`

### Rationale for `transaction_items`
If product prices are updated in the `products` table, older transactions must still reflect the price *at the time of purchase*. Normalizing into `transaction_items` and storing the `price_at_purchase` guarantees historical financial integrity.

## The Checkout Data Flow (Mutation)
When a checkout occurs, multiple tables are affected in a single transactional context:
1. Insert record into `transactions`.
2. Retrieve the generated `transaction.id`.
3. Iterate over the cart, inserting records into `transaction_items` linked to the `transaction_id`.
4. Update the `stock` in `products` (Current Stock - Quantity).

## Environment Separation (.env Strategy)
Credentials must never be hardcoded in the Java source. 

**Why?**
- Different developers have different MySQL passwords (e.g., `root` vs `password`).
- Hardcoding requires modifying Java files simply to connect to the database, causing immediate git conflicts.

**Solution:**
Create a `.env` file in the project root.
```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=db_kasir_dev
DB_USER=root
DB_PASSWORD=123456789
```
A `.env.example` file is provided in the repository. Developers must copy it, rename it to `.env`, and fill in their local credentials. The `.env` file is explicitly ignored in `.gitignore`.

## Schema Freeze and Migration Policy
- The initial schema defined in `masterDB.sql` will undergo a "Schema Freeze" once development begins.
- If changes are necessary (e.g., adding a new column), do not directly edit `masterDB.sql` during a feature branch silently.
- Instead, propose a migration script (e.g., `alter_products_add_category.sql`) and communicate with the team. Once approved, the change is applied to `masterDB.sql` on the `develop` branch.
