package com.yufrolov.calculator.service;

import com.yufrolov.calculator.dto.HistoryDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryStorageServiceTest {

    private HistoryStorageService history;
    private HistoryDto testRecord1;
    private HistoryDto testRecord2;
    private HistoryDto testRecord3;

    @BeforeEach
    void setUp() {
        System.out.println(">>> Setting up test: creating fresh history storage");
        history = new HistoryStorageService();

        LocalDateTime now = LocalDateTime.now();
        testRecord1 = new HistoryDto("2+2", 4.0, now);
        testRecord2 = new HistoryDto("3*3", 9.0, now.plusSeconds(1));
        testRecord3 = new HistoryDto("10/2", 5.0, now.plusSeconds(2));
    }

    @AfterEach
    void tearDown() {
        System.out.println(">>> Cleaning up: clearing history storage");
        history = null;
        testRecord1 = null;
        testRecord2 = null;
        testRecord3 = null;
    }

    @Test
    void testAddRecordMultipleRecords() {
        history.addRecord(testRecord1);
        history.addRecord(testRecord2);
        history.addRecord(testRecord3);

        assertEquals(3, history.getAllRecords().size());
        assertEquals(testRecord1, history.getAllRecords().get(0));
        assertEquals(testRecord2, history.getAllRecords().get(1));
        assertEquals(testRecord3, history.getAllRecords().get(2));
    }

    @Test
    void testAddRecordNullRecord() {
        assertDoesNotThrow(() -> history.addRecord(null));
        assertEquals(0, history.getAllRecords().size());
    }

    @Test
    void testGetAllRecordsWhenEmpty() {
        List<HistoryDto> records = history.getAllRecords();

        assertNotNull(records);
        assertTrue(records.isEmpty());
    }

    @Test
    void testGetAllRecordsReturnsUnmodifiableList() {
        history.addRecord(testRecord1);
        List<HistoryDto> records = history.getAllRecords();

        assertThrows(UnsupportedOperationException.class, () ->
                records.add(new HistoryDto("5+5", 10.0))
        );

        assertThrows(UnsupportedOperationException.class, () ->
                records.remove(0)
        );
    }

    @Test
    void testClearWhenHistoryHasRecords() {
        history.addRecord(testRecord1);
        history.addRecord(testRecord2);

        assertFalse(history.isEmpty());

        history.clear();

        assertTrue(history.isEmpty());
        assertEquals(0, history.getAllRecords().size());
    }

    @Test
    void testIsEmptyWhenHistoryIsEmpty() {
        assertTrue(history.isEmpty());
    }

    @Test
    void testIsEmptyAfterAddingRecord() {
        history.addRecord(testRecord1);
        assertFalse(history.isEmpty());
    }

    @Test
    void testRecordsPreserveInsertionOrder() {
        history.addRecord(testRecord1);
        history.addRecord(testRecord2);
        history.addRecord(testRecord3);

        List<HistoryDto> records = history.getAllRecords();
        assertEquals(testRecord1, records.get(0));
        assertEquals(testRecord2, records.get(1));
        assertEquals(testRecord3, records.get(2));
    }

}