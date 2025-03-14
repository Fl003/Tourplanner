package org.example.tourplanner.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tour {
    private final StringProperty name;
    private final StringProperty startingpoint;
    private final StringProperty destination;
    private final StringProperty transportType;
    private final StringProperty description;
    private final DoubleProperty distance;
    private final DoubleProperty duration;


    public Tour(StringProperty name, StringProperty startingpoint, StringProperty destination, StringProperty transportType, StringProperty description, DoubleProperty distance, DoubleProperty duration) {
        this.name = name;
        this.startingpoint = startingpoint;
        this.destination = destination;
        this.transportType = transportType;
        this.description = description;
        this.distance = distance;
        this.duration = duration;
    }

    public Tour(String name, String startingpoint, String destination, String transportType, String description, Double distance, Double duration) {
        this.name = new SimpleStringProperty(name);
        this.startingpoint = new SimpleStringProperty(startingpoint);
        this.destination= new SimpleStringProperty(destination);
        this.transportType = new SimpleStringProperty(transportType);
        this.description = new SimpleStringProperty(description);
        this.distance = new SimpleDoubleProperty(distance);
        this.duration = new SimpleDoubleProperty(duration);

    }

    public Tour(String name, String startingpoint, String destination, String transportType, String description) {
        this.name = new SimpleStringProperty(name);
        this.startingpoint = new SimpleStringProperty(startingpoint);
        this.destination= new SimpleStringProperty(destination);
        this.transportType = new SimpleStringProperty(transportType);
        this.description = new SimpleStringProperty(description);
        this.distance = new SimpleDoubleProperty(0);
        this.duration = new SimpleDoubleProperty(0);
    }

    public String getStartingpoint() {
        return startingpoint.get();
    }

    public StringProperty startingpointProperty() {
        return startingpoint;
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

    public String getTransportType() {
        return transportType.get();
    }

    public StringProperty transportTypeProperty() {
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

    public double getDuration() {
        return duration.get();
    }

    public DoubleProperty durationProperty() {
        return duration;
    }

    public void setDistance(double distance) {
        this.distance.set(distance);
    }

    public void setDuration(double duration) {
        this.distance.set(duration);
    }

    @Override
    public String toString() {
        return getName();
    }

}
