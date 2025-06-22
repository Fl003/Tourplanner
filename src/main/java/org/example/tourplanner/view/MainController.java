package org.example.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.tourplanner.FXMLDependencyInjection;
import org.example.tourplanner.viewmodel.TourModalViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private final TourModalViewModel tourModalViewModel;

    public MainController(TourModalViewModel tourModalViewModel) {
        this.tourModalViewModel = tourModalViewModel;
    }

    public void showTourModal(ActionEvent actionEvent) throws IOException {
        tourModalViewModel.clearTour();

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Parent root = FXMLDependencyInjection.load("TourModal.fxml", Locale.ENGLISH);
        dialogStage.setScene(new Scene(root));
        dialogStage.setTitle("Create Tour");
        dialogStage.setMinHeight(251.0);
        dialogStage.setMinWidth(742.0);
        dialogStage.showAndWait();
    }
}