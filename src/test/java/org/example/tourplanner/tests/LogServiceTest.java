package org.example.tourplanner.tests;

import org.example.tourplanner.dto.LogDto;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//2
class LogServiceTest {

    private final org.example.tourplanner.service.LogService logService = new org.example.tourplanner.service.LogService();
//Verifies that retrieving all logs for a given tour ID returns a non-null list, ensuring backend communication works.
    @Test
    void testGetAllLogsForTourId_ShouldReturnLogsList() {
        // Assuming the backend is running and tour with ID 1 exists and has logs
        List<LogDto> logs = logService.getAllLogsForTourId(1L);
        assertNotNull(logs);
    }
//Confirms that the searchInLogs function can find logs
    @Test
    void testSearchInLogs_ShouldFindMatchingLog() {
        LogDto log = new LogDto();
        log.setId(1L);
        log.setTourId(1L);
        LocalDateTime now = LocalDateTime.now();
        log.setDatetime(Timestamp.valueOf(now));
        log.setComment("Nice tour");
        log.setDifficulty("Medium");
        log.setTotalDistance(10000.0);
        log.setTotalDuration(5400);
        log.setRating(4);

        List<LogDto> logs = List.of(log);
        assertTrue(logService.searchInLogs(logs, "Nice"));
        assertTrue(logService.searchInLogs(logs, "Medium"));
        assertTrue(logService.searchInLogs(logs, now.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        assertTrue(logService.searchInLogs(logs, "10"));
        assertTrue(logService.searchInLogs(logs, "1h 30min"));
        assertTrue(logService.searchInLogs(logs, "4"));
        assertFalse(logService.searchInLogs(logs, "NonExistingString"));
    }
}
