package com.yufrolov.calculator.service;

import com.yufrolov.calculator.exception.MathEvaluationException;

public class ArithmeticService {
    private final MathExpressionService parser;
    private final ArithmeticValidatorService validator;

    public ArithmeticService(ArithmeticValidatorService validator) {
        this.parser = new MathExpressionService();
        this.validator = validator;
    }

    public double evaluate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new MathEvaluationException("Expression cannot be empty");
        }

        String cleanExpr = expression.replaceAll("\\s+", "");
        var postfix = parser.toPostfix(cleanExpr);
        return parser.evaluatePostfix(postfix, validator);
    }

    public double add(double a, double b) {
        return evaluate(a + "+" + b);
    }

    public double subtract(double a, double b) {
        return evaluate(a + "-" + b);
    }

    public double multiply(double a, double b) {
        return evaluate(a + "*" + b);
    }

    public double divide(double a, double b) {
        return evaluate(a + "/" + b);
    }
}
