package org.example.tourplanner.view;

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
import org.example.tourplanner.model.TransportType;
import org.example.tourplanner.viewmodel.TourModalViewModel;

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

    public TourModalController(TourModalViewModel tourModalViewModel) {
        this.tourModalViewModel = tourModalViewModel;
    }

    public void saveTour(ActionEvent actionEvent) {
        if (!validInput()) return;

        if (!tourModalViewModel.saveTour()) {
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("CorrectTransportType"));
        }

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private boolean validInput() {
        //validation
        if(name.getText().isEmpty() || startingPoint.getText().isEmpty() || destination.getText().isEmpty() || description.getText().isEmpty() || transportType.getValue() == null) {
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("BlankField"));
            return false;
        }

        if(!name.getText().matches("[a-zA-ZäöüÄÖÜß ]+")){
            statusBar.setStyle("-fx-background-color: red;");
            statusMessage.setText(this.resources.getString("OnlyLettersTour"));
            return false;
        }
        return true;
    }

    @FXML
    void initialize() {
        // set items in combobox
        transportType.setItems(this.tourModalViewModel.getTransportTypes());
        // if edit, set transportType otherwise select first
        if (tourModalViewModel.getTransportType().get() != null)
            transportType.setValue(tourModalViewModel.getTransportType().get().toString());
        else
            transportType.getSelectionModel().selectFirst();
        // listen for changes and save in viewmodel
        transportType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            tourModalViewModel.setTransportType(TransportType.valueOf(newValue));
        });

        // bind fields
        name.textProperty().bindBidirectional(this.tourModalViewModel.getName());
        startingPoint.textProperty().bindBidirectional(this.tourModalViewModel.getStartingPoint());
        description.textProperty().bindBidirectional(this.tourModalViewModel.getDescription());
        destination.textProperty().bindBidirectional(this.tourModalViewModel.getDestination());
    }

    public void selectAddress(ActionEvent actionEvent) throws IOException {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = FXMLDependencyInjection.getLoader("AddressSelectionModal.fxml", Locale.ENGLISH);
        Parent root = loader.load();
        AddressSelectionController controller = loader.getController();

        controller.setCallback((lat, lng, label) -> {
            if (actionEvent.getTarget() == this.start) {
                tourModalViewModel.setStartingPoint(label, lat, lng);
            } else if (actionEvent.getTarget() == this.end) {
                tourModalViewModel.setDestination(label, lat, lng);
            }
        });

        dialogStage.setScene(new Scene(root));
        dialogStage.setTitle(this.resources.getString("AddressSelectionModalTitle"));
        dialogStage.showAndWait();
    }
}
