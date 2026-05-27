package com.yufrolov.calculator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ArithmeticValidatorServiceTest {
    private final ArithmeticValidatorService validator = new ArithmeticValidatorService();

    @Test
    void testIsDivisionAllowedReturnsBoolean() {
        assertDoesNotThrow(() -> validator.isDivisionAllowed(10));
        assertDoesNotThrow(() -> validator.isDivisionAllowed(0));
    }

    @Test
    void testDivisionAllowedWithPositiveInfinity() {
        assertTrue(validator.isDivisionAllowed(Double.POSITIVE_INFINITY));
    }

    @Test
    void testDivisionAllowedWithNegativeInfinity() {
        assertTrue(validator.isDivisionAllowed(Double.NEGATIVE_INFINITY));
    }


    @ParameterizedTest
    @ValueSource(doubles = {1, 2, 3.5, 100, -1, -5.5, 0.001, Double.MAX_VALUE})
    void testDivisionAllowedForNonZeroDivisors(double divisor) {
        assertTrue(validator.isDivisionAllowed(divisor),
                "Division should be allowed for divisor: " + divisor);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -0, 0.0})
    void testDivisionNotAllowedForZeroDivisor(double divisor) {
        assertFalse(validator.isDivisionAllowed(divisor),
                "Division should NOT be allowed for divisor: " + divisor);
    }
}