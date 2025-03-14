package org.example.tourplanner.model;

public enum TransportType {
    CAR("driving-car"),
    BUS("driving-hgv"),
    FAHRRAD("cycling-regular"),
    ZUFUSS("foot-walking");

    public final String apiValue;

    TransportType(String apiValue) {
        this.apiValue = apiValue;
    }
}
