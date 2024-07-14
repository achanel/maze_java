package s21.team.fileloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import s21.team.gui.MazeGridPanel;
import s21.team.util.Cell;
import s21.team.util.CustomFileManager;


public class AddMazeGridPanelToFile {

  public void saveMazeGridPanelToFile(MazeGridPanel mazePanel, JFrame frame) {
    JFileChooser chooser = CustomFileManager.getMazejFileChooser();

    int returnVal = chooser.showSaveDialog(frame);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      try {
        File file = chooser.getSelectedFile();
        saveMazeToFile(mazePanel, file.getPath(), frame);

        CustomFileManager.showGoodMazeFileMessage(frame);
      } catch (Exception e) {
        CustomFileManager.showErrorMazeFileMessage(frame);
      }
    } else {
      CustomFileManager.showErrorMazeFileMessage(frame);
    }
  }

  private void saveMazeToFile(MazeGridPanel mazePanel, String filePath, JFrame frame) {
    try {
      System.out.println("Add to File: " + mazePanel.toString());
      File file = new File(filePath);
      List<String> lines = new ArrayList<>();

      lines.add(mazePanel.getRows() + " " + mazePanel.getCols());

      for (Cell cell : mazePanel.getGrid()) {
        String[] wallData = new String[4];
        for (int i = 0; i < 4; i++) {
          wallData[i] = (cell.walls[i]) ? "1" : "0";
        }
        lines.add(String.join(" ", wallData));
      }

      CustomFileManager.writeMazeFile(file, lines);
    } catch (IOException e) {
      CustomFileManager.showErrorIOMazeFileMessage(frame);
    }
  }
}
