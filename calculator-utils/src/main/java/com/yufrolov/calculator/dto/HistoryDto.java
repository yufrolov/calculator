package com.yufrolov.calculator.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryDto {
    private final String expression;
    private final double result;
    private final LocalDateTime timestamp;

    public HistoryDto(String expression, double result) {
        this.expression = expression;
        this.result = result;
        this.timestamp = LocalDateTime.now();
    }

    public String getExpression() {
        return expression;
    }

    public double getResult() {
        return result;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s = %s",
                timestamp.format(formatter), expression,
                result == (long) result ? String.format("%d", (long) result) : String.format("%s", result));
    }
}
