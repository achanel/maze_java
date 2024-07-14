package s21.team.util;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import s21.team.Maze;

public class Cell {
  private final int x;
  private final int y;
  private int id;

  private boolean path = false;
  private boolean noWay = false;
  private boolean visited = false;

  public boolean[] walls = {true, true, true, true};

  public Cell(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getY() {
    return y;
  }

  public int getX() {
    return x;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isVisited() {
    return visited;
  }

  public void setVisited(boolean visited) {
    this.visited = visited;
  }

  public void setNoWay(boolean noWay) {
    this.noWay = noWay;
  }

  public void setPath(boolean path) {
    this.path = path;
  }

  public boolean isPath() {
    return path;
  }

  public void setWalls(boolean[] walls) {
    this.walls = walls;
  }

  public void draw(Graphics g) {
    int x2 = x * Maze.W;
    int y2 = y * Maze.W;

    if (visited) {
      g.setColor(Color.LIGHT_GRAY);
      g.fillRect(x2, y2, Maze.W, Maze.W);
    }

    if (path) {
      g.setColor(Color.BLUE);
      g.fillRect(x2, y2, Maze.W, Maze.W);
    } else if (noWay) {
      g.setColor(Color.BLACK);
      g.fillRect(x2, y2, Maze.W, Maze.W);
    }

    g.setColor(Color.WHITE);
    if (walls[0]) {
      drawThickLine(g, x2, y2, x2 + Maze.W, y2);
    }
    if (walls[1]) {
      drawThickLine(g, x2 + Maze.W, y2, x2 + Maze.W, y2 + Maze.W);
    }
    if (walls[2]) {
      drawThickLine(g, x2 + Maze.W, y2 + Maze.W, x2, y2 + Maze.W);
    }
    if (walls[3]) {
      drawThickLine(g, x2, y2 + Maze.W, x2, y2);
    }
  }

  public static void drawThickLine(Graphics g, int x1, int y1, int x2,
                                   int y2) {
    float length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1)
        * (y2 - y1));
    float dx = (x2 - x1) / length;
    float dy = (y2 - y1) / length;
    float xpos = x1;
    float ypos = y1;
    for (int i = 0; i < (int) length; i++) {
      drawDot(g, (int) xpos, (int) ypos);
      xpos += dx;
      ypos += dy;
    }
    drawDot(g, x2, y2);
  }

  public static void drawDot(Graphics g, int x, int y) {
    g.drawLine(x - 1, y - 1, x, y - 1);
    g.drawLine(x - 1, y, x, y);
  }

  public void setVerticalWall(boolean wall) {
    walls[0] = wall;
  }

  public void setHorizontalWall(boolean wall) {
    walls[1] = wall;
  }

  public void displayAsColor(Graphics g, Color color) {
    int x2 = x * Maze.W;
    int y2 = y * Maze.W;
    g.setColor(color);
    g.fillRect(x2, y2, Maze.W, Maze.W);
  }

  public void removeWalls(Cell next) {
    int x = this.x - next.x;

    if (x == 1) {
      walls[3] = false;
      next.walls[1] = false;
    } else if (x == -1) {
      walls[1] = false;
      next.walls[3] = false;
    }

    int y = this.y - next.y;

    if (y == 1) {
      walls[0] = false;
      next.walls[2] = false;
    } else if (y == -1) {
      walls[2] = false;
      next.walls[0] = false;
    }
  }


  private Cell checkNeighbourInGridBounds(List<Cell> grid, Cell neighbour) {
    if (grid.contains(neighbour)) {
      return grid.get(grid.indexOf(neighbour));
    } else {
      return null;
    }
  }

  private Cell randomNeighbour(List<Cell> neighbours) {
    if (!neighbours.isEmpty()) {
      return neighbours.get(new Random().nextInt(neighbours.size()));
    } else {
      return null;
    }
  }

  public List<Cell> getValidMoveNeighbours(List<Cell> grid) {
    List<Cell> neighbours = new ArrayList<>(4);

    Cell top = checkNeighbourInGridBounds(grid, new Cell(x, y - 1));
    Cell right = checkNeighbourInGridBounds(grid, new Cell(x + 1, y));
    Cell bottom = checkNeighbourInGridBounds(grid, new Cell(x, y + 1));
    Cell left = checkNeighbourInGridBounds(grid, new Cell(x - 1, y));

    if (top != null && !walls[0]) {
      neighbours.add(top);
    }
    if (right != null && !walls[1]) {
      neighbours.add(right);
    }
    if (bottom != null && !walls[2]) {
      neighbours.add(bottom);
    }
    if (left != null && !walls[3]) {
      neighbours.add(left);
    }

    return neighbours;
  }

  public Cell getPathNeighbour(List<Cell> grid) {
    List<Cell> neighbours = new ArrayList<>();

    Cell top = checkNeighbourInGridBounds(grid, new Cell(x, y - 1));
    Cell right = checkNeighbourInGridBounds(grid, new Cell(x + 1, y));
    Cell bottom = checkNeighbourInGridBounds(grid, new Cell(x, y + 1));
    Cell left = checkNeighbourInGridBounds(grid, new Cell(x - 1, y));

    if (top != null && !top.noWay && !walls[0]) {
      neighbours.add(top);
    }
    if (bottom != null && !bottom.noWay && !walls[2]) {
      neighbours.add(bottom);
    }
    if (right != null && !right.noWay && !walls[1]) {
      neighbours.add(right);
    }
    if (left != null && !left.noWay && !walls[3]) {
      neighbours.add(left);
    }
    if (neighbours.size() == 1) {
      return neighbours.get(0);
    }

    return randomNeighbour(neighbours);
  }

  public Cell getRightNeighbour(List<Cell> grid) {
    return checkNeighbourInGridBounds(grid, new Cell(x + 1, y));
  }

  public Cell getBottomNeighbour(List<Cell> grid) {
    return checkNeighbourInGridBounds(grid, new Cell(x, y + 1));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Cell other = (Cell) obj;
    if (x != other.x) {
      return false;
    }
    return y == other.y;
  }

  @Override
  public String toString() {
    return "Cell{" +
        "x=" + x +
        ", y=" + y +
        ", id=" + id +
        ", path=" + path +
        ", noWay=" + noWay +
        ", visited=" + visited +
        ", walls=" + Arrays.toString(walls) +
        '}';
  }
}