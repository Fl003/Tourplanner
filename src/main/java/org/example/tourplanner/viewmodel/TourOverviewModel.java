package org.example.tourplanner.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.tourplanner.model.Tour;

public class TourOverviewModel {
    private ObservableList<Tour> tourList = FXCollections.observableArrayList();

    public TourOverviewModel() {
        tourList.add(new Tour("Test1", "Fh Technikum", "Handelskai", "foot-walking", "Not available", 1.4, 10.0));
    }

    public void addTour(Tour tour) {
        tourList.add(tour);
    }

    public ObservableList<Tour> getObservableTours() {
        return tourList;
    }
}
