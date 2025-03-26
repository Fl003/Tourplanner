package org.example.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.model.TransportType;
import org.example.tourplanner.viewmodel.TourModalViewModel;
import org.example.tourplanner.viewmodel.TourListViewModel;
import java.util.ResourceBundle;

public class TourModalController {
    private final TourModalViewModel tourModalViewModel;
    private final TourListViewModel tourListViewModel;

    @FXML
    public TextField name;
    @FXML
    public TextField startingPoint;
    @FXML
    public ComboBox transportType;
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

    public TourModalController(TourModalViewModel tourModalViewModel, TourListViewModel tourListViewModel) {
        this.tourModalViewModel = tourModalViewModel;
        this.tourListViewModel = tourListViewModel;
    }

    public void handleSavingMethod(ActionEvent actionEvent) {
        //validation
        if(name.getText().isEmpty() || startingPoint.getText().isEmpty() || destination.getText().isEmpty() || description.getText().isEmpty() || transportType.getValue() == null) {
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("BlankField"));
            return;
        }

        if(!name.getText().matches("[a-zA-ZäöüÄÖÜß ]+") || !startingPoint.getText().matches("[a-zA-ZäöüÄÖÜß ]+") || !destination.getText().matches("[a-zA-ZäöüÄÖÜß ]+")){
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("OnlyLettersTour"));
            return;
        }

        try {
            TransportType selectedTransportType = TransportType.valueOf(transportType.getSelectionModel().getSelectedItem().toString());
            // If editableTour = null, then create mode
            if(editableTour == null) {
                Tour newTour = new Tour(name.getText(), startingPoint.getText(), destination.getText(), selectedTransportType, description.getText());
                this.tourListViewModel.addTour(newTour);
            } else {
                //Edit mode
                editableTour.setName(name.getText());
                editableTour.setStartingPoint(startingPoint.getText());
                editableTour.setDestination(destination.getText());
                editableTour.setDescription(description.getText());
                editableTour.setTransportType(selectedTransportType);
                tourListViewModel.updateTour(editableTour);
            }
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

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
        destination.setText(selectedTour.getDestination());
        description.setText(selectedTour.getDescription());
        transportType.setValue(selectedTour.getTransportType().toString());
    }
}
