package org.example.tourplanner.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.example.tourplanner.model.Log;
import org.example.tourplanner.model.Tour;

public class LogViewModel {
    private final TourListViewModel tourListViewModel;
    private final ObservableList<Log> logs = FXCollections.observableArrayList();

    // safe which tour is selected to be able to create new logs -> later through id in db
    private Tour selectedTour;

    public LogViewModel(TourListViewModel tourListViewModel) {
        this.selectedTour = null;
        this.tourListViewModel = tourListViewModel;
        this.tourListViewModel.addSelectionChangedListener(this::setLogs);
    }

    public void setLogs(Tour tour) {
        if (tour == null) {
            this.logs.clear();
            return;
        }
        this.selectedTour = tour;
        this.logs.setAll(tour.getLogs());
        // to notice changes in Tour.logs
        // automatically reloads the table
        tour.getLogs().addListener((ListChangeListener<Log>) change -> logs.setAll(tour.getLogs()));
    }

    public ObservableList<Log> getLogs() {
        return logs;
    }

    public Tour getSelectedTour() { return selectedTour; }
}
