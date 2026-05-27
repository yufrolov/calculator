package com.yufrolov.calculator.service;

import com.yufrolov.calculator.exception.MathEvaluationException;

import java.util.*;

public class MathExpressionService {
    private static final Map<Character, Integer> OPERATOR_PRECEDENCE = Map.of(
            '+', 1, '-', 1, '*', 2, '/', 2
    );

    public List<String> toPostfix(String expression) {
        List<String> output = new ArrayList<>();
        Deque<Character> operators = new ArrayDeque<>();

        boolean expectOperand = true;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isWhitespace(c)) {
                continue;
            }

            if (Character.isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i));
                    i++;
                }
                output.add(number.toString());
                i--;
                expectOperand = false;
                continue;
            }

            if (c == '-' && expectOperand) {
                StringBuilder number = new StringBuilder("-");
                i++;
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i));
                    i++;
                }
                output.add(number.toString());
                i--;
                expectOperand = false;
                continue;
            }

            if (c == '+' && expectOperand) {
                continue;
            }

            if (c == '(') {
                operators.push(c);
                expectOperand = true;
                continue;
            }

            if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    output.add(String.valueOf(operators.pop()));
                }
                operators.pop();
                expectOperand = false;
                continue;
            }

            if (isOperator(c)) {
                while (!operators.isEmpty() && operators.peek() != '(' &&
                        getPrecedence(operators.peek()) >= getPrecedence(c)) {
                    output.add(String.valueOf(operators.pop()));
                }
                operators.push(c);
                expectOperand = true;
                continue;
            }

            throw new MathEvaluationException("Unexpected character: " + c);
        }

        while (!operators.isEmpty()) {
            output.add(String.valueOf(operators.pop()));
        }

        return output;
    }

    public double evaluatePostfix(List<String> postfix, ArithmeticValidatorService validator) {
        Deque<Double> stack = new ArrayDeque<>();

        for (String token : postfix) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                double b = stack.pop();
                double a = stack.pop();
                double result = applyOperator(a, b, token.charAt(0), validator);
                stack.push(result);
            } else {
                throw new MathEvaluationException("Unknown token: " + token);
            }
        }

        return stack.pop();
    }

    private double applyOperator(double a, double b, char op, ArithmeticValidatorService validator) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (!validator.isDivisionAllowed(b)) {
                    throw new MathEvaluationException("Division by zero is not allowed!");
                }
                return a / b;
            default:
                throw new MathEvaluationException("Unknown operator: " + op);
        }
    }

    private boolean isOperator(char c) {
        return OPERATOR_PRECEDENCE.containsKey(c);
    }

    private int getPrecedence(char op) {
        return OPERATOR_PRECEDENCE.getOrDefault(op, 0);
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
