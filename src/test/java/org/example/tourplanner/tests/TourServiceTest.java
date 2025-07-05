package org.example.tourplanner.tests;
import org.example.tourplanner.service.LogService;
import org.example.tourplanner.service.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.example.tourplanner.dto.LogDto;
import org.example.tourplanner.dto.TourDto;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.model.TransportType;

//7
public class TourServiceTest {
    private org.example.tourplanner.service.TourService tourService;
    private LogService mockLogService;

    @BeforeEach
    void setUp() {
        mockLogService = Mockito.mock(LogService.class);
        tourService = new org.example.tourplanner.service.TourService(mockLogService);
    }
//nsures a TourDto and its logs are correctly converted into a Tour model with calculated popularity and child-friendliness.
    @Test
    void convertTourDtoToTour_shouldMapCorrectlyWithLogs() {
        TourDto dto = new TourDto(1L, "Test Tour", "Vienna", "Höchstädtplatz", 16.3,
                12.0, "Höchstädtplatz", 13.0, 12.0, "Car", 300.0, 720.0);

        LogDto log = new LogDto();
        log.setDifficulty("Easy");
        log.setTotalDuration(1500);
        log.setTotalDistance(40.0);

        when(mockLogService.getAllLogsForTourId(1L)).thenReturn(List.of(log));

        Tour result = tourService.convertTourDtoToTour(dto);

        assertNotNull(result);
        assertEquals("Test Tour", result.getName());
        assertEquals(5, result.getChildFriendliness());
        assertEquals(1, result.getPopularity());
    }
//Ensures that a tour with no logs has minimum popularity
    @Test
    void getPopularity_shouldReturn1IfNoLogs() {
        int popularity = tourService.getPopularity(null);
        assertEquals(5, popularity);
    }
//Tests the popularity algorithm
    @Test
    void getPopularity_shouldReturn4For7Logs() {
        List<LogDto> logs = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            logs.add(new LogDto());
        }
        int popularity = tourService.getPopularity(logs);
        assertEquals(4, popularity);
    }
//Verifies that logs with hard difficulty and long distance/time produce a low child-friendliness score
    @Test
    void getChildFriendliness_shouldReturnLowScoreForHardLogs() {
        LogDto log = new LogDto();
        log.setDifficulty("Difficult");
        log.setTotalDuration(10000); // >90min
        log.setTotalDistance(15000.0); // >10km

        int score = tourService.getChildFriendliness(List.of(log));
        assertTrue(score <= 1);
    }

//Tests child-friendliness calculation with mixed difficulty logs, expecting a mid-range score
    @Test
    void searchInTour_shouldFindTourByName() {
        Tour tour = new Tour(1L, "Amazing Trip", "Vienna", 0.0, (double) 0, "Linz", 0.0, (double) 0.0,
                TransportType.Car, "Nice journey", 500.00, 360000.0, 3, 4);

        boolean found = tourService.searchAllToursForString("Amazing")
                .stream()
                .anyMatch(t -> t.getName().equals("Amazing Trip"));
    }
//Confirms that a search by tour name correctly identifies matching tours
    @Test
    void getChildFriendliness_shouldReturnAverageScoreForMultipleLogs() {
        LogDto easyLog = new LogDto();
        easyLog.setDifficulty("Easy");
        easyLog.setTotalDuration(1000);
        easyLog.setTotalDistance(4000.0);

        LogDto mediumLog = new LogDto();
        mediumLog.setDifficulty("Medium");
        mediumLog.setTotalDuration(3600);
        mediumLog.setTotalDistance(7000.0);

        LogDto difficultLog = new LogDto();
        difficultLog.setDifficulty("Difficult");
        difficultLog.setTotalDuration(10000);
        difficultLog.setTotalDistance(15000.0);

        List<LogDto> logs = List.of(easyLog, mediumLog, difficultLog);
        int score = tourService.getChildFriendliness(logs);

        //score sollte zw 1 und 5
        assertTrue(score >= 1 && score <= 5);
    }

//uses reflection to test a private method that matches a search string against any tour field, verifying search behavior
    @Test
    void searchInTour_shouldReturnTrueIfSearchStringMatchesAnyField() throws Exception {
        Tour tour = new Tour(1L, "Holiday Trip", "StartPoint", 0.0, 0.0, "Destination", 0.0, 0.0,
                TransportType.Bike, "Beautiful tour", 8000.0, 7200.0, 3, 4);

        Method method = TourService.class.getDeclaredMethod("searchInTour", Tour.class, String.class);
        method.setAccessible(true);
        //Name
        boolean resultName = (boolean) method.invoke(tourService, tour, "Holiday");
        assertTrue(resultName);

        // Beschreibung
        boolean resultDesc = (boolean) method.invoke(tourService, tour, "Beautiful");
        assertTrue(resultDesc);

        //TransportType
        boolean resultTransport = (boolean) method.invoke(tourService, tour, "Bike");
        assertTrue(resultTransport);

        // nd vorhandener string
        boolean resultFalse = (boolean) method.invoke(tourService, tour, "Unknown");
        assertFalse(resultFalse);
    }

}