package org.example.tourplanner.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.tourplanner.model.TransportType;

public class TourModalViewModel {
    private final ObservableList<String> transportType = FXCollections.observableArrayList();

    public TourModalViewModel() {
        transportType.add(TransportType.CAR.toString());
        transportType.add(TransportType.BUS.toString());
        transportType.add(TransportType.FAHRRAD.toString());
        transportType.add(TransportType.ZUFUSS.toString());
    }

    public ObservableList<String> getTransportType() {
        return transportType;
    }
}
