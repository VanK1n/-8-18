package task_8;

import java.io.*;
import java.util.*;
import javax.swing.JTable;

public class ArrayUtils {

    /**
     * Читает двумерный массив из файла.
     * Ожидается, что числа разделены пробелами, строки разделены переносами строк.
     *
     * @param filename Имя файла для чтения.
     * @return Двумерный массив целых чисел.
     * @throws IOException              Если возникает ошибка при чтении файла.
     * @throws IllegalArgumentException Если массив не прямоугольный.
     */
    public static int[][] readArrayFromFile(String filename) throws IOException {
        List<int[]> rows = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        int numCols = -1;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.trim().split("\\s+");
            if (numCols == -1) {
                numCols = tokens.length;
            } else if (tokens.length != numCols) {
                br.close();
                throw new IllegalArgumentException("Массив не прямоугольный.");
            }
            int[] row = new int[numCols];
            for (int i = 0; i < numCols; i++) {
                row[i] = Integer.parseInt(tokens[i]);
            }
            rows.add(row);
        }
        br.close();
        int[][] array = new int[rows.size()][numCols];
        for (int i = 0; i < rows.size(); i++) {
            array[i] = rows.get(i);
        }
        return array;
    }

    /**
     * Записывает двумерный массив булевых значений в файл.
     * Истинные значения записываются как "1", ложные как "0", разделены пробелами.
     *
     * @param array    Двумерный массив булевых значений.
     * @param filename Имя файла для записи.
     * @throws IOException Если возникает ошибка при записи файла.
     */
    public static void writeArrayToFile(boolean[][] array, String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        for (boolean[] row : array) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < row.length; i++) {
                sb.append(row[i] ? "1" : "0");
                if (i < row.length - 1) {
                    sb.append(" ");
                }
            }
            bw.write(sb.toString());
            bw.newLine();
        }
        bw.close();
    }

    /**
     * Находит седловые точки в двумерном массиве.
     *
     * @param array Входной двумерный массив.
     * @return Двумерный массив булевых значений, где true обозначает седловую точку.
     */
    public static boolean[][] findSaddlePoints(int[][] array) {
        int numRows = array.length;
        if (numRows == 0) return new boolean[0][0];
        int numCols = array[0].length;
        boolean[][] saddlePoints = new boolean[numRows][numCols];

        // Предварительный расчет минимумов и максимумов по строкам и столбцам
        int[] rowMins = new int[numRows];
        int[] rowMaxs = new int[numRows];
        int[] colMins = new int[numCols];
        int[] colMaxs = new int[numCols];

        for (int i = 0; i < numRows; i++) {
            rowMins[i] = array[i][0];
            rowMaxs[i] = array[i][0];
            for (int j = 1; j < numCols; j++) {
                if (array[i][j] < rowMins[i]) {
                    rowMins[i] = array[i][j];
                }
                if (array[i][j] > rowMaxs[i]) {
                    rowMaxs[i] = array[i][j];
                }
            }
        }

        for (int j = 0; j < numCols; j++) {
            colMins[j] = array[0][j];
            colMaxs[j] = array[0][j];
            for (int i = 1; i < numRows; i++) {
                if (array[i][j] < colMins[j]) {
                    colMins[j] = array[i][j];
                }
                if (array[i][j] > colMaxs[j]) {
                    colMaxs[j] = array[i][j];
                }
            }
        }

        // Определение седловых точек с использованием предварительно вычисленных значений
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int current = array[i][j];
                if ((current == rowMins[i] && current == colMaxs[j]) ||
                        (current == rowMaxs[i] && current == colMins[j])) {
                    saddlePoints[i][j] = true;
                }
            }
        }

        return saddlePoints;
    }

    /**
     * Проверяет, является ли массив прямоугольным.
     *
     * @param array Входной двумерный массив.
     * @return true, если прямоугольный, иначе false.
     */
    public static boolean isRectangular(int[][] array) {
        if (array.length == 0) return true;
        int numCols = array[0].length;
        for (int[] row : array) {
            if (row.length != numCols) return false;
        }
        return true;
    }

    /**
     * Читает двумерный массив из JTable.
     *
     * @param table JTable для чтения.
     * @return Двумерный массив целых чисел.
     * @throws NumberFormatException Если в таблице есть некорректные данные.
     */
    public static int[][] readArrayFromJTable(JTable table) throws NumberFormatException {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        int[][] array = new int[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Object value = table.getValueAt(i, j);
                if (value == null) {
                    throw new NumberFormatException("Пустая ячейка обнаружена.");
                }
                array[i][j] = Integer.parseInt(value.toString());
            }
        }
        return array;
    }

    /**
     * Отображает двумерный массив булевых значений в JTable.
     *
     * @param array Двумерный массив булевых значений.
     * @param table JTable для отображения.
     */
    public static void displayArrayInJTable(boolean[][] array, JTable table) {
        if (array.length == 0) {
            table.setModel(new javax.swing.table.DefaultTableModel());
            return;
        }
        int numRows = array.length;
        int numCols = array[0].length;
        String[] columnNames = new String[numCols];
        for (int j = 0; j < numCols; j++) {
            columnNames[j] = "Col " + (j + 1);
        }
        Object[][] tableData = new Object[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                tableData[i][j] = array[i][j] ? "1" : "0";
            }
        }
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Сделать ячейки не редактируемыми
            }
        };
        table.setModel(model);
    }
}
