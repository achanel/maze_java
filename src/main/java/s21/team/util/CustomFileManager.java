package s21.team.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class CustomFileManager {
  public static List<String> readMazeFile(File file) throws Exception {
    List<String> lines = new ArrayList<>();
    try (Scanner scanner = new Scanner(file)) {
      while (scanner.hasNextLine()) {
        lines.add(scanner.nextLine());
      }
    }

    if (lines.isEmpty()) {
      throw new IOException("Lines from file is empty.");
    }

    return lines;
  }

  public static void writeMazeFile(File file, List<String> lines) throws IOException {
    FileWriter writer = new FileWriter(file);
    for (String line : lines) {
      writer.write(line + System.lineSeparator());
    }
    writer.close();
  }

  public static JFileChooser getMazejFileChooser() {
    JFileChooser chooser = new JFileChooser();

    FileFilter filter = new FileFilter() {
      @Override
      public boolean accept(File f) {
        return f.exists() && f.isFile() && f.canRead() && f.getName().toLowerCase().endsWith(".txt");
      }

      @Override
      public String getDescription() {
        return "Только .txt файлы";
      }
    };

    chooser.setFileFilter(filter);
    chooser.setAcceptAllFileFilterUsed(false);
    return chooser;
  }

  public static void showErrorMazeFileMessage(JFrame frame) {
    JOptionPane.showMessageDialog(frame,
        "Не выбран файл", "Ошибка", JOptionPane.ERROR_MESSAGE);
  }

  public static void showErrorIOMazeFileMessage(JFrame frame) {
    JOptionPane.showMessageDialog(frame,
        "Please, check your file on correct data.");
  }

  public static void showGoodMazeFileMessage(JFrame frame) {
    JOptionPane.showMessageDialog(frame,
        "Данные были успешно загружены.", "Успешно", JOptionPane.INFORMATION_MESSAGE);
  }
}
