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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
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

    public TourModalController(TourModalViewModel tourModalViewModel, TourListViewModel tourListViewModel) {
        this.tourModalViewModel = tourModalViewModel;
        this.tourListViewModel = tourListViewModel;
    }

    public void createTour(ActionEvent actionEvent) {
        // TODO: status bar red, status message = message, strings in res bundle
        //validation
        if(name.getText().isEmpty() || startingPoint.getText().isEmpty() || destination.getText().isEmpty() || description.getText().isEmpty() || transportType.getValue() == null) {
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("BlankField"));
            return;
        }

        if(!name.getText().matches("[a-zA-ZäöüÄÖÜß ]+") || !startingPoint.getText().matches("[a-zA-ZäöüÄÖÜß ]+") || !destination.getText().matches("[a-zA-ZäöüÄÖÜß ]+")){
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("OnlyLetters"));
            return;
        }

        try{
            TransportType selectedTransportType = TransportType.valueOf(transportType.getSelectionModel().getSelectedItem().toString());
            Tour newTour = new Tour(name.getText(), startingPoint.getText(), destination.getText(), selectedTransportType, description.getText());
            this.tourListViewModel.addTour(newTour);
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } catch(IllegalArgumentException e){
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("CorrectTransportType"));
        }
    }

    @FXML
    void initialize() {
        transportType.setItems(this.tourModalViewModel.getTransportType());

    }
}
