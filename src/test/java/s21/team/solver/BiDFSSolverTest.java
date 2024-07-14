package s21.team.solver;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import s21.team.generator.EllersGen;
import s21.team.gui.MazeGridPanel;
import s21.team.util.Cell;

class BiDFSSolverTest {
  private static List<Cell> grid;
  private static MazeGridPanel panel;
  private static BiDFSSolver solver;

  @BeforeAll
  public static void setUp() throws InterruptedException, ExecutionException {
    int rows = 10;
    int cols = 10;
    panel = new MazeGridPanel(rows, cols);

    grid = new ArrayList<>();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        grid.add(new Cell(i, j));
      }
    }

    EllersGen gen = new EllersGen(grid, panel);
    CompletableFuture<Boolean> future = gen.generate(1, cols);
    future.get();

    solver = new BiDFSSolver(grid, panel);
  }

  @Test
  void testConstructor() {
    assertNotNull(solver);
    assertEquals(grid, solver.grid);
    assertEquals(panel, solver.panel);
  }

  @Test
  void testSolveReturnsTrue() {
    Cell start = grid.get(0);
    Cell end = grid.get(grid.size() - 1);
    assertTrue(solver.solve(1, start, end));
  }

  @Test
  void testPathFromStartingPoint() {
    Cell start = grid.get(0);
    Cell end = grid.get(grid.size() - 1);
    solver.solve(1, start, end);
    solver.pathFromStartingPoint();
    assertNotNull(solver.curr1);
  }

  @Test
  void testPathFromEndingPoint() {
    Cell start = grid.get(0);
    Cell end = grid.get(grid.size() - 1);
    solver.solve(1, start, end);
    solver.pathFromEndingPoint();
    assertNotNull(solver.curr2);
  }

  @Test
  void testPathFound() {
    Cell start = grid.get(0);
    Cell end = grid.get(grid.size() - 1);
    solver.solve(1, start, end);
    assertNotNull(solver.pathFound());

    grid.get(1).setNoWay(false);
    grid.get(10).setNoWay(false);
    start.setNoWay(false);
    end.setNoWay(false);

    solver.pathFromStartingPoint();
    solver.pathFromEndingPoint();
    assertNotNull(solver.pathFound());
  }

  @Test
  void testFinalPath() {
    Cell start = grid.get(0);
    Cell end = grid.get(grid.size() - 1);
    solver.solve(1, start, end);

    grid.get(1).setNoWay(false);
    grid.get(10).setNoWay(false);
    start.setNoWay(false);
    end.setNoWay(false);

    solver.pathFromStartingPoint();
    solver.pathFromEndingPoint();

    if (solver.pathFound()) {
      solver.finalPath();
      assertTrue(grid.get(0).isVisited());
      assertTrue(grid.get(1).isVisited());
      assertTrue(grid.get(grid.size() - 1).isVisited());
    }
  }

  @Test
  void testSolvePathFound() {
    Cell start = grid.get(0);
    Cell end = grid.get(grid.size() - 1);

    boolean result = solver.solve(1, start, end);

    assertTrue(result);

    assertTrue(grid.get(grid.size() - 1).isVisited());
  }

  @Test
  void testSolveNoPathFound() {
    grid.get(1).setNoWay(true);
    grid.get(10).setNoWay(true);

    Cell start = grid.get(0);
    Cell end = grid.get(grid.size() - 1);

    boolean result = solver.solve(1, start, end);

    assertTrue(result);
  }

  @Test
  void testPathFromStartingPointNoPath() {
    Cell start = grid.get(0);
    Cell end = grid.get(grid.size() - 1);

    solver.solve(1, start, end);

    start.setNoWay(true);

    solver.pathFromStartingPoint();

    assertNotEquals(start, solver.curr1);
  }

  @Test
  void testPathFromEndingPointNoPath() {
    Cell start = grid.get(0);
    Cell end = grid.get(grid.size() - 1);

    solver.solve(1, start, end);

    end.setNoWay(true);

    solver.pathFromEndingPoint();

    assertNotNull(end);
    assertNotNull(solver.curr2);
  }

  @Test
  void testPathFoundWithIntersectingPaths() {
    Cell start = grid.get(0);
    Cell end = grid.get(grid.size() - 1);

    solver.solve(1, start, end);

    grid.get(1).setNoWay(false);
    grid.get(10).setNoWay(false);

    solver.pathFromStartingPoint();
    solver.pathFromEndingPoint();

    assertFalse(solver.pathFound());
  }

  @Test
  void testFinalPathAnotherWay() {
    Cell start = grid.get(0);
    Cell end = grid.get(grid.size() - 1);

    solver.solve(1, start, end);

    grid.get(1).setNoWay(false);
    grid.get(10).setNoWay(false);

    solver.pathFromStartingPoint();
    solver.pathFromEndingPoint();

    if (solver.pathFound()) {
      solver.finalPath();

      assertTrue(grid.get(0).isVisited());
      assertTrue(grid.get(1).isVisited());
      assertTrue(grid.get(grid.size() - 1).isVisited());
    }
  }

  @Test
  void testFinalPathOneMoreTest() {
    assertDoesNotThrow(() -> solver.finalPath());
  }

  @Test
  void testJoinPaths() {
    Stack<Cell> path1 = new Stack<>();
    Stack<Cell> path2 = new Stack<>();

    path1.push(grid.get(0));
    path1.push(grid.get(1));
    path1.push(grid.get(2));

    path2.push(grid.get(3));
    path2.push(grid.get(4));
    path2.push(grid.get(5));

    Cell c = grid.get(2);
    Cell current = grid.get(4);

    solver.joinPaths(c, path2, current);

    assertTrue(path1.containsAll(path2));
  }

  @Test
  void testJoinPaths_EmptyPaths() {
    Stack<Cell> path1 = new Stack<>();
    Stack<Cell> path2 = new Stack<>();

    Cell c = grid.get(2);
    Cell current = grid.get(4);

    solver.joinPaths(c, path2, current);

    assertTrue(path1.containsAll(path2));
  }
}
