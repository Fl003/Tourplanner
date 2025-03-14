package org.example.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.viewmodel.TourModalViewModel;
import org.example.tourplanner.viewmodel.TourOverviewModel;

public class TourModalController {
    private final TourModalViewModel tourModalViewModel;
    private final TourOverviewModel tourOverviewModel;

    @FXML
    public TextField tourName;
    @FXML
    public TextField tourStartingpoint;
    @FXML
    public ComboBox tourTransporttype;
    @FXML
    public TextField tourDestination;
    @FXML
    public TextArea tourDescription;

    public TourModalController(TourModalViewModel tourModalViewModel, TourOverviewModel tourOverviewModel) {
        this.tourModalViewModel = tourModalViewModel;
        this.tourOverviewModel = tourOverviewModel;
    }

    public void createTour(ActionEvent actionEvent) {
        // TODO: validation
        Tour newTour = new Tour(tourName.getText(), tourStartingpoint.getText(), tourDestination.getText(), tourTransporttype.getSelectionModel().getSelectedItem().toString(), tourDescription.getText());
        this.tourOverviewModel.addTour(newTour);
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
        tourTransporttype.setItems(this.tourModalViewModel.getTransportType());
    }
}
