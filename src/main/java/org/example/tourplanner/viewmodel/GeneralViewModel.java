package org.example.tourplanner.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.tourplanner.dto.TourDto;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.model.TransportType;
import org.example.tourplanner.service.TourService;

public class GeneralViewModel {
    private final ObservableList<String> transportTypes = FXCollections.observableArrayList();

    private final TourService tourService;

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty startingPoint = new SimpleStringProperty();
    private final StringProperty destination = new SimpleStringProperty();
    private final ObjectProperty<TransportType> transportType = new SimpleObjectProperty<>();
    private final StringProperty transportTypeString = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty distance = new SimpleStringProperty();
    private final StringProperty estimatedTime = new SimpleStringProperty();
    private final IntegerProperty popularity = new SimpleIntegerProperty();
    private final IntegerProperty childFriendliness = new SimpleIntegerProperty();

    public GeneralViewModel(TourListViewModel tourListViewModel, TourService tourService) {
        this.tourService = tourService;

        tourListViewModel.addSelectionChangedListener(this::selectTour);

        transportTypes.add(TransportType.Car.toString());
        transportTypes.add(TransportType.Bus.toString());
        transportTypes.add(TransportType.Bike.toString());
        transportTypes.add(TransportType.Walking.toString());
    }

    public ObservableList<String> getTransportType() {
        return transportTypes;
    }

    public void selectTour(Long tourId) {
        if(tourId == null){
            name.set("");
            startingPoint.set("");
            destination.set("");
            transportType.set(null);
            description.set("");
            distance.set("");
            estimatedTime.set("");
            popularity.set(0);
            childFriendliness.set(0);
            return;
        }

        TourDto tourDto = tourService.getTourById(tourId);
        Tour tour = tourService.convertTourDtoToTour(tourDto);

        name.set(tour.getName());
        startingPoint.set(tour.getStartingpoint());
        destination.set(tour.getDestination());
        transportType.set(tour.getTransportType());
        description.set(tour.getDescription());
        double dist = tour.getDistance() / 1000;
        distance.set(String.format("%.2f km", dist));
        int hours = (int) Math.floor(tour.getEstimatedTime() / 3600);
        int minutes = (int) Math.floor((tour.getEstimatedTime() - (hours * 3600)) / 60);
        int seconds = (int) Math.floor((tour.getEstimatedTime() - (minutes * 60) - (hours * 3600)));
        estimatedTime.set(hours + "h " + minutes + "min " + seconds + "sec");
        popularity.set(tour.getPopularity());
        childFriendliness.set(tour.getChildFriendliness());
    }

    public StringProperty nameProperty() {
        return name;
    }
    public StringProperty startingPointProperty() {
        return startingPoint;
    }
    public StringProperty destinationProperty() {
        return destination;
    }
    public StringProperty descriptionProperty() {
        return description;
    }
    public StringProperty estimatedTimeProperty() {
        return estimatedTime;
    }
    public StringProperty distanceProperty() {
        return distance;
    }
    public StringProperty transportTypeStringProperty() {
        transportTypeString.bind(Bindings.createStringBinding(
                () -> transportType.get() == null ? "" : transportType.get().toString(),
                transportType
        ));
        return transportTypeString;
    }
    public IntegerProperty popularityProperty() { return popularity; }
    public IntegerProperty childFriendlinessProperty() { return childFriendliness; }
}
