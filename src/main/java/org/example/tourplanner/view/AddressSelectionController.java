package org.example.tourplanner.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.example.tourplanner.service.GeocodeService;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AddressSelectionController {
    @FXML
    public TextField address;
    @FXML
    public WebView preview;
    @FXML
    public ComboBox<String> addressSelector;

    public GeocodeService geocodeService;

    @FXML
    private ResourceBundle resources;

    private AddressSelectionCallback callback;

    public void setCallback(AddressSelectionCallback callback) {
        this.callback = callback;
    }

    private final Map<String, JsonNode> pointsMap = new LinkedHashMap<>();

    public AddressSelectionController(GeocodeService geocodeService) {
        this.geocodeService = geocodeService;
    }

    @FXML
    public void initialize() {
        URL leafletUrl = getClass().getResource("/AddressSelection.html");
        if (leafletUrl != null) {
            preview.getEngine().load(leafletUrl.toExternalForm());
        } else {
            System.err.println("AddressSelection.html not found!");
        }

        addressSelector.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            String script = String.format("highlightMarker(%s);", newVal.split(" ")[1]);
            preview.getEngine().executeScript(script);
        });
    }

    public void search(ActionEvent actionEvent) {
        if (address.getText().length() < 3) {
            return;
        }
        String encodedAddress = URLEncoder.encode(address.getText(), StandardCharsets.UTF_8);
        String json = geocodeService.getCoordinates(encodedAddress);
        if (json != null) {
            setMarkersFromORSResponse(json);
            addressSelector.getItems().clear();
            addressSelector.getItems().addAll(pointsMap.keySet());
        }
    }

    public void setMarkersFromORSResponse(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);
            JsonNode features = root.get("features");

            int i = 1;
            pointsMap.clear();

            if (features != null && features.isArray()) {
                preview.getEngine().executeScript("clearMarkers();");
                for (JsonNode feature : features) {
                    JsonNode coords = feature.get("geometry").get("coordinates");
                    double lng = coords.get(0).asDouble();
                    double lat = coords.get(1).asDouble();

                    String popupText = this.resources.getString("AddressLabelText")  + " " + i;

                    pointsMap.put(popupText, feature);

                    String safePopupText = popupText.replace("'", "\\'");

                    String script = String.format(Locale.US, "addMarker(%f, %f, '%s');", lat, lng, safePopupText);
                    preview.getEngine().executeScript(script);

                    i++;
                }
                preview.getEngine().executeScript("fitToAllMarkers();");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void select(ActionEvent actionEvent) {
        String selectedPoint = addressSelector.getSelectionModel().getSelectedItem();
        JsonNode feature = pointsMap.get(selectedPoint);
        JsonNode coords = feature.get("geometry").get("coordinates");
        double lng = coords.get(0).asDouble();
        double lat = coords.get(1).asDouble();
        if (callback != null) {
            callback.onAddressSelected(lat, lng, feature.get("properties").get("label").toString().replace("\"", ""));
        }

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}

