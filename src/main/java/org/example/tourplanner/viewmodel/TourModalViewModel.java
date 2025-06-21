package org.example.tourplanner.viewmodel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.tourplanner.dto.TourDto;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.model.TransportType;
import org.example.tourplanner.service.DirectionsService;
import org.example.tourplanner.service.TourService;

import java.io.IOException;

public class TourModalViewModel {
    private final ObservableList<String> transportTypes = FXCollections.observableArrayList();

    private final TourService tourService;
    private final DirectionsService directionsService;

    private Long id = null;
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty description = new SimpleStringProperty();
    private final ObjectProperty<TransportType> transportType = new SimpleObjectProperty<>();
    private final SimpleStringProperty startingPoint = new SimpleStringProperty();
    private final SimpleDoubleProperty startLat = new SimpleDoubleProperty();
    private final SimpleDoubleProperty startLng = new SimpleDoubleProperty();
    private final SimpleStringProperty destination = new SimpleStringProperty();
    private final SimpleDoubleProperty destinationLat = new SimpleDoubleProperty();
    private final SimpleDoubleProperty destinationLng = new SimpleDoubleProperty();

    public TourModalViewModel(TourService tourService, DirectionsService directionsService) {
        this.tourService = tourService;
        this.directionsService = directionsService;

        transportTypes.add(TransportType.Car.toString());
        transportTypes.add(TransportType.Bus.toString());
        transportTypes.add(TransportType.Bike.toString());
        transportTypes.add(TransportType.Walking.toString());
    }

    public boolean saveTour() {
        double duration = 0.0, distance = 0.0;

        try {
            String json = directionsService.getDirections(transportType.get().apiValue, startLng.get(), startLat.get(), destinationLng.get(), destinationLat.get());
            if (json != null) {
                System.out.println(json);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(json);
                JsonNode segments = root.get("features").get(0).get("properties").get("segments").get(0);

                distance = segments.get("distance").asDouble();
                duration = segments.get("duration").asDouble();
            }
        } catch (IOException e) {
            return false;
        }

        try {
            TourDto tourDto = new TourDto(id, name.get(), description.get(), startingPoint.get(), startLat.get(), startLng.get(), destination.get(), destinationLat.get(), destinationLng.get(), transportType.get().toString(), distance, duration);

            if (id == null) {
                this.tourService.saveTour(tourDto);
            } else {
                this.tourService.updateTour(tourDto);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void setStartingPoint(String startingPoint, Double startLat, Double startLng) {
        this.startingPoint.set(startingPoint);
        this.startLat.set(startLat);
        this.startLng.set(startLng);
    }

    public void setDestination(String destination, Double destinationLat, Double destinationLng) {
        this.destination.set(destination);
        this.destinationLat.set(destinationLat);
        this.destinationLng.set(destinationLng);
    }

    public void setTour(Tour selectedTour) {
        id = selectedTour.getId();
        name.set(selectedTour.getName());
        setStartingPoint(selectedTour.getStartingpoint(), selectedTour.getStartLat(), selectedTour.getStartLng());
        setDestination(selectedTour.getDestination(), selectedTour.getDestinationLat(), selectedTour.getDestinationLng());
        description.set(selectedTour.getDescription());
        transportType.set(selectedTour.getTransportType());
    }

    public void clearTour() {
        id = null;
        name.set("");
        description.set("");
        setStartingPoint(null, 0.0, 0.0);
        setDestination(null, 0.0, 0.0);
        transportType.set(null);
    }

    public ObservableList<String> getTransportTypes() {
        return transportTypes;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType.set(transportType);
    }

    public StringProperty getName() { return name; }
    public StringProperty getDescription() { return description; }
    public StringProperty getStartingPoint() { return startingPoint; }
    public StringProperty getDestination() { return destination; }
    public ObjectProperty<TransportType> getTransportType() { return transportType; }
}
