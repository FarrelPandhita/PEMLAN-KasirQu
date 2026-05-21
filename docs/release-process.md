# Release Process

This document outlines the steps required to package the KasirQu application for final submission and deployment.

## Pre-Release Checklist
Before initiating a release, ensure the following conditions are met:
1. All feature branches (`feature/create`, `feature/read`, etc.) are merged into `develop`.
2. No remaining merge conflicts exist on `develop`.
3. The application runs flawlessly when pulling from `develop`.
4. The database schema in `database/masterDB.sql` matches the exact schema used in production testing.

## Step 1: Merging `develop` to `main`
The `main` branch is strictly reserved for the final, stable build.
```bash
git checkout main
git merge develop
git push origin main
```

## Step 2: Building the Executable JAR
Since this is a Java Swing application, it must be compiled into a single runnable "Fat JAR" containing all dependencies (specifically the MySQL driver).

**Maven Assembly Plugin**
Ensure the `pom.xml` contains the `maven-assembly-plugin` configured to build a fat jar with the correct `mainClass`.

Run the following command:
```bash
mvn clean compile assembly:single
```
This will generate a file in the `target/` directory named something like `KasirQu-1.0-jar-with-dependencies.jar`.

## Step 3: Deployment Package
Create a `.zip` file for university submission containing:
1. `KasirQu-1.0-jar-with-dependencies.jar` (The executable)
2. `database/masterDB.sql` (For the lecturer to set up the DB)
3. `README.txt` (A small guide instructing the lecturer to run the SQL script and configure the `.env` file).
4. `.env.example`

## Step 4: Tagging the Release
Tag the final commit on GitHub to mark the submission version.
```bash
git tag -a v1.0.0 -m "Final Submission Build"
git push origin v1.0.0
```
