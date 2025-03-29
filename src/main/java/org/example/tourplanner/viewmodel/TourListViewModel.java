package org.example.tourplanner.viewmodel;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.tourplanner.model.Log;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.model.TransportType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TourListViewModel {
    public void deleteTour(Tour selectedItem) {
        tourList.remove(selectedItem);
        notifyListeners(null);
    }

    public void updateTour(Tour editableTour) {
        for(int i = 0; i < tourList.size(); i++) {
            if(tourList.get(i).equals(editableTour)) {
                tourList.set(i, editableTour);
                notifyListeners(editableTour);
                break;
            }
        }
    }

    public void saveLog(Log newLog, Tour selectedTour) {
        int index = tourList.indexOf(selectedTour);
        for(int i = 0; i < tourList.get(index).getLogs().size(); i++) {
            if(tourList.get(index).getLogs().get(i).equals(newLog)) {
                tourList.get(index).getLogs().set(i, newLog);
                return;
            }
        }
        tourList.get(index).getLogs().add(newLog);
    }

    public interface SelectionChangedListener {
        void onSelectionChanged(Tour tour);
    }

    private List<SelectionChangedListener> listeners = new ArrayList<>();

    private ObservableList<Tour> tourList = FXCollections.observableArrayList();

    public ChangeListener<Tour> getChangeListener() {
        return (observableValue, oldValue, newValue) -> notifyListeners(newValue);
    }

    private void notifyListeners(Tour newValue) {
        for (var listener : listeners ) {
            listener.onSelectionChanged(newValue);
        }
    }

    public void addSelectionChangedListener(SelectionChangedListener listener) {
        listeners.add(listener);
    }

    public TourListViewModel() {
        tourList.add(new Tour(1,"Test1", "Fh Technikum", "Handelskai", TransportType.CAR, "Not available", 1408.8, 281.9));
        Log l = new Log(LocalDate.now(), 15, 35, "Was a very nice trip", "easy", 3502, 432, 3);
        tourList.getFirst().addLog(l);
    }

    public void addTour(Tour tour) {
        tourList.add(tour);
    }

    public void deleteLog(Log log, Tour tour) {
        // search for tour and delete log from tour
        int i = this.tourList.indexOf(tour);
        this.tourList.get(i).removeLog(log);
    }

    public ObservableList<Tour> getObservableTours() {
        return tourList;
    }
}
