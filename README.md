<div align="center">

# Maze

**Java 17 desktop app for generating, rendering and solving perfect mazes.**

Eller's algorithm · bidirectional DFS solver · Swing GUI · Maven + Makefile · JUnit 5

![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/build-Maven-C71A36?logo=apachemaven&logoColor=white)
![JUnit 5](https://img.shields.io/badge/tests-JUnit%205-25A162?logo=junit5&logoColor=white)
![UI](https://img.shields.io/badge/UI-Java%20Swing-orange)

</div>

---

Maze is a desktop application that builds **perfect mazes** (exactly one path between any two
cells), renders them on a fixed 500×500 canvas and finds the shortest route between two points.
Mazes can be generated on the fly or loaded from / saved to a text file.

> The full project brief (the original School 21 task) is available in
> [`README_RUS.md`](README_RUS.md).

## Features

- **Generate** a perfect maze of any size from 1×1 up to 50×50 using **Eller's algorithm**.
- **Solve** any maze shown on screen with a **bidirectional DFS**; the route is drawn as a
  2-pixel line through the centres of the cells.
- **Load / save** mazes to a plain-text file format.
- **Interactive Swing GUI** — pick rows/columns, generate, select start/end points and solve.
- **Unit-tested** generation and solving modules (JUnit 5 + Mockito).

## Algorithms

| Concern | Algorithm | Notes |
| --- | --- | --- |
| Maze generation | **Eller's algorithm** | Produces a perfect maze: no isolated areas, no loops. Row-by-row with disjoint sets. |
| Maze solving | **Bidirectional DFS** | Two searches expand from the start and the goal until their fronts meet, then the paths are joined. |
| Data structure | **Disjoint sets** | Tracks which cells belong to the same set while carving passages. |

## Tech stack

- **Language:** Java 17 (Google Java Style)
- **UI:** Java Swing / AWT
- **Build:** Maven (assembly plugin → runnable fat JAR), wrapped by a GNU-style `Makefile`
- **Tests:** JUnit 5, Mockito

## Getting started

### Requirements

- JDK 17 or newer
- Maven (only needed for the Maven/`make` paths)

### Build and run with Maven

```bash
mvn clean package
java -jar target/maze-1.0-jar-with-dependencies.jar
```

### Build and run with Make

```bash
make            # build + install into ../install
make install    # build and copy the runnable JAR to ../install
java -jar ../install/maze-1.0-jar-with-dependencies.jar
```

### Makefile targets

| Target | Description |
| --- | --- |
| `all` | Check Java, clean, compile and install |
| `compile` | Package the project with Maven |
| `install` | Copy the runnable JAR into `../install` |
| `uninstall` | Remove the installation directory |
| `dist` | Create `dist/maze-1.0.tar.gz` |
| `test` | Run the unit tests with Maven |
| `dvi` | Build the TeXinfo documentation (if `doc/*.texi` is present) |
| `clean` | Remove build artifacts |

## Using the app

1. Launch the app and choose **generate** or **load from file**.
2. For generation, set the number of rows and columns and press **Set**, then **Generate**.
3. Click two cells on the grid to mark the **start** and **end** points.
4. Press **Solve** to draw the shortest route.
5. Use **Save to file** to export the current maze.

The maze is rendered in a 500×500 field with 2-pixel walls.

## Maze file format

The first line holds the dimensions (`rows columns`). Every following line describes a single cell
with four wall flags — **top, right, bottom, left** — where `1` means a wall and `0` means a passage.
The grid is written row by row, so there are `rows × columns` cell lines:

```
2 2
1 0 0 1
1 1 0 0
0 1 1 1
0 1 1 1
```

## Project structure

```
src/
├── main/java/s21/team/
│   ├── Maze.java                     # entry point + Swing UI wiring
│   ├── fileloader/                   # load / save mazes to file
│   ├── generator/EllersGen.java      # Eller's maze generation
│   ├── gui/MazeGridPanel.java        # grid rendering and point selection
│   ├── solver/BiDFSSolver.java       # bidirectional DFS solver
│   └── util/                         # Cell, DisjointSets, file helpers
└── test/java/s21/team/               # JUnit 5 tests for generator and solver
```

## Testing

```bash
mvn test        # or: make test
```

Coverage includes Eller's generation (visited cells, initial column, disjoint-set creation) and the
bidirectional solver (path search, path joining, edge cases).

## Roadmap

The following bonus parts of the original brief are **not implemented yet**:

- [ ] Cave generation with a cellular automaton
- [ ] Reinforcement learning (Q-learning) maze agent
- [ ] Web interface

## License

No license specified yet.

## Authors

Course project completed at School 21 by the `s21.team` team.
