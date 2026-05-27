package com.yufrolov.calculator.service;

import com.yufrolov.calculator.dto.HistoryDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryStorageService {
    private final List<HistoryDto> records = new ArrayList<>();


    public void addRecord(HistoryDto record) {
        if (record != null) records.add(record);
    }

    public List<HistoryDto> getAllRecords() {
        return Collections.unmodifiableList(records);
    }

    public void clear() {
        records.clear();
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }
}
