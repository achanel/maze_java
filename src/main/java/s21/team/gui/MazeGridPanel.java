package s21.team.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import s21.team.Maze;
import s21.team.util.Cell;

public class MazeGridPanel extends JPanel {
  private List<Cell> grid = new ArrayList<>();
  private List<Cell> curCells = new ArrayList<>();

  private Cell startPoint;
  private Cell endPoint;

  private final int cols;
  private final int rows;

  public MazeGridPanel(int rows, int cols) {
    this.cols = cols;
    this.rows = rows;
    for (int x = 0; x < rows; x++) {
      for (int y = 0; y < cols; y++) {
        grid.add(new Cell(x, y));
      }
    }

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int x = e.getX() / Maze.W;
        int y = e.getY() / Maze.W;
        Cell clickedCell = getCell(x, y);

        if (startPoint == null) {
          startPoint = clickedCell;
        } else if (endPoint == null) {
          endPoint = clickedCell;
          removeMouseListener(this);
        }
        repaint();
      }
    });
  }

  public List<Cell> getGrid() {
    return grid;
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(500 + 1, 500 + 1);
  }

  public void setCurrent(Cell current) {
    if (curCells.isEmpty()) {
      curCells.add(current);
    } else {
      curCells.set(0, current);
    }
  }

  public void setGrid(ArrayList<Cell> grid) {
    this.grid = grid;
  }

  public void setCurCells(List<Cell> curCells) {
    this.curCells = curCells;
  }

  public Cell getStartPoint() {
    return startPoint;
  }

  public Cell getEndPoint() {
    return endPoint;
  }

  private Cell getCell(int x, int y) {
    for (Cell cell : grid) {
      if (cell.getX() == x && cell.getY() == y) {
        return cell;
      }
    }
    return null;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (Cell c : grid) {
      c.draw(g);
    }
    for (Cell c : curCells) {
      if (c != null) c.displayAsColor(g, Color.ORANGE);
    }
    if (startPoint != null) {
      startPoint.displayAsColor(g, Color.GREEN);
    }
    if (endPoint != null) {
      endPoint.displayAsColor(g, Color.RED);
    }
  }

  public int getCols() {
    return cols;
  }

  public int getRows() {
    return rows;
  }

  @Override
  public String toString() {
    return "MazeGridPanel{" +
        "grid=" + grid +
        ", curCells=" + curCells +
        ", startPoint=" + startPoint +
        ", endPoint=" + endPoint +
        ", cols=" + cols +
        ", rows=" + rows +
        '}';
  }
}