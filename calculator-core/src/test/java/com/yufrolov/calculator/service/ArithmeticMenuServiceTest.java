package com.yufrolov.calculator.service;

import com.yufrolov.calculator.dto.HistoryDto;
import com.yufrolov.calculator.exception.MathEvaluationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArithmeticMenuServiceTest {
    @Mock
    private ArithmeticService mockCalculator;

    @Mock
    private HistoryStorageService mockHistory;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        outContent.reset();
    }

    // ==================== Тесты для команд ====================

    @Test
    void testExitCommand() {
        provideInput("/exit\n");
        ArithmeticMenuService menu = new ArithmeticMenuService(mockCalculator, mockHistory);

        menu.start();

        assertTrue(outContent.toString().contains("До свидания!"));
    }

    @Test
    void testHistoryCommandWhenEmpty() {
        when(mockHistory.isEmpty()).thenReturn(true);
        provideInput("/history\n/exit\n");
        ArithmeticMenuService menu = new ArithmeticMenuService(mockCalculator, mockHistory);

        menu.start();

        assertTrue(outContent.toString().contains("История пуста"));
        verify(mockHistory).isEmpty();
    }

    @Test
    void testHistoryCommandWithRecords() {
        HistoryDto record = new HistoryDto("2+2", 4.0);
        when(mockHistory.isEmpty()).thenReturn(false);
        when(mockHistory.getAllRecords()).thenReturn(List.of(record));
        provideInput("/history\n/exit\n");
        ArithmeticMenuService menu = new ArithmeticMenuService(mockCalculator, mockHistory);

        menu.start();

        assertTrue(outContent.toString().contains("2+2"));
        verify(mockHistory).getAllRecords();
    }

    @Test
    void testClearCommand() {
        provideInput("/clear\n/exit\n");
        ArithmeticMenuService menu = new ArithmeticMenuService(mockCalculator, mockHistory);

        menu.start();

        assertTrue(outContent.toString().contains("История очищена"));
        verify(mockHistory).clear();
    }

    @Test
    void testSuccessfulCalculation() {
        when(mockCalculator.evaluate("2+2")).thenReturn(4.0);
        provideInput("2+2\n/exit\n");
        ArithmeticMenuService menu = new ArithmeticMenuService(mockCalculator, mockHistory);

        menu.start();

        assertTrue(outContent.toString().contains("= 4"));
        verify(mockCalculator).evaluate("2+2");
        verify(mockHistory).addRecord(any(HistoryDto.class));
    }

    @Test
    void testCalculationWithDoubleResult() {
        when(mockCalculator.evaluate("5/2")).thenReturn(2.5);
        provideInput("5/2\n/exit\n");
        ArithmeticMenuService menu = new ArithmeticMenuService(mockCalculator, mockHistory);

        menu.start();

        assertTrue(outContent.toString().contains("= 2.5"));
    }

    @Test
    void testCalculationWithMathError() {
        when(mockCalculator.evaluate("10/0")).thenThrow(new MathEvaluationException("Division by zero!"));
        provideInput("10/0\n/exit\n");
        ArithmeticMenuService menu = new ArithmeticMenuService(mockCalculator, mockHistory);

        menu.start();

        assertTrue(outContent.toString().contains("Division by zero!"));
        verify(mockHistory, never()).addRecord(any());
    }

    @Test
    void testCalculationWithGenericError() {
        when(mockCalculator.evaluate("invalid")).thenThrow(new RuntimeException());
        provideInput("invalid\n/exit\n");
        ArithmeticMenuService menu = new ArithmeticMenuService(mockCalculator, mockHistory);

        menu.start();

        assertTrue(outContent.toString().contains("Неверное выражение"));
        verify(mockHistory, never()).addRecord(any());
    }

    @Test
    void testFormatIntegerResult() {
        when(mockCalculator.evaluate("2+2")).thenReturn(4.0);
        provideInput("2+2\n/exit\n");
        ArithmeticMenuService menu = new ArithmeticMenuService(mockCalculator, mockHistory);

        menu.start();

        assertTrue(outContent.toString().contains("= 4"));
        assertFalse(outContent.toString().contains("4.0"));
    }

    @Test
    void testFormatDoubleResult() {
        when(mockCalculator.evaluate("5/2")).thenReturn(2.5);
        provideInput("5/2\n/exit\n");
        ArithmeticMenuService menu = new ArithmeticMenuService(mockCalculator, mockHistory);

        menu.start();

        assertTrue(outContent.toString().contains("= 2.5"));
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }
}