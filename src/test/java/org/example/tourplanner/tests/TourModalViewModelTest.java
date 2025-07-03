package org.example.tourplanner.tests;

import org.example.tourplanner.dto.TourDto;
import org.example.tourplanner.model.TransportType;
import org.example.tourplanner.service.DirectionsService;
import org.example.tourplanner.service.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//2
public class TourModalViewModelTest {

    private TourService tourService;
    private DirectionsService directionsService;
    private org.example.tourplanner.viewmodel.TourModalViewModel viewModel;

    @BeforeEach
    void setup() {
        tourService = mock(TourService.class);
        directionsService = mock(DirectionsService.class);
        viewModel = new org.example.tourplanner.viewmodel.TourModalViewModel(tourService, directionsService);
    }
//Verifies that for a new tour (no ID), saveTour(): calls the directions API, parses the response JSON correctly, saves the tour via tourService
    @Test
    void saveTour_shouldCallSaveWhenIdNull() throws Exception {
        String directionsJson = """
            {
              "features": [
                {
                  "properties": {
                    "segments": [
                      {
                        "distance": 1234.5,
                        "duration": 567.8
                      }
                    ]
                  }
                }
              ]
            }
            """;

        when(directionsService.getDirections(anyString(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(directionsJson);

        viewModel.setTransportType(TransportType.Car);
        viewModel.getName().set("Tour Name");
        viewModel.setStartingPoint("Start", 1.0, 2.0);
        viewModel.setDestination("Dest", 3.0, 4.0);
        viewModel.getDescription().set("Desc");

        // id null so it should call saveTour
        boolean success = viewModel.saveTour();

        assertTrue(success);
        ArgumentCaptor<TourDto> captor = ArgumentCaptor.forClass(TourDto.class);
        verify(tourService, times(1)).saveTour(captor.capture());
        verify(tourService, never()).updateTour(any());

        TourDto dto = captor.getValue();
        assertEquals("Tour Name", dto.getName());
        assertEquals(1234.5, dto.getDistance());
        assertEquals(567.8, dto.getDuration());
    }


    @Test
    void saveTour_shouldReturnFalseWhenDirectionsJsonInvalid() throws Exception {
        when(directionsService.getDirections(anyString(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn("invalid json");

        viewModel.setTransportType(TransportType.Walking);
        viewModel.setStartingPoint("Start", 0.0, 0.0);
        viewModel.setDestination("End", 0.0, 0.0);
        viewModel.getName().set("Test");

        boolean success = viewModel.saveTour();

        assertFalse(success);
        verifyNoInteractions(tourService);
    }

}
