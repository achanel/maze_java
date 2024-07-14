package s21.team.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import s21.team.gui.MazeGridPanel;
import s21.team.util.Cell;

class EllerGenTest {

  @Mock
  private MazeGridPanel panel;

  private List<Cell> grid;
  private EllersGen ellersGen;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    grid = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        grid.add(new Cell(i, j));
      }
    }
    ellersGen = new EllersGen(grid, panel);
  }

  @Test
  void testGenerate() throws ExecutionException, InterruptedException {
    int speed = 1;
    int cols = 10;

    CompletableFuture<Boolean> future = ellersGen.generate(speed, cols);

    Thread.sleep(1);

    assertTrue(future.get());
    assertTrue(grid.stream().allMatch(Cell::isVisited));
  }

  @Test
  void testInitialColumn() {
    int speed = 1;
    int cols = 10;

    ellersGen.generate(speed, cols);

    List<Cell> initialColumn = grid.subList(0, cols);
    assertNotNull(initialColumn);
    assertEquals(cols, initialColumn.size());
  }

  @Test
  void testDisjointSetsCreation() {
    int speed = 1;
    int cols = 10;

    ellersGen.generate(speed, cols);

    for (Cell cell : grid) {
      assertNotNull(cell.getId());
    }
  }
}

