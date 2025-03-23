package org.example.tourplanner.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.tourplanner.FXMLDependencyInjection;
import org.example.tourplanner.model.Log;
import org.example.tourplanner.viewmodel.LogViewModel;

import java.io.IOException;
import java.util.Locale;

public class LogController {
    private final LogViewModel logViewModel;

    @FXML
    public TableView logTable;
    @FXML
    public TableColumn columnDateTime;
    @FXML
    public TableColumn<Log, String> columnDifficulty;
    @FXML
    public TableColumn<Log, Integer> columnTotalDistance;
    @FXML
    public TableColumn<Log, Integer> columnTotalTime;
    @FXML
    public TableColumn<Log, Integer> columnRating;

    public LogController(LogViewModel logViewModel) {
        this.logViewModel = logViewModel;
    }

    public void initialize() {
        columnDateTime.prefWidthProperty().bind(logTable.widthProperty().divide(5)); // w * 1/4
        columnDifficulty.prefWidthProperty().bind(logTable.widthProperty().divide(5)); // w * 1/2
        columnTotalDistance.prefWidthProperty().bind(logTable.widthProperty().divide(5)); // w * 1/4
        columnTotalTime.prefWidthProperty().bind(logTable.widthProperty().divide(5)); // w * 1/4
        columnRating.prefWidthProperty().bind(logTable.widthProperty().divide(5)); // w * 1/4

        columnDifficulty.setCellValueFactory(cellData -> cellData.getValue().difficultyProperty());
        columnTotalDistance.setCellValueFactory(cellData -> cellData.getValue().totalDistanceProperty().asObject());

        logTable.setItems(logViewModel.getLogs());
    }

    public void showLogModal(ActionEvent actionEvent) throws IOException {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Parent root = FXMLDependencyInjection.load("LogModal.fxml", Locale.ENGLISH);
        dialogStage.setScene(new Scene(root));
        dialogStage.setTitle("Create Log");
        dialogStage.setMinHeight(307.0);
        dialogStage.setMinWidth(742.0);
        dialogStage.showAndWait();
    }
}
