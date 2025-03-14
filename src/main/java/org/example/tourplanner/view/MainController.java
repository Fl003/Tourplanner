package org.example.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.tourplanner.FXMLDependencyInjection;
import org.example.tourplanner.viewmodel.MainViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    @FXML
    public TableView tourTable;

    private final MainViewModel mainViewModel;

    public MainController(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    @FXML
    protected void onHelloButtonClick() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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