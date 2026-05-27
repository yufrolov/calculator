package com.yufrolov.calculator;

import com.yufrolov.calculator.service.ArithmeticService;
import com.yufrolov.calculator.service.ArithmeticValidatorService;
import com.yufrolov.calculator.service.ArithmeticMenuService;
import com.yufrolov.calculator.service.HistoryStorageService;
import com.yufrolov.calculator.utils.LoggerUtils;

public class App {
    public static void main(String[] args) {
        LoggerUtils.info("Starting Calculator Application...");

        // Создание зависимостей
        ArithmeticValidatorService validator = new ArithmeticValidatorService();
        ArithmeticService calculator = new ArithmeticService(validator);
        HistoryStorageService history = new HistoryStorageService();

        // Запуск консольного меню
        ArithmeticMenuService menu = new ArithmeticMenuService(calculator, history);
        menu.start();

        LoggerUtils.info("Application terminated");
    }
}