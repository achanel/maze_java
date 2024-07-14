package s21.team.fileloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import s21.team.gui.MazeGridPanel;
import s21.team.util.Cell;
import s21.team.util.CustomFileManager;

public class GetMazeGridPanelFromFile {

  private static final String COLS_FROM_FILE = "COLS";
  private static final String ROWS_FROM_FILE = "ROWS";

  public MazeGridPanel getMazeGridPanelFromFile(JFrame frame) {
    JFileChooser chooser = CustomFileManager.getMazejFileChooser();

    int returnVal = chooser.showOpenDialog(frame);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      try {
        return fileParserFromFile(chooser.getSelectedFile().getPath(), frame);
      } catch (IOException ex) {
        CustomFileManager.showErrorMazeFileMessage(frame);
        return null;
      }
    } else {
      CustomFileManager.showErrorMazeFileMessage(frame);
      return null;
    }
  }

  private MazeGridPanel fileParserFromFile(
      String filePath,
      JFrame frame) throws IOException {
    try {
      File file = new File(filePath);
      List<String> lines = CustomFileManager.readMazeFile(file);
      ArrayList<Cell> cellsFromFile = new ArrayList<>();
      Map<String, Integer> gridDimensions = getGridDimensions(lines);

      int cols = gridDimensions.get(COLS_FROM_FILE);
      int rows = gridDimensions.get(ROWS_FROM_FILE);

      MazeGridPanel mazePanel = new MazeGridPanel(cols, rows);

      for (int y = 0; y < cols; y++) {
        for (int x = 0; x < rows; x++) {
          cellsFromFile.add(new Cell(y, x));
        }
      }

      for (int i = 0, currentCellIndex = 0; i < lines.size(); i++) {
        String line = lines.get(i);
        if (line.length() == 7) {
          boolean[] walls = stringToBooleanArray(line);
          cellsFromFile.get(currentCellIndex).setWalls(walls);
          currentCellIndex++;
        }
      }

      mazePanel.setGrid(cellsFromFile);
      System.out.println("Add to File: " + mazePanel);

      return mazePanel;
    } catch (Exception e) {
      CustomFileManager.showErrorIOMazeFileMessage(frame);
      return null;
    }
  }

  private boolean[] stringToBooleanArray(String wallsData) {
    String[] sortedWallsData = wallsData.split(" ");

    boolean[] walls = new boolean[sortedWallsData.length];
    for (int i = 0; i < sortedWallsData.length; i++) {
      walls[i] = sortedWallsData[i].equals("1");
    }
    return walls;
  }

  private Map<String, Integer> getGridDimensions(List<String> lines) throws Exception {
    String[] colRowItems = lines.get(0).split(" ");
    if (colRowItems.length != 2) {
      throw new IOException("Incorrect rows and columns");
    }

    Map<String, Integer> dimensions = new HashMap<>();
    dimensions.put(ROWS_FROM_FILE, Integer.parseInt(colRowItems[0]));
    dimensions.put(COLS_FROM_FILE, Integer.parseInt(colRowItems[1]));

    return dimensions;
  }
}
