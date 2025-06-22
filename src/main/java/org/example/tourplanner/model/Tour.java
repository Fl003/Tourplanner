package org.example.tourplanner.model;

import javafx.beans.property.*;

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
    // computed attributes -> not saved in db
    private final IntegerProperty popularity;
    private final IntegerProperty childFriendliness;

    // needed for new Tour, no id yet, no computed values
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
        this.popularity = new SimpleIntegerProperty(0);
        this.childFriendliness = new SimpleIntegerProperty();
    }

    public Tour(Long id, String name, String startingPoint, Double startLat, Double startLng, String destination, Double destinationLat, Double destinationLng, TransportType transportType, String description, Double distance, Double estimatedTime, Integer popularity, Integer childFriendliness) {
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
        this.popularity = new SimpleIntegerProperty(popularity);
        this.childFriendliness = new SimpleIntegerProperty(childFriendliness);
    }

    // no id, no distance/estimatedTime, no computed values
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
        this.popularity = new SimpleIntegerProperty(0);
        this.childFriendliness = new SimpleIntegerProperty(0);
    }

    public Long getId() { return id; }

    public String getStartingpoint() {
        return startingPoint.get();
    }

    public Double getStartLat() {
        return startLat.get();
    }

    public Double getStartLng() {
        return startLng.get();
    }

    public String getName() {
        return name.get();
    }

    public String getDestination() {
        return destination.get();
    }
    
    public Double getDestinationLat() {
        return destinationLat.get();
    }

    public Double getDestinationLng() {
        return destinationLng.get();
    }

    public TransportType getTransportType() {
        return transportType.get();
    }

    public String getDescription() {
        return description.get();
    }

    public double getDistance() {
        return distance.get();
    }

    public double getEstimatedTime() {
        return estimatedTime.get();
    }

    public int getPopularity() { return popularity.get(); }

    public int getChildFriendliness() { return childFriendliness.get(); }

    public void setDistance(double distance) {
        this.distance.set(distance);
    }

    public void setEstimatedTime(double estimatedTime) {
        this.distance.set(estimatedTime);
    }

    public void setPopularity(int popularity) { this.popularity.set(popularity); }

    public void setChildFriendliness(int childFriendliness) { this.childFriendliness.set(childFriendliness); }

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
