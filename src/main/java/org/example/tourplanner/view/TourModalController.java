package org.example.tourplanner.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.tourplanner.FXMLDependencyInjection;
import org.example.tourplanner.dto.TourDto;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.model.TransportType;
import org.example.tourplanner.service.DirectionsService;
import org.example.tourplanner.service.TourService;
import org.example.tourplanner.viewmodel.TourModalViewModel;
import org.example.tourplanner.viewmodel.TourListViewModel;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class TourModalController {
    private final TourModalViewModel tourModalViewModel;
    public Button handleSavingMethod;
    @FXML
    public Button end;
    @FXML
    public Button start;

    private TourListViewModel tourListViewModel;
    private final TourService tourService;
    private final DirectionsService directionsService;

    @FXML
    public TextField name;
    @FXML
    public TextField startingPoint;
    @FXML
    public ComboBox<String> transportType;
    @FXML
    public TextField destination;
    @FXML
    public TextArea description;
    @FXML
    public HBox statusBar;
    @FXML
    public Label statusMessage;
    @FXML
    private ResourceBundle resources;
    @FXML
    private Tour editableTour;

    private Double startLat;
    private Double startLng;
    private Double destinationLat;
    private Double destinationLng;

    public void setViewModel(TourListViewModel viewModel) {
        this.tourListViewModel = viewModel;
    }

    public TourModalController(TourModalViewModel tourModalViewModel, TourListViewModel tourListViewModel, TourService tourService, DirectionsService directionsService) {
        this.tourModalViewModel = tourModalViewModel;
        this.tourListViewModel = tourListViewModel;
        this.tourService = tourService;
        this.directionsService = directionsService;
    }

    public void handleSavingMethod(ActionEvent actionEvent) {
        //validation
        if(name.getText().isEmpty() || startingPoint.getText().isEmpty() || destination.getText().isEmpty() || description.getText().isEmpty() || transportType.getValue() == null) {
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("BlankField"));
            return;
        }

        if(!name.getText().matches("[a-zA-ZäöüÄÖÜß ]+")){
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("OnlyLettersTour"));
            return;
        }

        TransportType selectedTransportType = TransportType.valueOf(transportType.getSelectionModel().getSelectedItem().toString());
        double duration = 0.0, distance = 0.0;

        try {
            String json = directionsService.getDirections(selectedTransportType.apiValue, startLng, startLat, destinationLng, destinationLat);
            if (json != null) {
                System.out.println(json);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(json);
                JsonNode segments = root.get("features").get(0).get("properties").get("segments").get(0);

                distance = segments.get("distance").asDouble();
                duration = segments.get("duration").asDouble();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // If editableTour = null, then create
            if(editableTour == null) {
                TourDto newTour = new TourDto(null, name.getText(), description.getText(), startingPoint.getText(), startLat, startLng, destination.getText(), destinationLat, destinationLng, selectedTransportType.name(), distance, duration);
                if (this.tourService.saveTour(newTour))
                {
                    this.tourListViewModel.loadTours();
                    Node source = (Node) actionEvent.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                } else {

                }
            } else {
                //Edit
                TourDto tour = new TourDto(editableTour.getId(), name.getText(), description.getText(), startingPoint.getText(), startLat, startLng, destination.getText(), destinationLat, destinationLng, selectedTransportType.name(), distance, duration);
                if (this.tourService.updateTour(tour))
                {
                    this.tourListViewModel.loadTours();
                    Node source = (Node) actionEvent.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                } else {
                    // failed
                }
            }
        } catch (IllegalArgumentException e) {
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("CorrectTransportType"));
        }
    }

    @FXML
    void initialize() {
        transportType.setItems(this.tourModalViewModel.getTransportType());
    }

    public void setTour(Tour selectedTour) {
        this.editableTour = selectedTour;
        name.setText(selectedTour.getName());
        startingPoint.setText(selectedTour.getStartingpoint());
        startLat = selectedTour.getStartLat();
        startLng = selectedTour.getStartLng();
        destination.setText(selectedTour.getDestination());
        destinationLat = selectedTour.getDestinationLat();
        destinationLng = selectedTour.getDestinationLng();
        description.setText(selectedTour.getDescription());
        transportType.setValue(selectedTour.getTransportType().toString());
    }

    public void selectAddress(ActionEvent actionEvent) throws IOException {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = FXMLDependencyInjection.getLoader("AddressSelectionModal.fxml", Locale.ENGLISH);
        Parent root = loader.load();
        AddressSelectionController controller = loader.getController();

        controller.setCallback((lat, lng, label) -> {
            if (actionEvent.getTarget() == this.start) {
                startingPoint.setText(label);
                startLat = lat;
                startLng = lng;
            } else if (actionEvent.getTarget() == this.end) {
                destination.setText(label);
                destinationLat = lat;
                destinationLng = lng;
            }
        });

        dialogStage.setScene(new Scene(root));
        dialogStage.setTitle(this.resources.getString("AddressSelectionModalTitle"));
        dialogStage.showAndWait();
    }
}
