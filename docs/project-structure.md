# Project Structure & Ownership

This document defines the folder structure and explicit file ownership. To prevent merge conflicts, you must only edit files within your assigned domain.

## Directory Structure

```text
KasirQu/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── kasirqu/
│                   ├── config/           # Database configuration, Environment variables loader
│                   ├── models/           # POJO/Entity classes (Product, Transaction, CartItem)
│                   ├── exceptions/       # Custom business exceptions (InsufficientStockException)
│                   ├── facades/          # Facades orchestrating services (GUI entry point)
│                   │   ├── InventoryFacade.java
│                   │   ├── TransactionFacade.java
│                   │   └── CartFacade.java
│                   ├── services/         # Business logic layer
│                   │   ├── create/       # Owned by CREATE Developer
│                   │   ├── read/         # Owned by READ Developer
│                   │   ├── update/       # Owned by UPDATE Developer
│                   │   └── delete/       # Owned by DELETE Developer
│                   ├── repositories/     # Database interaction layer
│                   │   ├── create/       # Owned by CREATE Developer
│                   │   ├── read/         # Owned by READ Developer
│                   │   ├── update/       # Owned by UPDATE Developer
│                   │   └── delete/       # Owned by DELETE Developer
│                   ├── utils/            # Shared utilities (Password hashing, Validation)
│                   └── gui/              # Presentation layer (Java Swing)
│                       ├── components/
│                       ├── views/
│                       └── main/
├── docs/                                 # Project documentation
├── database/                             
│   └── masterDB.sql                      # Single source of truth for schema
├── .env.example                          # Example environment configuration
├── .gitignore                            
└── pom.xml                               # Maven configuration
```

## Ownership Rules

### Modifying Shared Resources (`models/`, `config/`, `facades/`)
Files inside `models`, `config`, and `facades` are considered shared.
- Modifications to these directories must be discussed in the team's communication channel.
- The Facade developer is responsible for assembling the facades, but depends on the Service layer contracts defined by CRUD developers.

### Modifying Isolated Resources (`services/`, `repositories/`)
- **CREATE Developer**: Exclusively modifies `services/create/` and `repositories/create/`.
- **READ Developer**: Exclusively modifies `services/read/` and `repositories/read/`.
- **UPDATE Developer**: Exclusively modifies `services/update/` and `repositories/update/`.
- **DELETE Developer**: Exclusively modifies `services/delete/` and `repositories/delete/`.
- **GUI Developer**: Exclusively modifies `gui/`.

## Do's and Don'ts

- **DO** write your specific repository implementation in your assigned folder.
- **DO NOT** edit another developer's service to fix an issue. Report the bug to them instead.
- **DO NOT** create "utility" functions inside your service if they apply globally; request an addition to the `utils/` package.
