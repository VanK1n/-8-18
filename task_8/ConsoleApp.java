package task_8;

import java.io.IOException;

// ConsoleApp.java
public class ConsoleApp {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Использование: java ConsoleApp <input_file> <output_file>");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];

        try {
            int[][] array = ArrayUtils.readArrayFromFile(inputFile);
            if (!ArrayUtils.isRectangular(array)) {
                System.out.println("Входной массив не прямоугольный.");
                return;
            }
            boolean[][] saddlePoints = ArrayUtils.findSaddlePoints(array);
            ArrayUtils.writeArrayToFile(saddlePoints, outputFile);
            System.out.println("Результат успешно записан в " + outputFile);
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлами: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректные данные: " + e.getMessage());
        }
    }
}

