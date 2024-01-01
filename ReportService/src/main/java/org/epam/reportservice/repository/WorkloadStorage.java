package org.epam.reportservice.repository;

import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

@Repository
public class WorkloadStorage {
    private final Map<String, Map<Year, Map<Month, Double>>> storage = new HashMap<>();

    public Map<String, Map<Year, Map<Month, Double>>> getStorage() {
        return storage;
    }
}
