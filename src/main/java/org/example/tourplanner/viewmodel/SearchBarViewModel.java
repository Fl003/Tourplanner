package org.example.tourplanner.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.service.TourService;

import java.util.List;

public class SearchBarViewModel {
    private final TourListViewModel tourListViewModel;
    private final TourService tourService;

    private final StringProperty searchString = new SimpleStringProperty();

    public SearchBarViewModel(TourListViewModel tourListViewModel, TourService tourService) {
        this.tourListViewModel = tourListViewModel;
        this.tourService = tourService;
    }

    public void searchAllToursForString() {
        if (searchString.get() == null || searchString.get().isEmpty()) {
            tourListViewModel.loadTours();
            return;
        }
        List<Tour> tours = tourService.searchAllToursForString(searchString.get());

        tourListViewModel.setFilteredTours(tours);
    }

    public StringProperty searchStringProperty() {
        return searchString;
    }
}
