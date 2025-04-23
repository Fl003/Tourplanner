package org.example.tourplanner.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.tourplanner.dto.TourDto;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.model.TransportType;
import org.example.tourplanner.service.TourService;
import org.example.tourplanner.viewmodel.TourModalViewModel;
import org.example.tourplanner.viewmodel.TourListViewModel;
import java.util.ResourceBundle;

public class TourModalController {
    private final TourModalViewModel tourModalViewModel;
    public Button handleSavingMethod;
    private TourListViewModel tourListViewModel;
    private MainController mainController;
    private TourService tourService;

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


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setViewModel(TourListViewModel viewModel) {
        this.tourListViewModel = viewModel;
    }

    public TourModalController(TourModalViewModel tourModalViewModel, TourListViewModel tourListViewModel, TourService tourService) {
        this.tourModalViewModel = tourModalViewModel;
        this.tourListViewModel = tourListViewModel;
        this.tourService = tourService;
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
                TourDto newTour = new TourDto(null, name.getText(), description.getText(), startingPoint.getText(), destination.getText(), selectedTransportType.name(), 0.0, 0.0);
                if (this.tourService.saveTour(newTour))
                {
                    //MainController.showSuccess(this.resources.getString("SuccessMessage"));
                    this.tourListViewModel.loadTours();
                    Node source = (Node) actionEvent.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                } else {
                    // failed
                }
            } else {
                //Edit mode
                TourDto tour = new TourDto(editableTour.getId(), name.getText(), description.getText(), startingPoint.getText(), destination.getText(), selectedTransportType.name(), 0.0, 0.0);
                if (this.tourService.updateTour(tour))
                {
                    //MainController.showSuccess(this.resources.getString("SuccessMessage"));
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
        destination.setText(selectedTour.getDestination());
        description.setText(selectedTour.getDescription());
        transportType.setValue(selectedTour.getTransportType().toString());
    }
}
