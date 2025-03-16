package org.example.tourplanner.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.model.TransportType;

public class GeneralViewModel {
    private final TourListViewModel tourListViewModel;

    private final ObservableList<String> transportTypes = FXCollections.observableArrayList();

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty startingPoint = new SimpleStringProperty();
    private final StringProperty destination = new SimpleStringProperty();
    private final ObjectProperty<TransportType> transportType = new SimpleObjectProperty<>();
    private final StringProperty transportTypeString = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty distance = new SimpleStringProperty();
    private final StringProperty estimatedTime = new SimpleStringProperty();

    public GeneralViewModel(TourListViewModel tourListViewModel) {
        this.tourListViewModel = tourListViewModel;
        this.tourListViewModel.addSelectionChangedListener(this::selectTour);

        transportTypes.add(TransportType.CAR.toString());
        transportTypes.add(TransportType.BUS.toString());
        transportTypes.add(TransportType.FAHRRAD.toString());
        transportTypes.add(TransportType.ZUFUSS.toString());
    }

    public ObservableList<String> getTransportType() {
        return transportTypes;
    }

    public void selectTour(Tour tour) {

        name.set(tour.getName());
        startingPoint.set(tour.getStartingpoint());
        destination.set(tour.getDestination());
        transportType.set(tour.getTransportType());
        description.set(tour.getDescription());
        double dist = tour.getDistance() / 1000;
        distance.set(dist + "km");
        int hours = (int) Math.floor(tour.getEstimatedTime() / 3600);
        int minutes = (int) Math.floor((tour.getEstimatedTime() - (hours * 3600)) / 60);
        int seconds = (int) Math.floor((tour.getEstimatedTime() - (minutes * 60) - (hours * 3600)));
        estimatedTime.set(hours + "h " + minutes + "min " + seconds + "sec");
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
    public ObjectProperty<TransportType> transportTypeObjectProperty() {
        return transportType;
    }
    public StringProperty transportTypeStringProperty() {
        transportTypeString.bind(Bindings.createStringBinding(
                () -> transportType.get() == null ? "" : transportType.get().toString(),
                transportType
        ));
        return transportTypeString;
    }
}
