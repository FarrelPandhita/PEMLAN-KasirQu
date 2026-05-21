# KasirQu POS System

A Desktop Cashier / Point of Sale application built as a university final project.

## Architecture Summary
KasirQu utilizes a Backend-First, Hybrid Architecture. It enforces a strict separation of concerns to prevent tangled business logic, ensure modularity, and dramatically reduce merge conflicts among team members.
- **Academic:** CRUD-based ownership.
- **Engineering:** Isolated file ownership and modular structure.

## Tech Stack
- **Language**: Java 17
- **Database**: MySQL (Connector/J)
- **GUI**: Java Swing
- **Build Tool**: Maven

## Quick Start
1. Clone the repository.
2. Run `database/masterDB.sql` in your local MySQL instance.
3. Copy `.env.example` to `.env` and update credentials.
4. Open the project in NetBeans (or IntelliJ/VSCode).
5. Compile and run via `mvn clean compile exec:java` or IDE run configurations.

## Contribution Guidelines
Please read `CONTRIBUTING.md` and the `docs/` folder for strict architectural rules.
