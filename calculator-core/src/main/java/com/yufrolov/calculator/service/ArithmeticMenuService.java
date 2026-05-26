package com.yufrolov.calculator.service;

import com.yufrolov.calculator.dto.HistoryDto;
import com.yufrolov.calculator.exception.MathEvaluationException;
import com.yufrolov.calculator.utils.LoggerUtils;

import java.util.Scanner;

public class ArithmeticMenuService {
    private final ArithmeticService calculator;
    private final HistoryStorageService history;
    private final Scanner scanner;

    public ArithmeticMenuService(ArithmeticService calculator, HistoryStorageService history) {
        this.calculator = calculator;
        this.history = history;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Запуск главного цикла программы
     */
    public void start() {
        printWelcomeMessage();

        while (true) {
            System.out.print(">> ");
            String input = scanner.nextLine().trim();

            if (processCommand(input)) {
                break;
            }

            System.out.println();
        }

        scanner.close();
    }

    /**
     * Обработка введенной команды или выражения
     *
     * @return true если нужно завершить программу
     */
    private boolean processCommand(String input) {
        if (input.equalsIgnoreCase("/exit")) {
            LoggerUtils.info("Exiting application...");
            System.out.println("До свидания!");
            return true;
        }

        if (input.equalsIgnoreCase("/history")) {
            showHistory();
            return false;
        }

        if (input.equalsIgnoreCase("/clear")) {
            clearHistory();
            return false;
        }

        calculateAndSave(input);
        return false;
    }

    /**
     * Вывод приветствия и списка команд
     */
    private void printWelcomeMessage() {
        LoggerUtils.info("Starting Calculator Application...");

        System.out.println("========================================");
        System.out.println("   КАЛЬКУЛЯТОР С ИСТОРИЕЙ v1.0");
        System.out.println("========================================");
        System.out.println("Команды:");
        System.out.println("  /history  - показать историю вычислений");
        System.out.println("  /clear    - очистить историю");
        System.out.println("  /exit     - выход");
        System.out.println("  или введите выражение (например: 2+3*4, (2+3)*4)");
        System.out.println("========================================\n");
    }

    /**
     * Отображение истории вычислений
     */
    private void showHistory() {
        if (history.isEmpty()) {
            System.out.println("История пуста.");
        } else {
            System.out.println("\n=== ИСТОРИЯ ВЫЧИСЛЕНИЙ ===");
            for (HistoryDto record : history.getAllRecords()) {
                System.out.println(record);
            }
            System.out.println("===========================\n");
        }
    }

    /**
     * Очистка истории
     */
    private void clearHistory() {
        history.clear();
        LoggerUtils.info("History cleared");
        System.out.println("История очищена.\n");
    }

    /**
     * Вычисление выражения и сохранение результата
     */
    private void calculateAndSave(String input) {
        try {
            LoggerUtils.info("Calculating: " + input);
            double result = calculator.evaluate(input);

            String resultStr = formatResult(result);
            System.out.println("= " + resultStr);

            HistoryDto record = new HistoryDto(input, result);
            history.addRecord(record);
            LoggerUtils.info("Result: " + result + " (saved to history)");

        } catch (MathEvaluationException e) {
            LoggerUtils.error("Calculation error: " + e.getMessage());
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            LoggerUtils.error("Unexpected error: " + e.getMessage());
            System.out.println("Ошибка: Неверное выражение. Используйте: 2+3*4, (2+3)*4");
        }
    }

    /**
     * Форматирование результата (убирает .0 для целых чисел)
     */
    private String formatResult(double result) {
        return result == (long) result ?
                String.format("%d", (long) result) :
                String.format("%s", result);
    }
}
