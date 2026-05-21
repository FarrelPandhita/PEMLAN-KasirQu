# Contributing to KasirQu

## Branch Ownership
Developers must strictly work on their assigned `feature/*` branches.
- `feature/create`
- `feature/read`
- `feature/update`
- `feature/delete-gui`

## CRUD Ownership & Isolation
- **CREATE**: modifies `services/create/` and `repositories/create/`
- **READ**: modifies `services/read/` and `repositories/read/`
- **UPDATE**: modifies `services/update/` and `repositories/update/`
- **DELETE**: modifies `services/delete/` and `repositories/delete/`
- **GUI**: modifies `gui/`

## Forbidden Actions
1. **NO** monolithic classes (e.g., `ProductService.java`).
2. **NO** GUI files modification if you are not the GUI developer.
3. **NO** SQL injection vulnerability. Always use `PreparedStatement`.
4. **NO** business logic inside the Java Swing UI layer.
5. **NO** direct commits to `main` or `develop`.

## Merge Process
1. Push to your `feature/*` branch.
2. Open a Pull Request to `develop`.
3. Squash and merge.

## Daily Sync
Run `git pull origin develop` daily to keep your feature branch updated.
