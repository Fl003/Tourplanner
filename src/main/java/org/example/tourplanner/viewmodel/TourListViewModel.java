package org.example.tourplanner.viewmodel;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.tourplanner.dto.TourDto;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.service.LogService;
import org.example.tourplanner.service.TourService;

import java.util.ArrayList;
import java.util.List;

public class TourListViewModel {
    private final TourService tourService;

    public interface SelectionChangedListener {
        void onSelectionChanged(Long tourId);
    }

    private List<SelectionChangedListener> listeners = new ArrayList<>();

    private final ObservableList<Tour> tourList = FXCollections.observableArrayList();

    public ChangeListener<Tour> getChangeListener() {
        return (observableValue, oldValue, newValue) -> {
            if (newValue != null)
                notifyListeners(newValue.getId());
        };
    }

    private void notifyListeners(Long newValue) {
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
            Tour tour = tourService.convertTourDtoToTour(tourDto);
            tourList.add(tour);
        }
        notifyListeners(null);
    }

    public void reloadTour(Long tourId) {
        loadTours();
        notifyListeners(tourId);
    }

    public Tour getLastCreatedTours() {
        TourDto tour = tourService.getLastCreatedTours();
        if (tour == null) return null;
        return tourService.convertTourDtoToTour(tour);
    }

    public void setFilteredTours(List<Tour> filteredTours) {
        tourList.clear();
        tourList.addAll(filteredTours);
    }

    public ObservableList<Tour> getObservableTours() {
        return tourList;
    }
}
