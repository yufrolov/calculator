package com.yufrolov.calculator.service;

import com.yufrolov.calculator.exception.MathEvaluationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MathExpressionServiceTest {

    private MathExpressionService parser;
    private ArithmeticValidatorService validator;

    @BeforeEach
    void setUp() {
        parser = new MathExpressionService();
        validator = new ArithmeticValidatorService();
    }


    @Test
    void testToPostfixSimpleAddition() {
        List<String> postfix = parser.toPostfix("2+3");
        assertEquals(List.of("2", "3", "+"), postfix);
    }

    @Test
    void testToPostfixSimpleSubtraction() {
        List<String> postfix = parser.toPostfix("5-2");
        assertEquals(List.of("5", "2", "-"), postfix);
    }

    @Test
    void testToPostfixSimpleMultiplication() {
        List<String> postfix = parser.toPostfix("3*4");
        assertEquals(List.of("3", "4", "*"), postfix);
    }

    @Test
    void testToPostfixSimpleDivision() {
        List<String> postfix = parser.toPostfix("10/2");
        assertEquals(List.of("10", "2", "/"), postfix);
    }

    @Test
    void testToPostfixWithOperatorPrecedence() {
        List<String> postfix = parser.toPostfix("2+3*4");
        assertEquals(List.of("2", "3", "4", "*", "+"), postfix);
    }

    @Test
    void testToPostfixWithParentheses() {
        List<String> postfix = parser.toPostfix("(2+3)*4");
        assertEquals(List.of("2", "3", "+", "4", "*"), postfix);
    }

    @Test
    void testToPostfixWithNestedParentheses() {
        List<String> postfix = parser.toPostfix("((2+3)*4)/2");
        assertEquals(List.of("2", "3", "+", "4", "*", "2", "/"), postfix);
    }

    @Test
    void testToPostfixWithDecimalNumbers() {
        List<String> postfix = parser.toPostfix("1.5+2.3");
        assertEquals(List.of("1.5", "2.3", "+"), postfix);
    }

    @Test
    void testToPostfixComplexExpression() {
        List<String> postfix = parser.toPostfix("10+2*6-4/2");
        assertEquals(List.of("10", "2", "6", "*", "+", "4", "2", "/", "-"), postfix);
    }

    @Test
    void testToPostfixWithMultipleOperators() {
        List<String> postfix = parser.toPostfix("2+3-4");
        assertEquals(List.of("2", "3", "+", "4", "-"), postfix);
    }

    @Test
    void testEvaluatePostfixSimple() {
        List<String> postfix = List.of("2", "3", "+");
        double result = parser.evaluatePostfix(postfix, validator);
        assertEquals(5.0, result);
    }

    @Test
    void testEvaluatePostfixWithPrecedence() {
        List<String> postfix = List.of("2", "3", "4", "*", "+");
        double result = parser.evaluatePostfix(postfix, validator);
        assertEquals(14.0, result);
    }

    @Test
    void testEvaluatePostfixWithDivision() {
        List<String> postfix = List.of("10", "2", "/");
        double result = parser.evaluatePostfix(postfix, validator);
        assertEquals(5.0, result);
    }

    @Test
    void testEvaluatePostfixComplex() {
        List<String> postfix = List.of("2", "3", "+", "4", "*", "2", "/");
        double result = parser.evaluatePostfix(postfix, validator);
        assertEquals(10.0, result);
    }

    @Test
    void testEvaluatePostfixWithDivisionByZero() {
        List<String> postfix = List.of("10", "0", "/");
        assertThrows(MathEvaluationException.class, () ->
                parser.evaluatePostfix(postfix, validator)
        );
    }

    @Test
    void testEvaluatePostfixInvalidOperator() {
        List<String> postfix = List.of("2", "3", "%");
        assertThrows(MathEvaluationException.class, () ->
                parser.evaluatePostfix(postfix, validator)
        );
    }


    @ParameterizedTest
    @CsvSource({
            "2+3, 5",
            "2+3*4, 14",
            "(2+3)*4, 20",
            "10/2, 5",
            "10-5, 5",
            "2*3, 6",
            "2.5+2.5, 5",
            "10/4, 2.5"
    })
    void testFullEvaluation(String expression, double expected) {
        List<String> postfix = parser.toPostfix(expression);
        double result = parser.evaluatePostfix(postfix, validator);
        assertEquals(expected, result);
    }


    @Test
    void testIsNumberWithValidNumber() {
        List<String> postfix = parser.toPostfix("123");
        double result = parser.evaluatePostfix(postfix, validator);
        assertEquals(123.0, result);
    }

    @Test
    void testIsNumberWithDecimal() {
        List<String> postfix = parser.toPostfix("123.456");
        double result = parser.evaluatePostfix(postfix, validator);
        assertEquals(123.456, result);
    }

    @Test
    void testOperatorPrecedence() {
        double result = parser.evaluatePostfix(parser.toPostfix("2+3*4"), validator);
        assertEquals(14.0, result);
    }

}