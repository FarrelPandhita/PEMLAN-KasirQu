# Database Migration & Schema Policy

In collaborative development, database schemas must be carefully managed. If one developer adds a column and another doesn't know about it, the application will crash.

## The Schema Freeze
During the initial planning phase, `database/masterDB.sql` will be finalized. Once development begins, a **Schema Freeze** is enacted.

### What is a Schema Freeze?
It means you cannot arbitrarily change tables, add columns, or rename fields to suit your feature without team consensus.

## Migration Strategy (If Changes are Required)

If you discover that the current schema is insufficient (e.g., we forgot to add a `discount` column to `products`), follow this process:

### 1. Do Not Edit `masterDB.sql` Immediately
Do not just add the column locally and push.

### 2. Create a Migration Script
Create a new file in the `database/` folder describing the change.
Naming convention: `YYYYMMDD_description.sql`

Example: `database/20260521_add_discount_to_products.sql`
Content:
```sql
ALTER TABLE products ADD COLUMN discount DOUBLE DEFAULT 0.0;
```

### 3. Communicate
Notify the team in the group chat: "I've added a migration script to add a discount column. Please run this script locally."

### 4. Merge to Develop
Once the migration script is reviewed and merged into `develop`, the maintainer will update `masterDB.sql` on the `develop` branch to reflect the new cumulative state.

## Why this matters
This ensures all 4 developers have identical local database structures. If someone gets an "Unknown column" SQL error, they immediately know they missed running a migration script.
