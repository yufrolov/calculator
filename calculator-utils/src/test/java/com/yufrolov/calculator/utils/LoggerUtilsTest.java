package com.yufrolov.calculator.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoggerUtilsTest {

    @Test
    void testGetLoggerReturnsNonNull() {
        assertNotNull(LoggerUtils.getLogger());
    }

    @Test
    void testGetLoggerReturnsLoggerWithCorrectName() {
        String loggerName = LoggerUtils.getLogger().getName();
        assertEquals(LoggerUtils.class.getName(), loggerName);
    }


    @Test
    void testErrorMethodDoesNotThrowException() {
        assertDoesNotThrow(() -> {
            LoggerUtils.error("Test error message");
            LoggerUtils.error("");
            LoggerUtils.error(null);
            LoggerUtils.error("!@#$%^&*()_+{}|:<>?~`-=[]\\;',./");
        });
    }

    @Test
    void testMultipleLogCalls() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                LoggerUtils.info("Info message " + i);
                LoggerUtils.error("Error message " + i);
            }
        });
    }
}