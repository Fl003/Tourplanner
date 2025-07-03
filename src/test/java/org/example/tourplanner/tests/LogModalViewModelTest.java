package org.example.tourplanner.tests;

import org.example.tourplanner.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//5
public class LogModalViewModelTest {

    private LogService logServiceMock;
    private org.example.tourplanner.viewmodel.LogModalViewModel viewModel;

    @BeforeEach
    void setup() {
        logServiceMock = mock(LogService.class);
        viewModel = new org.example.tourplanner.viewmodel.LogModalViewModel(logServiceMock);
    }
//Checks if setting the total distance (in me) correctly converts and formats it into a km string
    @Test
    void testSetTotalDistance_convertsMetersToKmString() {
        viewModel.setTotalDistance(12345.0);
        assertEquals("12.345km", viewModel.totalDistanceProperty().get());
    }
//Tests if total time (in s) is converted into a human-readable format
    @Test
    void testSetTotalTime_convertsSecondsToReadableFormat() {
        viewModel.setTotalTime(7453);
        assertEquals("2h 4min 13sec", viewModel.totalTimeProperty().get());
    }

// Ensures that if no ID is set (new log), saveLog() calls the saveLog method in the service with the right data.
    @Test
    void testSaveLog_callsSaveOnNewLog() {
        // Set fields
        viewModel.setDate(LocalDate.of(2023, 12, 1));
        viewModel.setHour(10);
        viewModel.setMinute(30);
        viewModel.setComment("Test log");
        viewModel.setDifficulty("Medium");
        viewModel.setTotalDistance(5000.0);
        viewModel.setTotalTime(600);
        viewModel.setRating(4);
        viewModel.setTourId(99L);

        viewModel.saveLog();

        verify(logServiceMock, times(1)).saveLog(argThat(dto -> {
            Timestamp expectedTimestamp = Timestamp.valueOf("2023-12-01 10:30:00");
            return dto.getTourId().equals(99L)
                    && dto.getDatetime().equals(expectedTimestamp)
                    && dto.getComment().equals("Test log")
                    && dto.getDifficulty().equals("Medium")
                    && dto.getTotalDistance() == 5000.0
                    && dto.getTotalDuration() == 600
                    && dto.getRating() == 4;
        }));
    }
//Ensures that if an ID exists (editing log), saveLog() calls the updateLog method.
    @Test
    void testSaveLog_callsUpdateOnExistingLog() {
        viewModel.setId(42L);
        viewModel.setTourId(1L);
        viewModel.setDate(LocalDate.of(2024, 6, 1));
        viewModel.setHour(8);
        viewModel.setMinute(15);
        viewModel.setComment("Update test");
        viewModel.setDifficulty("Easy");
        viewModel.setTotalDistance(3000.0); // 3km
        viewModel.setTotalTime(180);        // 3min
        viewModel.setRating(5);

        viewModel.saveLog();

        verify(logServiceMock, times(1)).updateLog(argThat(dto -> dto.getId() == 42L));
    }


//Checks that calling clearLog() resets all form fields to defaults (or null), preparing for a new log entry.
    @Test
    void testClearLog_resetsAllFields() {
        viewModel.setId(1L);
        viewModel.setTourId(2L);
        viewModel.setDate(LocalDate.now());
        viewModel.setHour(9);
        viewModel.setMinute(45);
        viewModel.setComment("Clear me");
        viewModel.setDifficulty("Difficult");
        viewModel.setTotalDistance(5000.0);
        viewModel.setTotalTime(300);
        viewModel.setRating(3);

        viewModel.clearLog();

        assertNull(viewModel.getId());
        assertNull(viewModel.getTourId());
        assertNull(viewModel.dateProperty().get());
        assertNull(viewModel.hourProperty().get());
        assertNull(viewModel.minuteProperty().get());
        assertNull(viewModel.commentProperty().get());
        assertEquals("Easy", viewModel.difficultyProperty().get());
        assertNull(viewModel.totalDistanceProperty().get());
        assertNull(viewModel.totalTimeProperty().get());
        assertEquals(0, viewModel.ratingProperty().get());
    }
}
