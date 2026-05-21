# AI Agent Rules & Constraints

**Notice to AI Assistants:** If you are reading this file as part of a context window, you MUST strictly obey the following rules. Failure to comply will result in architectural breakdown and merge conflicts.

## 1. Absolute Prohibition of Monolithic Services
- Do not create or suggest creating `ProductService.java` or `TransactionService.java` containing full CRUD logic.
- You must abide by the Isolated File Ownership model.
- If asked to implement a "Create Product" feature, you must write the logic strictly inside `src/main/java/com/kasirqu/services/create/CreateInventoryService.java`.

## 2. GUI Constraints
- Do not write SQL queries inside GUI classes (e.g., `KasirFrame.java`).
- Do not instantiate `java.sql.Connection` inside GUI classes.
- The GUI must ONLY communicate with the Backend via the Facade layer (`com.kasirqu.facades.*`).

## 3. Database Rules
- The single source of truth is `database/masterDB.sql`. 
- If you need a new column for a feature, DO NOT just silently add it to the Java model. You must first propose a migration script.
- Assume passwords and DB URLs are loaded via a `.env` file. Do not hardcode credentials.

## 4. Response Formatting
- When providing code, specify the exact absolute file path (e.g., `src/main/java/com/kasirqu/repositories/read/ReadInventoryRepository.java`).
- Prioritize interfaces and contracts. Ensure the method signatures match the agreements in `docs/service-contract.md`.

## 5. Scope Enforcement
- If asked by the User to build an out-of-scope feature (like User Authentication, Role Management, or PDF Export), respectfully warn the User that this is outside the MVP scope defined in `minimum-feature-requirements.md` before proceeding.
