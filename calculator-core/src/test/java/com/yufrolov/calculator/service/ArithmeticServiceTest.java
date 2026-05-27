package com.yufrolov.calculator.service;

import com.yufrolov.calculator.exception.MathEvaluationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ArithmeticServiceTest {

    private ArithmeticService arithmeticService;
    private ArithmeticValidatorService validator;

    @BeforeEach
    void setUp() {
        validator = new ArithmeticValidatorService();
        arithmeticService = new ArithmeticService(validator);
    }

    @ParameterizedTest
    @CsvSource({
            "2, 3, 5",
            "10, 5, 15",
            "-2, 3, 1",
            "0, 0, 0",
            "2.5, 1.5, 4.0"
    })
    void testAdd(double a, double b, double expected) {
        assertEquals(expected, arithmeticService.add(a, b));
    }

    @ParameterizedTest
    @CsvSource({
            "10, 5, 5",
            "0, 5, -5",
            "-5, -5, 0",
            "2.5, 1.5, 1.0"
    })
    void testSubtract(double a, double b, double expected) {
        assertEquals(expected, arithmeticService.subtract(a, b));
    }

    @ParameterizedTest
    @CsvSource({
            "2, 3, 6",
            "10, 5, 50",
            "-2, 3, -6",
            "0, 100, 0",
            "2.5, 2, 5.0"
    })
    void testMultiply(double a, double b, double expected) {
        assertEquals(expected, arithmeticService.multiply(a, b));
    }

    @ParameterizedTest
    @CsvSource({
            "10, 5, 2",
            "100, 4, 25",
            "0, 5, 0",
            "7.5, 2.5, 3.0"
    })
    void testDivide(double a, double b, double expected) {
        assertEquals(expected, arithmeticService.divide(a, b));
    }

    @Test
    void testDivideByZero() {
        assertThrows(MathEvaluationException.class, () ->
                arithmeticService.divide(10, 0)
        );
    }

    @Test
    void testEvaluateWithSpaces() {
        assertEquals(14, arithmeticService.evaluate(" 2 + 3 * 4 "));
    }

    @Test
    void testEvaluateWithDivisionByZero() {
        assertThrows(MathEvaluationException.class, () ->
                arithmeticService.evaluate("10/0")
        );
    }

    @Test
    void testEvaluateWithNullExpression() {
        assertThrows(MathEvaluationException.class, () ->
                arithmeticService.evaluate(null)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    void testEvaluateWithEmptyOrBlankExpression(String expression) {
        assertThrows(MathEvaluationException.class, () ->
                arithmeticService.evaluate(expression)
        );
    }

    @Test
    void testEvaluateWithInvalidOperator() {
        assertThrows(MathEvaluationException.class, () ->
                arithmeticService.evaluate("2^3")
        );
    }

    @Test
    void testEvaluateWithUnmatchedParentheses() {
        assertThrows(Exception.class, () ->
                arithmeticService.evaluate("(2+3")
        );
    }


    @Test
    void testChainedOperations() {
        double result = arithmeticService.add(2, 3);
        assertEquals(5, result);

        result = arithmeticService.multiply(result, 4);
        assertEquals(20, result);

        result = arithmeticService.subtract(result, 10);
        assertEquals(10, result);

        result = arithmeticService.divide(result, 2);
        assertEquals(5, result);
    }

}