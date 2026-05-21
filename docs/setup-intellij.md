# IDE Setup Guide: IntelliJ IDEA & VSCode

While NetBeans is the primary IDE (largely due to its built-in Swing GUI designer), backend developers (CREATE, READ, UPDATE, DELETE) can comfortably use IntelliJ IDEA or VSCode.

## IntelliJ IDEA Setup

IntelliJ is excellent for pure Java backend development, offering superior refactoring and code analysis tools.

### 1. Import the Project
1. Open IntelliJ IDEA.
2. Click `Open`.
3. Navigate to the `PEMLAN-KasirQu` directory containing the `pom.xml` and click OK.
4. IntelliJ will detect it as a Maven project. Choose "Trust Project".

### 2. Configure JDK
1. Go to `File` -> `Project Structure` (`Ctrl+Alt+Shift+S`).
2. Under `Project Settings` -> `Project`, ensure the SDK is set to JDK 17 (or your team's agreed version).

### 3. Database Tool Window (Optional but Recommended)
1. Open the `Database` tool window (usually on the right sidebar).
2. Click `+` -> `Data Source` -> `MySQL`.
3. Enter your local credentials (from your `.env` file).
4. This allows you to run queries and view data directly inside IntelliJ.

### Important Note on GUI Files
If you open a NetBeans GUI file (`.form` and `.java` pair) in IntelliJ, **DO NOT** edit the auto-generated UI code. IntelliJ does not natively support NetBeans' Matisse format. You will break the GUI for the GUI developer.

---

## Visual Studio Code Setup

VSCode is lightweight and fully capable of handling this Maven project.

### 1. Required Extensions
Install the following extensions:
- **Extension Pack for Java** (by Microsoft)
- **Maven for Java**

### 2. Open Project
1. Open VSCode.
2. `File` -> `Open Folder` -> Select `PEMLAN-KasirQu`.
3. Wait for the Java Language Server to initialize. You should see a "Java Projects" view in the explorer.

### 3. Running the App
You can use the built-in run/debug lens above the `main` method, or run via Maven in the terminal:
```bash
mvn clean compile exec:java
```

### Important Note
Similar to IntelliJ, do not attempt to edit the Java Swing UI layout code in VSCode. Leave GUI layout tasks to the NetBeans developer.
