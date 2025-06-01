package org.example.tourplanner.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.service.DirectionsService;
import org.example.tourplanner.viewmodel.MapViewModel;

import java.net.URL;
import java.util.Locale;

public class MapController {
    private final MapViewModel mapViewModel;
    private final DirectionsService directionsService;

    @FXML
    public WebView map;

    public MapController(MapViewModel mapViewModel, DirectionsService directionsService) {
        this.mapViewModel = mapViewModel;
        this.directionsService = directionsService;
    }

    @FXML
    public void initialize() {
        URL leafletUrl = getClass().getResource("/Map.html");
        if (leafletUrl != null) {
            map.getEngine().load(leafletUrl.toExternalForm());
        } else {
            System.err.println("AddressSelection.html not found!");
        }

        mapViewModel.selectedTourProperty().addListener((obs, oldTour, selectedTour) -> {
            if (selectedTour != null) {
                loadMap(selectedTour);
            }
        });
    }

    public void loadMap(Tour selectedTour) {
        String json = directionsService.getDirections(selectedTour.getTransportType().apiValue, selectedTour.getStartLng(), selectedTour.getStartLat(), selectedTour.getDestinationLng(), selectedTour.getDestinationLat());
        map.getEngine().executeScript("updateRoute(" + json + ");");

        try {
            ObjectMapper mapper = new ObjectMapper();
            String startLabel = mapper.writeValueAsString(selectedTour.getStartingpoint());
            String endLabel = mapper.writeValueAsString(selectedTour.getDestination());

            String script = String.format(Locale.ENGLISH, "addMarker(%f, %f, '%s');", selectedTour.getStartLat(), selectedTour.getStartLng(), startLabel.replace("\"", ""));
            map.getEngine().executeScript(script);
            script = String.format(Locale.ENGLISH, "addMarker(%f, %f, '%s');", selectedTour.getDestinationLat(), selectedTour.getDestinationLng(), endLabel.replace("\"", ""));
            map.getEngine().executeScript(script);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
