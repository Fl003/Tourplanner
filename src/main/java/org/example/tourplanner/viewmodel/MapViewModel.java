package org.example.tourplanner.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.example.tourplanner.dto.TourDto;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.service.TourService;


public class MapViewModel {
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    private final TourService tourService;

    public MapViewModel(TourListViewModel tourListViewModel, TourService tourService) {
        this.tourService = tourService;

        tourListViewModel.addSelectionChangedListener(this::selectTour);
    }

    public void selectTour(Long tourId) {
        TourDto tourDto = tourService.getTourById(tourId);
        Tour tour = tourService.convertTourDtoToTour(tourDto);
        selectedTour.set(tour);
    }

    public ReadOnlyObjectProperty<Tour> selectedTourProperty() {
        return selectedTour;
    }
}
