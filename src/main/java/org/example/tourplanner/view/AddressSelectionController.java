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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.tourplanner.service.TourService;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AddressSelectionController {
    private static final Logger logger = LogManager.getLogger(AddressSelectionController.class);
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
            logger.info("Loading AdressSelection.html: {}", leafletUrl.toExternalForm());
            preview.getEngine().load(leafletUrl.toExternalForm());
        } else {
            logger.error("AdressSelection.html not found");
        }

        addressSelector.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            String script = String.format("highlightMarker(%s);", newVal.split(" ")[1]);
            logger.debug("executing script to highlight marker: {}", script);
            preview.getEngine().executeScript(script);
        });
    }

    public void search(ActionEvent actionEvent) {
        if (address.getText().length() < 3) {
            logger.warn("search input too short: {}", address.getText().length());
            return;
        }

        try {
            String encodedAddress = URLEncoder.encode(address.getText(), StandardCharsets.UTF_8);
            logger.info("searching for address: {}", encodedAddress);

            String json = geocodeService.getCoordinates(encodedAddress);
            if (json != null) {
                setMarkersFromORSResponse(json);
                addressSelector.getItems().clear();
                addressSelector.getItems().addAll(pointsMap.keySet());
            } else {
                logger.warn("No response for input: {}", address.getText());
            }
        }
        catch(Exception e) {
            logger.error("Error during search", e);
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
                logger.debug("cleared existing markers");
                for (JsonNode feature : features) {
                    JsonNode coords = feature.get("geometry").get("coordinates");
                    double lng = coords.get(0).asDouble();
                    double lat = coords.get(1).asDouble();

                    String popupText = this.resources.getString("AddressLabelText")  + " " + i;

                    pointsMap.put(popupText, feature);

                    String safePopupText = popupText.replace("'", "\\'");

                    String script = String.format(Locale.US, "addMarker(%f, %f, '%s');", lat, lng, safePopupText);
                    logger.debug("adding marker: {}", script);
                    preview.getEngine().executeScript(script);

                    i++;
                }
                preview.getEngine().executeScript("fitToAllMarkers();");
                logger.info("markers added and view adjusted")
;            }
            else{
                logger.warn("no features found in response");
            }
        } catch (Exception e) {
            logger.error("Error during search", e);
        }
    }

    public void select(ActionEvent actionEvent) {
        String selectedPoint = addressSelector.getSelectionModel().getSelectedItem();
        if (selectedPoint == null) {
            logger.warn("No address selected");
            return;
        }


        JsonNode feature = pointsMap.get(selectedPoint);
        JsonNode coords = feature.get("geometry").get("coordinates");
        double lng = coords.get(0).asDouble();
        double lat = coords.get(1).asDouble();
        if (callback != null) {
            callback.onAddressSelected(lat, lng, feature.get("properties").get("label").toString().replace("\"", ""));
        }
        logger.info("adress selected: {}", selectedPoint);

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}

