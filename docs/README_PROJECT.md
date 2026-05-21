# KasirQu Project Documentation

## Project Vision
KasirQu is a Desktop Cashier / Point of Sale (POS) application. This project serves as a university final project emphasizing a balance between academic clarity, practical engineering, collaborative parallel development, maintainability, and minimizing merge conflicts.

## Project Scope
This is not an enterprise-scale application. The architecture is intentionally designed to avoid overengineering while maintaining robust, deterministic, and modular code ownership.

### MVP Features

#### A. Inventory Management
- Add product
- Update product
- Delete product
- View product list
- Pagination (10 items per page)
- Next / Previous page
- Search product
- Stock management
- Rename product
- Price update

#### B. Cashier Main Interface
- Add item to cart
- Increase quantity
- Decrease quantity
- Remove item when qty = 0
- Clear all cart items
- Cancel purchase
- Checkout transaction
- Generate invoice/nota

#### C. Operator Session
- Input cashier/operator name
- Persist current operator session in memory
- Operator name displayed on invoice

#### D. Transaction History
- Store completed transaction
- Store purchased items
- Maintain purchase history

#### E. Recommended Additional Features
- Low stock alert
- Invoice number generation
- Stock validation
- Recent transaction list
- Soft delete strategy

## Technology Stack
- **Language**: Java Native (No Spring, No external frameworks)
- **Build Tool**: Maven
- **Database**: MySQL
- **Driver**: MySQL Connector/J
- **GUI**: Java Swing (Backend-first integration)
- **Primary IDE**: NetBeans
- **Compatible IDEs**: IntelliJ IDEA, VSCode

## Development Team & Academic Requirements
The team consists of 4 collaborators. Task division strictly aligns with CRUD ownership to fulfill academic requirements:
- CREATE Developer
- READ Developer
- UPDATE Developer
- DELETE Developer

One developer is additionally responsible for GUI integration, ensuring business logic is entirely decoupled from the presentation layer.

## Architecture Philosophy: Hybrid Model
To satisfy both academic CRUD assignments and engineering best practices (low merge conflicts), KasirQu employs a Hybrid Architecture:
- **Academic**: CRUD-based ownership.
- **Engineering**: Isolated file ownership and modular structure. No shared monolithic service classes (e.g., `ProductService.java`). Instead, logic is split into isolated services (`CreateInventoryService.java`, `ReadInventoryService.java`, etc.).

## Navigation
Please refer to the subsequent documentation in the `docs/` directory for detailed guidelines on architecture, database, workflows, and standards.
