# Troubleshooting Guide

This document catalogs common errors encountered during development and their respective solutions.

## Database Connectivity Errors

### Error: `java.sql.SQLException: Access denied for user 'root'@'localhost'`
**Cause:** The credentials in your `.env` file do not match your local MySQL server.
**Fix:** Open `.env` and verify `DB_USER` and `DB_PASSWORD`. Ensure your MySQL server is actually running.

### Error: `java.sql.SQLSyntaxErrorException: Unknown column 'price_at_purchase' in 'field list'`
**Cause:** Your local database schema is outdated. A team member added a column via a migration, but you haven't applied it.
**Fix:** Check the `database/` folder for recent migration scripts and run them in your SQL client, or drop your database and re-import the latest `masterDB.sql`.

### Error: `java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver`
**Cause:** The MySQL JDBC driver is missing from the classpath.
**Fix:** If using Maven, run `mvn clean install` to download dependencies. If running a built JAR, ensure it was built as a "fat jar" (with dependencies).

## Git Errors

### Error: `error: Your local changes to the following files would be overwritten by merge`
**Cause:** You modified a file (e.g., `Product.java`) and are trying to pull changes from `develop` where someone else also modified it.
**Fix:** Stash your changes first.
```bash
git stash
git pull origin develop
git stash pop
```
Resolve any conflicts that arise after popping the stash.

### Error: `fatal: refusing to merge unrelated histories`
**Cause:** Usually occurs if the remote repository was initialized with a README after the local repository was created.
**Fix:** Run `git pull origin develop --allow-unrelated-histories`.

## UI / IDE Errors

### Error: The GUI form looks completely different in NetBeans than it does at runtime.
**Cause:** Hardcoded absolute positioning or resizing issues across different screen resolutions.
**Fix:** Ensure you are using layout managers (like `GridBagLayout` or `BorderLayout`) in NetBeans rather than "Free Design" (AbsoluteLayout) where possible.

### Error: `NullPointerException` when clicking a JButton
**Cause:** The GUI attempted to call a Facade method, but the Facade was not instantiated.
**Fix:** Check the constructor of your View class and ensure `this.inventoryFacade = new InventoryFacade();` (or similar dependency injection) is executed before the GUI renders.
