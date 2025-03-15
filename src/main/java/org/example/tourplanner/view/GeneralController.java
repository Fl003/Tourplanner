package org.example.tourplanner.view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.tourplanner.viewmodel.GeneralViewModel;

public class GeneralController {
    private final GeneralViewModel generalViewModel;

    @FXML
    public TextField name;
    @FXML
    public TextField startingpoint;
    @FXML
    public Label distance;
    @FXML
    public ComboBox transportType;
    @FXML
    public TextField destination;
    @FXML
    public TextArea description;
    @FXML
    public Label estimatedTime;

    public GeneralController(GeneralViewModel generalViewModel) {
        this.generalViewModel = generalViewModel;
    }

    public void initialize() {
        transportType.setItems(generalViewModel.getTransportType());

        name.textProperty().bindBidirectional(generalViewModel.nameProperty());
        startingpoint.textProperty().bindBidirectional(generalViewModel.startingPointProperty());
        destination.textProperty().bindBidirectional(generalViewModel.destinationProperty());
        transportType.valueProperty().bindBidirectional(generalViewModel.transportTypeProperty());
        description.textProperty().bindBidirectional(generalViewModel.descriptionProperty());
        estimatedTime.textProperty().bindBidirectional(generalViewModel.estimatedTimeProperty());
        distance.textProperty().bindBidirectional(generalViewModel.distanceProperty());
    }
}
