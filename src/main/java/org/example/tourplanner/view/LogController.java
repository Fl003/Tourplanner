package org.example.tourplanner.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.tourplanner.viewmodel.LogViewModel;

public class LogController {
    private final LogViewModel logViewModel;

    @FXML
    public TableView logTable;
    @FXML
    public TableColumn columnDateTime;
    @FXML
    public TableColumn columnDifficulty;
    @FXML
    public TableColumn columnTotalDistance;
    @FXML
    public TableColumn columnTotalTime;
    @FXML
    public TableColumn columnRating;

    public LogController(LogViewModel logViewModel) {
        this.logViewModel = logViewModel;
    }

    public void initialize() {
        columnDateTime.prefWidthProperty().bind(logTable.widthProperty().divide(5)); // w * 1/4
        columnDifficulty.prefWidthProperty().bind(logTable.widthProperty().divide(5)); // w * 1/2
        columnTotalDistance.prefWidthProperty().bind(logTable.widthProperty().divide(5)); // w * 1/4
        columnTotalTime.prefWidthProperty().bind(logTable.widthProperty().divide(5)); // w * 1/4
        columnRating.prefWidthProperty().bind(logTable.widthProperty().divide(5)); // w * 1/4
    }
}
