package com.yufrolov.calculator.service;

public class ArithmeticValidatorService {
    public boolean isDivisionAllowed(double divisor) {
        return divisor != 0;
    }
}
