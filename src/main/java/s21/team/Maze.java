package s21.team;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import s21.team.fileloader.AddMazeGridPanelToFile;
import s21.team.fileloader.GetMazeGridPanelFromFile;
import s21.team.generator.EllersGen;
import s21.team.gui.MazeGridPanel;
import s21.team.solver.BiDFSSolver;
import s21.team.util.Cell;


public class Maze implements ItemListener {
  private int COLS;
  private int ROWS;
  public static MazeGridPanel grid;
  public static final int WIDTH = 500;
  public static final int HEIGHT = WIDTH;
  public static int W = 20;
  private final int speed = 1;
  private boolean generated;

  private Cell startPoint;
  private Cell endPoint;
  JPanel MAIN_CARDS;

  public static void main(String[] args) {
    new Maze();
  }

  public Maze() {
    EventQueue.invokeLater(() -> {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        ex.printStackTrace();
      }
      String[] options = {"generate", "load from file"};

      createAndShowGUI(JOptionPane.showOptionDialog(null, "Which dessert?", "Select one:",
          JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]));
    });
  }

  private void createAndShowGUI(int type) {
    JFrame frame = new JFrame("Generating Maze using Eller's Algorithm");

    GetMazeGridPanelFromFile generateMazeFromFile = new GetMazeGridPanelFromFile();
    AddMazeGridPanelToFile addMazeGridPanelToFile = new AddMazeGridPanelToFile();

    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    frame.setContentPane(container);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    CardLayout cardLayout = new CardLayout();

    MAIN_CARDS = new JPanel(cardLayout);
    CardLayout cardLayoutFileLoader = new CardLayout();

    JButton runButton = new JButton("Generate");
    JButton solveButton = new JButton("Solve");
    JButton fileSaver = new JButton("Save to file");
    JButton fileLoader = new JButton("Load from file");
    JPanel generatePAnel = createGeneratePanel(runButton, solveButton, cardLayout, fileSaver);

    if (type == 0) {
      JButton set = new JButton("Set");
      JFormattedTextField rows = new JFormattedTextField(NumberFormat.getNumberInstance());
      rows.setText("rows");
      rows.setColumns(10);
      JFormattedTextField colons = new JFormattedTextField(NumberFormat.getNumberInstance());
      colons.setText("colons");
      colons.setColumns(10);
      JPanel setPanel = createSetSizePannel(set, cardLayout, colons, rows);
      container.add(setPanel);

      final JPanel cardsSaveToFile = createFilePanel(fileSaver, cardLayoutFileLoader);
      Dimension buttonFileLoaderSize = generatePAnel.getComponent(0).getPreferredSize();
      fileSaver.setPreferredSize(buttonFileLoaderSize);

      set.addActionListener(e -> {
        try {
          COLS = (int) (long) colons.getValue();
          ROWS = (int) (long) rows.getValue();
        } catch (Exception exception) {
          JOptionPane.showMessageDialog(frame, "Only numbers");
          return;
        }

        if (COLS > 25 || ROWS > 25) W = 10;

        grid = new MazeGridPanel(ROWS, COLS);
        grid.setBackground(Color.BLACK);

        JPanel mazeBorder = new JPanel();
        final int BORDER_SIZE = 20;
        mazeBorder.setBounds(0, 0, WIDTH + BORDER_SIZE, HEIGHT + BORDER_SIZE);
        mazeBorder.setBackground(Color.BLACK);
        mazeBorder.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
        mazeBorder.add(grid);

        container.add(mazeBorder);
        container.add(generatePAnel);

        setPanel.setVisible(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
      });

      runButton.addActionListener(e -> {
        if (COLS == 0 || ROWS == 0) {
          JOptionPane.showMessageDialog(frame, "Field size not selected");
        } else {
          generated = false;
          EllersGen generator = new EllersGen(grid.getGrid(), grid);
          generator.generate(speed, COLS).thenAccept(result -> generated = result);
          startPoint = null;
          endPoint = null;
          container.add(cardsSaveToFile);
          runButton.setVisible(false);
        }
      });

      solveButton.addActionListener(e -> {
        startPoint = grid.getStartPoint();
        endPoint = grid.getEndPoint();
        if (generated && startPoint != null && endPoint != null) {
          frame.setTitle("Solving Maze using BiDFS");
          final BiDFSSolver solver = new BiDFSSolver(grid.getGrid(), grid);
          solver.solve(speed, startPoint, endPoint);
          solveButton.setVisible(false);
          fileSaver.setVisible(false);
        } else {
          JOptionPane.showMessageDialog(frame,
              "Ensure the maze is generated and start/end points are selected.");
        }
      });

      fileSaver.addActionListener(e -> {
        if (generated) {
          addMazeGridPanelToFile.saveMazeGridPanelToFile(grid, frame);
        } else {
          JOptionPane.showMessageDialog(frame,
              "Ensure the maze is generated and start/end points are selected.");
        }
      });
    } else {
      final JPanel cardsLoadFromFile = createFilePanel(fileLoader, cardLayoutFileLoader);

      Dimension buttonFileLoaderSize = cardsLoadFromFile.getComponent(0).getPreferredSize();

      fileLoader.setPreferredSize(buttonFileLoaderSize);
      fileSaver.setPreferredSize(buttonFileLoaderSize);

      container.add(cardsLoadFromFile);

      solveButton.addActionListener(e -> {
        startPoint = grid.getStartPoint();
        endPoint = grid.getEndPoint();
        if (startPoint != null && endPoint != null) {
          frame.setTitle("Solving Maze using BiDFS");
          final BiDFSSolver solver = new BiDFSSolver(grid.getGrid(), grid);
          solver.solve(speed, startPoint, endPoint);
          solveButton.setVisible(false);
          fileSaver.setVisible(false);
        } else {
          JOptionPane.showMessageDialog(frame,
              "Ensure the maze is generated and start/end points are selected.");
        }
      });

      fileLoader.addActionListener(e -> {
        grid = generateMazeFromFile.getMazeGridPanelFromFile(frame);
        grid.setBackground(Color.BLACK);
        JPanel mazeBorder = new JPanel();
        final int BORDER_SIZE = 20;
        mazeBorder.setBounds(0, 0, WIDTH + BORDER_SIZE, HEIGHT + BORDER_SIZE);
        mazeBorder.setBackground(Color.BLACK);
        mazeBorder.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
        mazeBorder.add(grid);
        container.add(mazeBorder);
        container.add(solveButton);
        fileLoader.setVisible(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
      });

      fileSaver.addActionListener(e -> {
        addMazeGridPanelToFile.saveMazeGridPanelToFile(grid, frame);
      });
    }

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  public static JPanel createSetSizePannel(JButton set, CardLayout cardLayout,
                                           JFormattedTextField colons, JFormattedTextField rows) {
    JPanel card1 = new JPanel();
    card1.setLayout(new GridBagLayout());

    card1.add(set);
    card1.add(colons);
    card1.add(rows);

    JPanel cards = new JPanel(cardLayout);
    cards.setBorder(new EmptyBorder(0, 20, 0, 0));
    cards.setOpaque(false);
    cards.add(card1, "Generate");
    return cards;
  }

  private static JPanel createGeneratePanel(JButton runButton, JButton solveButton, CardLayout cardLayout, JButton fileSaver) {
    JPanel card1 = new JPanel();
    card1.setLayout(new GridBagLayout());

    card1.add(runButton);
    card1.add(solveButton);
    card1.add(fileSaver);

    JPanel cards = new JPanel(cardLayout);
    cards.setBorder(new EmptyBorder(0, 20, 0, 0));
    cards.setOpaque(false);
    cards.add(card1, "Generate");
    cards.add(card1, "Solve");
    return cards;
  }

  private static JPanel createFilePanel(JButton fileLoader,
                                        CardLayout cardLayout) {
    JPanel card1 = new JPanel();
    card1.setLayout(new GridBagLayout());

    GridBagConstraints c = new GridBagConstraints();

    c.insets = new Insets(5, 0, 5, 18);
    c.fill = GridBagConstraints.BOTH;

    c.gridheight = 2;
    c.weightx = 0.3;
    c.gridx = 1;
    c.gridy = 0;

    card1.add(fileLoader, c);

    JPanel cards = new JPanel(cardLayout);
    cards.setBorder(new EmptyBorder(0, 20, 0, 0));
    cards.setOpaque(false);
    cards.add(card1, "Add file");
    return cards;
  }

  private static JPanel createMazeBorder(MazeGridPanel grid) {
    JPanel mazeBorder = new JPanel();
    final int BORDER_SIZE = 20;
    mazeBorder.setBounds(0, 0, grid.getWidth() + BORDER_SIZE, grid.getHeight() + BORDER_SIZE);
    mazeBorder.setBackground(Color.BLACK);
    mazeBorder.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

    mazeBorder.add(grid);
    return mazeBorder;
  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    CardLayout cl = (CardLayout) (MAIN_CARDS.getLayout());
    cl.show(MAIN_CARDS, (String) e.getItem());
  }
}
