package s21.team.solver;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import javax.swing.Timer;
import s21.team.gui.MazeGridPanel;
import s21.team.util.Cell;


public class BiDFSSolver {

  private final Stack<Cell> path1 = new Stack<>();
  private final Stack<Cell> path2 = new Stack<>();
  Cell curr1;
  Cell curr2;
  final List<Cell> grid;
  final MazeGridPanel panel;

  public BiDFSSolver(List<Cell> grid, MazeGridPanel panel) {
    this.grid = grid;
    this.panel = panel;
  }

  public boolean solve(int speed, Cell start, Cell end) {
    curr1 = start;
    curr2 = end;
    final Timer timer = new Timer(speed, null);
    timer.addActionListener(e -> {
      if (!pathFound()) {
        pathFromEndingPoint();
        pathFromStartingPoint();
      } else {
        curr1 = null;
        curr2 = null;
        finalPath();
        timer.stop();
      }
      panel.setCurCells(Arrays.asList(curr1, curr2));
      panel.repaint();
      timer.setDelay(speed);
    });
    timer.start();
    return true;
  }

  void pathFromStartingPoint() {
    curr1.setNoWay(true);
    Cell next = curr1.getPathNeighbour(grid);
    if (next != null) {
      path1.push(curr1);
      curr1 = next;
    } else if (!path1.isEmpty()) {
      curr1 = path1.pop();
    }
  }

  void pathFromEndingPoint() {
    curr2.setNoWay(true);
    Cell next = curr2.getPathNeighbour(grid);
    if (next != null) {
      path2.push(curr2);
      curr2 = next;
    } else if (!path2.isEmpty()) {
      curr2 = path2.pop();
    }
  }

  boolean pathFound() {
    List<Cell> neighs1 = curr1.getValidMoveNeighbours(grid);
    List<Cell> neighs2 = curr2.getValidMoveNeighbours(grid);
    for (Cell c : neighs1) {
      if (path2.contains(c)) {
        path1.push(curr1);
        path1.push(c);
        joinPaths(c, path2, curr2);
        return true;
      }
    }
    for (Cell c : neighs2) {
      if (path1.contains(c)) {
        path2.push(curr2);
        path2.push(c);
        joinPaths(c, path1, curr1);
        return true;
      }
    }
    return false;
  }

  void finalPath() {
    while (!path1.isEmpty()) {
      path1.pop().setPath(true);
    }
  }

  void joinPaths(Cell c, Stack<Cell> path, Cell current) {
    while (!path.isEmpty() && !current.equals(c)) {
      current = path.pop();
    }
    path1.addAll(path2);
  }
}
