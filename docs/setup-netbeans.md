# IDE Setup Guide: Apache NetBeans

As NetBeans is the primary IDE for this project (especially for Java Swing GUI design), follow these steps to ensure a smooth onboarding process.

## Prerequisites
1. **JDK 17 or higher** installed and environment variables (`JAVA_HOME`) configured.
2. **Apache NetBeans 16+** installed.
3. **MySQL Server** running locally.
4. **Git** installed.

## 1. Cloning the Repository
While you can clone via NetBeans, using the terminal is highly recommended to ensure you get the exact branches.
```bash
git clone https://github.com/FarrelPandhita/PEMLAN-KasirQu.git
```

## 2. Opening the Project in NetBeans
1. Open NetBeans.
2. Go to `File` -> `Open Project...`
3. Navigate to the cloned `PEMLAN-KasirQu` folder.
4. NetBeans should recognize it as a Maven Project (indicated by a small 'ma' icon). Click `Open Project`.

## 3. Resolving Dependencies
1. Right-click the project in the Projects pane.
2. Select `Build with Dependencies` or `Clean and Build`.
3. Maven will automatically download the required `mysql-connector-j` driver defined in the `pom.xml`.

## 4. Setting Up the Database
1. Open your MySQL client (e.g., phpMyAdmin, DBeaver, or MySQL Workbench).
2. Create a database: `CREATE DATABASE db_kasir_dev;`
3. Execute the SQL script found in `database/masterDB.sql` to generate the tables.
4. Copy `.env.example` to `.env` in the project root and configure your local database credentials.

## 5. GUI Editor Tips (Matisse)
- The GUI developer will use NetBeans' drag-and-drop builder (Matisse).
- **Warning to non-GUI developers:** Do NOT manually edit the `.form` files or the auto-generated code blocks in the `.java` GUI files (marked with `//GEN-BEGIN` and `//GEN-END`). Modifying these outside of the NetBeans designer will permanently break the GUI file.
- If you are a backend developer, only write code inside your isolated `services/` and `repositories/` packages.
