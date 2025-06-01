package org.example.tourplanner.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Tour {
    private final Long id;
    private final StringProperty name;
    private final StringProperty startingPoint;
    private final DoubleProperty startLat;
    private final DoubleProperty startLng;
    private final StringProperty destination;
    private final DoubleProperty destinationLat;
    private final DoubleProperty destinationLng;
    private final ObjectProperty<TransportType> transportType;
    private final StringProperty description;
    private final DoubleProperty distance;
    private final DoubleProperty estimatedTime;
    private ObservableList<Log> logs = FXCollections.observableArrayList();


    public Tour(StringProperty name, StringProperty startingPoint, DoubleProperty startLat, DoubleProperty startLng, StringProperty destination, DoubleProperty destinationLat, DoubleProperty destinationLng, ObjectProperty<TransportType> transportType, StringProperty description, DoubleProperty distance, DoubleProperty estimatedTime) {
        this.id = 0L;
        this.name = name;
        this.startingPoint = startingPoint;
        this.startLat = startLat;
        this.startLng = startLng;
        this.destination = destination;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
        this.transportType = transportType;
        this.description = description;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
    }

    public Tour(Long id, String name, String startingPoint, Double startLat, Double startLng, String destination, Double destinationLat, Double destinationLng, TransportType transportType, String description, Double distance, Double estimatedTime) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.startingPoint = new SimpleStringProperty(startingPoint);
        this.startLat = new SimpleDoubleProperty(startLat);
        this.startLng = new SimpleDoubleProperty(startLng);
        this.destination= new SimpleStringProperty(destination);
        this.destinationLat = new SimpleDoubleProperty(destinationLat);
        this.destinationLng = new SimpleDoubleProperty(destinationLng);
        this.transportType = new SimpleObjectProperty<>(transportType);
        this.description = new SimpleStringProperty(description);
        this.distance = new SimpleDoubleProperty(distance);
        this.estimatedTime = new SimpleDoubleProperty(estimatedTime);
    }

    public Tour(String name, String startingPoint, Double startLat, Double startLng, String destination, Double destinationLat, Double destinationLng, TransportType transportType, String description) {
        this.id = 0L;
        this.name = new SimpleStringProperty(name);
        this.startingPoint = new SimpleStringProperty(startingPoint);
        this.startLat = new SimpleDoubleProperty(startLat);
        this.startLng = new SimpleDoubleProperty(startLng);
        this.destination= new SimpleStringProperty(destination);
        this.destinationLat =  new SimpleDoubleProperty(destinationLat);
        this.destinationLng = new SimpleDoubleProperty(destinationLng);
        this.transportType = new SimpleObjectProperty<>(transportType);
        this.description = new SimpleStringProperty(description);
        this.distance = new SimpleDoubleProperty(0);
        this.estimatedTime = new SimpleDoubleProperty(0);
    }

    public void addLog(Log log) {
        this.logs.add(log);
    }

    public void removeLog(Log log) {
        this.logs.remove(log);
    }

    public void setLogs(ObservableList<Log> logs) {
        this.logs = logs;
    }

    public Long getId() { return id; }

    public ObservableList<Log> getLogs() {
        return logs;
    }

    public String getStartingpoint() {
        return startingPoint.get();
    }

    public StringProperty startingPointProperty() {
        return startingPoint;
    }

    public Double getStartLat() {
        return startLat.get();
    }

    public DoubleProperty startLat() {
        return startLat;
    }

    public Double getStartLng() {
        return startLng.get();
    }

    public DoubleProperty startLng() {
        return startLat;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDestination() {
        return destination.get();
    }
    
    public Double getDestinationLat() {
        return destinationLat.get();
    }

    public DoubleProperty destinationLat() {
        return destinationLat;
    }

    public Double getDestinationLng() {
        return destinationLng.get();
    }

    public DoubleProperty destinationLng() {
        return destinationLng;
    }

    public StringProperty destinationProperty() {
        return destination;
    }

    public TransportType getTransportType() {
        return transportType.get();
    }

    public ObjectProperty<TransportType> transportTypeProperty() {
        return transportType;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public double getDistance() {
        return distance.get();
    }

    public DoubleProperty distanceProperty() {
        return distance;
    }

    public double getEstimatedTime() {
        return estimatedTime.get();
    }

    public DoubleProperty estimatedTimeProperty() {
        return estimatedTime;
    }

    public void setDistance(double distance) {
        this.distance.set(distance);
    }

    public void setEstimatedTime(double estimatedTime) {
        this.distance.set(estimatedTime);
    }

    @Override
    public String toString() {
        return getName();
    }

    public void setName(String name) {this.name.set(name);}

    public void setStartingPoint(String startingPoint) { this.startingPoint.set(startingPoint); }

    public void setDestination(String destination) {this.destination.set(destination); }

    public void setTransportType(TransportType transportType) {this.transportType.set(transportType);}

    public void setDescription(String description) {this.description.set(description); }
}
