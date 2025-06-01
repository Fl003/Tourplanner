package org.example.tourplanner.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.example.tourplanner.model.Tour;


public class MapViewModel {
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    public MapViewModel(TourListViewModel tourListViewModel) {
        tourListViewModel.addSelectionChangedListener(this::selectTour);
    }

    public void selectTour(Tour tour) {
        selectedTour.set(tour);
    }

    public ReadOnlyObjectProperty<Tour> selectedTourProperty() {
        return selectedTour;
    }
}
