package s21.team.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import javax.swing.Timer;
import s21.team.gui.MazeGridPanel;
import s21.team.util.Cell;
import s21.team.util.DisjointSets;


public class EllersGen {
  private final List<Cell> grid;
  private List<Cell> initialColumn;
  private final DisjointSets disjointSet;

  private int fromIndex;
  private int toIndex;
  private boolean genNextCol;
  private final MazeGridPanel panel;

  public EllersGen(List<Cell> grid, MazeGridPanel panel) {
    this.grid = grid;
    this.panel = panel;
    this.genNextCol = true;
    fromIndex = 0;
    this.disjointSet = new DisjointSets();
  }

  public CompletableFuture<Boolean> generate(int speed, int cols) {
    CompletableFuture<Boolean> future = new CompletableFuture<>();

    for (int i = 0; i < grid.size(); i++) {
      grid.get(i).setId(i);
      disjointSet.setCreation(grid.get(i).getId());
    }

    toIndex = cols;
    final Timer timer = new Timer(speed, null);
    timer.addActionListener(e -> {
      if (genNextCol) {
        initialColumn = grid.subList(fromIndex, toIndex);
        fromIndex = toIndex;
        toIndex += cols;
        new ColumnFormation(initialColumn, speed, panel);
      } else if (grid.parallelStream().allMatch(Cell::isVisited)) {
        timer.stop();
        future.complete(true);
      }
    });
    timer.start();

    return future;
  }

  private class ColumnFormation {

    private final Queue<Cell> iterateRightQueue = new LinkedList<>();
    private final Queue<Cell> iterateDownQueue = new LinkedList<>();
    private final List<Cell> col;
    private final Random r = new Random();
    private Cell current;

    private ColumnFormation(List<Cell> col, int speed, MazeGridPanel panel) {
      genNextCol = false;
      this.col = col;
      iterateDownQueue.addAll(col);
      iterateRightQueue.addAll(col);

      final Timer timer = new Timer(speed, null);
      timer.addActionListener(e -> {
        if (!iterateDownQueue.isEmpty()) {
          iterateDown();
        } else if (!iterateRightQueue.isEmpty()) {
          iterateRight();
        } else {
          current = null;
          genNextCol = true;
          timer.stop();
        }
        panel.setCurrent(current);
        panel.repaint();
        timer.setDelay(speed);
      });
      timer.start();
    }

    private void iterateRight() {
      Cell c = iterateRightQueue.poll();

      List<Cell> cells = new ArrayList<>();
      for (Cell c2 : col) {
        if (disjointSet.setFinding(c.getId()) == disjointSet.setFinding(c2.getId())) {
          cells.add(c2);
        }
      }
      Collections.shuffle(cells);
      Cell c3 = cells.get(0);
      Cell right = c3.getRightNeighbour(grid);
      if (right != null) {
        current = right;
        right.setVisited(true);
        c3.removeWalls(right);
        disjointSet.union(c3.getId(), right.getId());
      }
    }

    private void iterateDown() {
      current = iterateDownQueue.poll();
      current.setVisited(true);

      if (r.nextBoolean() || col.contains(grid.get(grid.size() - 1))) {
        Cell bottom = current.getBottomNeighbour(grid);
        if (bottom != null) {
          if (disjointSet.setFinding(current.getId()) != disjointSet.setFinding(bottom.getId())) {
            current.removeWalls(bottom);
            disjointSet.union(current.getId(), bottom.getId());
          }
        }
      }
    }
  }
}