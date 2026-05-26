package com.yufrolov.calculator.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtils {
    private static final Logger logger = LogManager.getLogger(LoggerUtils.class);

    public static Logger getLogger() {
        return logger;
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void error(String message) {
        logger.error(message);
    }
}