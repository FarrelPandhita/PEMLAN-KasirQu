# Merge Strategy & Conflict Prevention

## The Goal
Zero merge conflicts during the MVP development phase.

## Why Conflicts Occur
A merge conflict happens when Git cannot automatically resolve differences in code between two commits. This typically occurs when two developers modify the *same line* of the *same file*, or if one developer deletes a file while another modifies it.

## Conflict Prevention Rules

### 1. The Golden Rule of Isolation
Never modify a file outside of your designated ownership folder (as defined in `contributor-ownership.md`). If you need a change in a file owned by someone else, ask them to make it.

### 2. Communication Before Modifying Shared Files
Models (`Product.java`, `Transaction.java`) and Configurations are shared files. 
- If you need to add a property (e.g., `discount` to `Product.java`), announce it in the group chat.
- "I am adding `discount` to Product.java and pushing to develop."
- All other members must immediately run `git pull origin develop` to synchronize.

## The Pull Request (PR) Workflow

1. **Create PR**: Open a Pull Request from your `feature/*` branch to `develop`.
2. **Review**: Another team member reviews the code. They check for:
   - Did this developer edit files outside their scope?
   - Are there any glaring SQL syntax errors?
3. **Merge**: Once approved, merge using **Squash and Merge**. This keeps the `develop` branch history clean, condensing multiple small commits (e.g., "fix typo", "fix again") into one logical feature commit.

## Resolving a Conflict (Worst-Case Scenario)

If you accidentally edit a shared file and face a conflict during `git merge origin/develop`:

1. Git will pause the merge and mark the conflicted files.
2. Open the file in your IDE (NetBeans/IntelliJ/VSCode). Look for the conflict markers:
```java
<<<<<<< HEAD
    private double newPrice;
=======
    private BigDecimal currentPrice;
>>>>>>> origin/develop
```
3. Discuss with the team which code is correct.
4. Manually edit the file to keep the correct code and delete the markers (`<<<<<<<`, `=======`, `>>>>>>>`).
5. Save the file.
6. Run `git add <file>` to mark it as resolved.
7. Run `git commit -m "Merge develop and resolve conflict in Product.java"`.
