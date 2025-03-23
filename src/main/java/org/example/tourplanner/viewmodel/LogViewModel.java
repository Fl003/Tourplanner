package org.example.tourplanner.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.tourplanner.model.Log;
import org.example.tourplanner.model.Tour;

import java.util.List;

public class LogViewModel {
    private final TourListViewModel tourListViewModel;
    private final ObservableList<Log> logs = FXCollections.observableArrayList();

    public LogViewModel(TourListViewModel tourListViewModel) {
        this.tourListViewModel = tourListViewModel;
        this.tourListViewModel.addSelectionChangedListener(this::setLogs);
    }

    public void setLogs(Tour tour) {
        this.logs.clear();
        this.logs.addAll(tour.getLogs());
    }

    public ObservableList<Log> getLogs() {
        return logs;
    }
}
