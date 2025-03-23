package org.example.tourplanner.model;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private final StringProperty name;
    private final StringProperty startingPoint;
    private final StringProperty destination;
    private final ObjectProperty<TransportType> transportType;
    private final StringProperty description;
    private final DoubleProperty distance;
    private final DoubleProperty estimatedTime;
    private List<Log> logs;


    public Tour(StringProperty name, StringProperty startingPoint, StringProperty destination, ObjectProperty<TransportType> transportType, StringProperty description, DoubleProperty distance, DoubleProperty estimatedTime) {
        this.name = name;
        this.startingPoint = startingPoint;
        this.destination = destination;
        this.transportType = transportType;
        this.description = description;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.logs = new ArrayList<>();
    }

    public Tour(String name, String startingPoint, String destination, TransportType transportType, String description, Double distance, Double estimatedTime) {
        this.name = new SimpleStringProperty(name);
        this.startingPoint = new SimpleStringProperty(startingPoint);
        this.destination= new SimpleStringProperty(destination);
        this.transportType = new SimpleObjectProperty<>(transportType);
        this.description = new SimpleStringProperty(description);
        this.distance = new SimpleDoubleProperty(distance);
        this.estimatedTime = new SimpleDoubleProperty(estimatedTime);
        this.logs = new ArrayList<>();
    }

    public Tour(String name, String startingPoint, String destination, TransportType transportType, String description) {
        this.name = new SimpleStringProperty(name);
        this.startingPoint = new SimpleStringProperty(startingPoint);
        this.destination= new SimpleStringProperty(destination);
        this.transportType = new SimpleObjectProperty<>(transportType);
        this.description = new SimpleStringProperty(description);
        this.distance = new SimpleDoubleProperty(0);
        this.estimatedTime = new SimpleDoubleProperty(0);
        this.logs = new ArrayList<>();
    }

    public void addLog(Log log) {
        this.logs.add(log);
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public String getStartingpoint() {
        return startingPoint.get();
    }

    public StringProperty startingPointProperty() {
        return startingPoint;
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

}
