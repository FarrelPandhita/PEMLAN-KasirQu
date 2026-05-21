# Branching Strategy

This project uses a simplified but strict branching model optimized for collaborative parallel development.

## Core Branches

### `main`
- **Purpose**: Represents the stable, production-ready release.
- **Rule**: STRICTLY NO DIRECT COMMITS.
- **Rule**: Code only enters `main` via a controlled merge from `develop` at the end of a sprint or milestone.

### `develop`
- **Purpose**: The primary integration branch. All features merge here.
- **Rule**: Contains the latest integrated features. Must always be in a compile-able state.
- **Rule**: NO DIRECT COMMITS. Must merge from feature branches.

## Feature Branches
Developers work exclusively on feature branches originating from `develop`.

### Naming Convention
Feature branches must be named according to the academic task division:
- `feature/create`
- `feature/read`
- `feature/update`
- `feature/delete`
- `feature/gui`

**Why no nested branches?** (e.g., `feature/create/add-product`)
Since the project is divided strictly by CRUD responsibility rather than individual granular features, a single flat branch per developer minimizes git overhead and confusion for a small team. The developer owns their feature branch for the duration of the MVP.

## The Problem with Naive Branching
**Scenario:**
1. Developer A works on `feature/create` and modifies a monolithic `ProductService.java`.
2. Developer B works on `feature/update` and modifies the exact same `ProductService.java`.
3. Both developers attempt to merge their branches into `develop`.

**Result:** Severe Git Merge Conflicts. The team spends hours manually resolving curly braces and import statements instead of writing code.

**The Solution:**
Because our architecture uses Isolated File Ownership (`CreateInventoryService.java` vs `UpdateInventoryService.java`), developers operating on their respective feature branches will naturally avoid modifying the same files.

## Summary of Branch Rules
1. Never push directly to `main`.
2. Never push directly to `develop`.
3. Work only on your designated `feature/*` branch.
4. Merge `feature/*` to `develop` only when the code is stable and tested locally.
