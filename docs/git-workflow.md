# Git Workflow

This document outlines the exact git commands required for daily development. Follow these steps meticulously to avoid repository corruption.

## 1. Initial Setup (First Time Only)

Clone the repository and set up your tracking branches.
```bash
git clone https://github.com/FarrelPandhita/PEMLAN-KasirQu.git
cd PEMLAN-KasirQu
```

If you are the CREATE developer, checkout your branch:
```bash
git checkout feature/create
```

## 2. Daily Sync Policy (Start of Day)

Before writing any new code, you MUST pull the latest changes from `develop` into your feature branch. This ensures you are working on top of the latest integrated codebase.

```bash
# Ensure you are on your branch
git checkout feature/create

# Fetch all remote changes
git fetch origin

# Merge the latest develop into your feature branch
git merge origin/develop
```
*If a conflict occurs here, it means you edited a shared file. Resolve it immediately before proceeding.*

## 3. Developing and Committing Code

As you work, commit frequently. Write clear, descriptive commit messages.

```bash
# Stage your specific file
git add src/main/java/com/kasirqu/services/create/CreateInventoryService.java

# Commit with a meaningful message
git commit -m "feat(create): implement product insertion logic"
```

**Anti-Pattern Warning:**
Avoid using `git add .` unless you have explicitly checked `git status`. You might accidentally stage files outside your ownership (e.g., modifying `UpdateInventoryService.java` by mistake).

## 4. Pushing Code to Remote (End of Day or Feature Completion)

```bash
git push origin feature/create
```

## 5. Integrating with Develop (Pull Request Workflow)

Once your feature (or a significant chunk of it) is complete, it needs to be merged into `develop`.

1. Go to the GitHub repository URL in your browser.
2. Click "Compare & pull request" for your `feature/*` branch.
3. Set the base branch to `develop`.
4. Add a description of your changes.
5. Wait for at least one team member to review the code.
6. Once approved, perform a "Squash and Merge" or "Merge Commit" into `develop`.

## Handling Worst-Case Scenarios

**Scenario:** You accidentally committed to `develop` locally.
```bash
# Undo the commit locally but keep your changes staged
git reset --soft HEAD~1

# Stash your changes
git stash

# Switch to your correct branch
git checkout feature/create

# Apply your changes
git stash pop
```
