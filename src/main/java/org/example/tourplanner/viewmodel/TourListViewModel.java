package org.example.tourplanner.viewmodel;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.tourplanner.dto.TourDto;
import org.example.tourplanner.model.Log;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.model.TransportType;
import org.example.tourplanner.service.TourService;

import java.util.ArrayList;
import java.util.List;

public class TourListViewModel {
    private final TourService tourService;

    public void deleteTour(Tour selectedItem) {
        tourList.remove(selectedItem);
        notifyListeners(null);
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

    private final ObservableList<Tour> tourList = FXCollections.observableArrayList();

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

    public TourListViewModel(TourService tourService) {
        this.tourService = tourService;

        loadTours();
    }

    public void addTour(Tour tour) {
        tourList.add(tour);
    }

    public void deleteLog(Log log, Tour tour) {
        // search for tour and delete log from tour
        int i = this.tourList.indexOf(tour);
        this.tourList.get(i).removeLog(log);
    }

    public void loadTours() {
        tourList.clear();
        List<TourDto> tours = this.tourService.getAllTours();
        for (TourDto tourDto : tours) {
            Tour tour = new Tour(
                    tourDto.getId(),
                    tourDto.getName(),
                    tourDto.getStartingPoint(),
                    tourDto.getDestination(),
                    TransportType.valueOf(tourDto.getTransportType()),
                    tourDto.getDescription(),
                    tourDto.getDistance(),
                    tourDto.getDuration()
            );
            tourList.add(tour);
        }
    }

    public ObservableList<Tour> getObservableTours() {
        return tourList;
    }
}
