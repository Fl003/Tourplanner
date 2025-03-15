package org.example.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.tourplanner.FXMLDependencyInjection;
import org.example.tourplanner.model.Tour;
import org.example.tourplanner.viewmodel.TourListViewModel;

import java.io.IOException;
import java.util.Locale;

public class TourListController {
    @FXML
    public ListView<Tour> tourList;

    private final TourListViewModel tourListViewModel;

    public TourListController(TourListViewModel tourListViewModel) {
        this.tourListViewModel = tourListViewModel;
    }

    @FXML
    void initialize() {
        tourList.setItems(tourListViewModel.getObservableTours());
        tourList.getSelectionModel().selectedItemProperty().addListener(tourListViewModel.getChangeListener());
    }

    public void showTourModal(ActionEvent actionEvent) throws IOException {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Parent root = FXMLDependencyInjection.load("TourModal.fxml", Locale.GERMAN);
        dialogStage.setScene(new Scene(root));
        dialogStage.setTitle("Create Tour");
        dialogStage.setMinHeight(251.0);
        dialogStage.setMinWidth(742.0);
        dialogStage.showAndWait();
    }
}
