# 🏥 GH-Health-System
### Ghana Smart Health Service Operations Optimizer

A semester project for **DCIT 204/308 – Data Structures and Algorithms I & II** at the **University of Ghana**.

This project aims to build a Smart Health Service Operations Optimizer for a Ghanaian healthcare environment. The system demonstrates the implementation of custom data structures and algorithms, database integration, performance analysis, and correctness testing.

---

# 📁 Project Structure

```
GH-Health-System/
│
├── data/                      # CSV datasets used to seed the database
│
├── diagrams/                  # Project diagrams
│   ├── Architecture/
│   ├── ERD/
│   ├── Flowcharts/
│   └── UML/
│
├── docs/                      # Project documentation
│   └── MeetingNotes/
│
├── report/                    # Final report resources
│   ├── Figures/
│   ├── Graphs/
│   ├── Screenshots/
│   └── TraceTables/
│
├── results/                   # Performance experiment results
│   └── graphs/
│
├── src/
│   └── main/
│       └── com/ug/optimizer/
│
│           ├── algorithms/
│           ├── database/
│           ├── datastructures/
│           ├── model/
│           ├── performance/
│           ├── services/
│           ├── ui/
│           └── utils/
│
├── test/                      # Unit tests
│
└── videos/                    # Demo videos
```

---

# 📦 Package Guide

## `algorithms/`

Contains all algorithm implementations.

```
algorithms/
│
├── searching/
├── sorting/
├── graph/
├── greedy/
└── dynamicprogramming/
```

Only algorithm implementations should be placed here.

---

## `database/`

Contains everything related to the database.

Examples:

- Database connection
- SQL operations
- Data loading
- Repository classes

---

## `datastructures/`

Contains all custom data structures required for the project.

```
datastructures/

array/
bst/
btree/
deque/
disjointset/
graph/
hashtable/
heap/
linkedlist/
priorityqueue/
queue/
redblacktree/
stack/
```

Each data structure should have its own package.

Example:

```
linkedlist/

LinkedList.java
Node.java
LinkedListIterator.java
```

**Do NOT place multiple data structures inside one package.**

---

## `model/`

Contains the application's data models.

Examples:

- Location
- Road
- ServiceRequest
- Resource
- AlgorithmRun

Models should only contain attributes and related methods.

No algorithms belong here.

---

## `performance/`

Contains benchmarking and performance analysis code.

Examples:

- Runtime measurement
- Memory usage
- CSV export
- Benchmark utilities

---

## `services/`

Contains the business logic of the application.

Examples:

- SchedulingService
- RoutingService
- OptimizationService

Services coordinate between the UI, database, data structures, and algorithms.

---

## `ui/`

Contains the user interface.

For this project, this will primarily be the console menu.

Examples:

- Main Menu
- Menu navigation
- User input handling

---

## `utils/`

Contains reusable helper classes.

Examples:

- Constants
- Validators
- CSV Readers
- Logger
- Utility methods

---

# 🧪 Tests

All unit tests should be placed inside:

```
test/java/
```

Organize tests the same way as the source code.

Example:

```
test/

datastructures/
algorithms/
database/
services/
```

---

# 📊 Documentation

All project documentation should be stored inside the `docs/` directory.

Examples:

- Meeting notes
- Architecture
- Planning documents
- Requirements

---

# 📈 Performance Results

Store benchmarking outputs inside:

```
results/
```

Graphs generated from benchmarking should be placed inside:

```
results/graphs/
```

---

# 📑 Report Resources

Everything used in the final report belongs inside:

```
report/
```

Including:

- Screenshots
- Figures
- Trace tables
- Performance graphs

---

# 🌳 Git Workflow

## Never commit directly to `main`.

Every member should:

1. Pull the latest changes.
2. Create a new feature branch.
3. Commit changes to their branch.
4. Push the branch.
5. Open a Pull Request.
6. Wait for review before merging.

---

## Branch Naming Convention

Use descriptive branch names.

Examples:

```
feature/database
feature/linked-list
feature/stack
feature/queue
feature/heap
feature/hash-table
feature/bst
feature/red-black-tree
feature/graph
feature/searching
feature/sorting
feature/greedy
feature/dynamic-programming
feature/testing
feature/report
```

---

# 💬 Commit Message Convention

Use clear commit messages.

Examples:

```
feat: implement linked list

feat: add binary search

fix: correct heap insertion

docs: update README

test: add queue unit tests

refactor: improve graph package
```

---

# ⚠️ Team Rules

- Do **not** modify another member's work without discussing it.
- Keep each feature in its assigned package.
- Write clean, well-documented code.
- Test your code before creating a Pull Request.
- Keep commits focused on a single task.
- Avoid committing generated files or temporary IDE files.

---

# 👥 Contributors

Team members will be listed here as the project progresses.

---

# 📜 License

This project was developed solely for academic purposes as part of the DCIT 204/308 Data Structures and Algorithms course at the University of Ghana.
