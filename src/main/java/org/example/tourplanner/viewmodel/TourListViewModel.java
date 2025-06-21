package org.example.tourplanner.viewmodel;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.tourplanner.dto.TourDto;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.model.TransportType;
import org.example.tourplanner.service.TourService;

import java.util.ArrayList;
import java.util.List;

public class TourListViewModel {
    private final TourService tourService;

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

    public void deleteTour(Long tourId) {
        tourService.deleteTour(tourId);
        loadTours();
    }

    public void loadTours() {
        tourList.clear();
        List<TourDto> tours = this.tourService.getAllTours();
        for (TourDto tourDto : tours) {
            Tour tour = new Tour(
                    tourDto.getId(),
                    tourDto.getName(),
                    tourDto.getStartingPoint(),
                    tourDto.getStartLat(),
                    tourDto.getStartLng(),
                    tourDto.getDestination(),
                    tourDto.getDestinationLat(),
                    tourDto.getDestinationLng(),
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
