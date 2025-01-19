package task_8;

// GUIApp.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GUIApp extends JFrame {
    private JTable table;
    private JTable resultTable;
    private JButton loadButton;
    private JButton processButton;
    private JButton saveButton;

    public GUIApp() {
        setTitle("Седловые точки");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Панель для таблиц
        JPanel tablesPanel = new JPanel(new GridLayout(1, 2));

        // Исходная таблица
        table = new JTable();
        JScrollPane scrollPane1 = new JScrollPane(table);
        tablesPanel.add(scrollPane1);

        // Результирующая таблица
        resultTable = new JTable();
        JScrollPane scrollPane2 = new JScrollPane(resultTable);
        tablesPanel.add(scrollPane2);

        add(tablesPanel, BorderLayout.CENTER);

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();

        loadButton = new JButton("Загрузить из файла");
        processButton = new JButton("Найти седловые точки");
        saveButton = new JButton("Сохранить результат");

        buttonPanel.add(loadButton);
        buttonPanel.add(processButton);
        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Обработчики событий
        loadButton.addActionListener(e -> loadFromFile());
        processButton.addActionListener(e -> processSaddlePoints());
        saveButton.addActionListener(e -> saveToFile());
    }

    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int ret = fileChooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getPath();
            try {
                int[][] array = ArrayUtils.readArrayFromFile(filename);
                if (!ArrayUtils.isRectangular(array)) {
                    JOptionPane.showMessageDialog(this, "Входной массив не прямоугольный.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                displayArrayInJTable(array);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при чтении файла: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Некорректные данные: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayArrayInJTable(int[][] array) {
        int numRows = array.length;
        int numCols = array[0].length;
        String[] columnNames = new String[numCols];
        for (int j = 0; j < numCols; j++) {
            columnNames[j] = "Col " + (j + 1);
        }
        Object[][] tableData = new Object[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                tableData[i][j] = array[i][j];
            }
        }
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(tableData, columnNames);
        table.setModel(model);
    }

    private void processSaddlePoints() {
        try {
            int[][] array = ArrayUtils.readArrayFromJTable(table);
            boolean[][] saddlePoints = ArrayUtils.findSaddlePoints(array);
            ArrayUtils.displayArrayInJTable(saddlePoints, resultTable);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Некорректные данные в таблице: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int ret = fileChooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getPath();
            try {
                int[][] array = ArrayUtils.readArrayFromJTable(resultTable);
                boolean[][] boolArray = new boolean[array.length][array[0].length];
                for (int i = 0; i < array.length; i++) {
                    for (int j = 0; j < array[0].length; j++) {
                        boolArray[i][j] = array[i][j] == 1;
                    }
                }
                ArrayUtils.writeArrayToFile(boolArray, filename);
                JOptionPane.showMessageDialog(this, "Результат успешно сохранён.", "Успех", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при записи файла: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Некорректные данные в результирующей таблице: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUIApp app = new GUIApp();
            app.setVisible(true);
        });
    }
}

