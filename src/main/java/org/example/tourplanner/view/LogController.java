package org.example.tourplanner.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.tourplanner.FXMLDependencyInjection;
import org.example.tourplanner.model.Log;
import org.example.tourplanner.viewmodel.LogModalViewModel;
import org.example.tourplanner.viewmodel.LogViewModel;
import org.example.tourplanner.viewmodel.TourListViewModel;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogController {
    private final LogViewModel logViewModel;
    private final LogModalViewModel logModalViewModel;
    private final TourListViewModel tourListViewModel;

    @FXML
    public TableView logTable;
    @FXML
    public TableColumn<Log, String> columnDateTime;
    @FXML
    public TableColumn<Log, String> columnDifficulty;
    @FXML
    public TableColumn<Log, Double> columnTotalDistance;
    @FXML
    public TableColumn<Log, Integer> columnTotalTime;
    @FXML
    public TableColumn<Log, Integer> columnRating;
    @FXML
    public TableColumn<Log, String> columnComment;

    // constructor
    public LogController(LogViewModel logViewModel, LogModalViewModel logModalViewModel, TourListViewModel tourListViewModel) {
        this.logViewModel = logViewModel;
        this.logModalViewModel = logModalViewModel;
        this.tourListViewModel = tourListViewModel;
    }

    public void initialize() {
        // define column widths
        columnDateTime.prefWidthProperty().bind(logTable.widthProperty().multiply(0.15));
        columnDifficulty.prefWidthProperty().bind(logTable.widthProperty().multiply(0.10));
        columnTotalDistance.prefWidthProperty().bind(logTable.widthProperty().multiply(0.15));
        columnTotalTime.prefWidthProperty().bind(logTable.widthProperty().multiply(0.20));
        columnRating.prefWidthProperty().bind(logTable.widthProperty().multiply(0.10));
        columnComment.prefWidthProperty().bind(logTable.widthProperty().multiply(0.20));

        // cellFactory for Formatting, Layout, ...
        columnTotalTime.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Integer time, boolean empty) {
                super.updateItem(time, empty);
                if (empty || time == null) {
                    setText(null);
                } else {
                    int hours = time / 3600;
                    int minutes = (time % 3600) / 60;
                    int seconds = time % 60;
                    setText(String.format("%02d h %02d min %02d sec", hours, minutes, seconds));
                }
            }
        });

        columnTotalDistance.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double distance, boolean empty) {
                super.updateItem(distance, empty);
                if (empty || distance == null)
                    setText(null);
                else {
                    double km = (double) distance / 1000;
                    setText(String.format("%.2f", km) + " km");
                }
            }
        });

        columnRating.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Integer rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty || rating == null)
                    setText(null);
                else
                    setText(rating + " Stars");
            }
        });

        columnComment.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String comment, boolean empty) {
                super.updateItem(comment, empty);
                if (empty || comment == null)
                    setText(null);
                else {
                    if (comment.length() > 50)
                        setText(comment.substring(0, 50) + " ...");
                    else
                        setText(comment);
                }
            }
        });

        // CellValueFactory for binding the value
        columnTotalDistance.setCellValueFactory(cellData -> cellData.getValue().totalDistanceProperty().asObject());
        columnTotalTime.setCellValueFactory(cellData -> cellData.getValue().totalTimeProperty().asObject());
        columnDifficulty.setCellValueFactory(cellData -> cellData.getValue().difficultyProperty().asString());
        columnRating.setCellValueFactory(cellData -> cellData.getValue().ratingProperty().asObject());
        columnComment.setCellValueFactory(cellData -> cellData.getValue().commentProperty());
        columnDateTime.setCellValueFactory(cellData -> {
            Log log = cellData.getValue();
            LocalDate date = log.dateProperty().getValue();
            int hour = log.hourProperty().get();
            int minute = log.minuteProperty().get();

            String formattedDateTime = String.format("%s %02d:%02d",
                    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    hour, minute);

            return new SimpleStringProperty(formattedDateTime);
        });

        // column for action buttons (edit, delete)
        TableColumn<Log, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(tc -> new TableCell<>() {
            private final Image imgEdit = new Image(getClass().getResourceAsStream("/org/example/tourplanner/icons/edit.png"), 15, 15, true, true);
            private final Button editBtn = new Button();
            private final Image imgDelete = new Image(getClass().getResourceAsStream("/org/example/tourplanner/icons/delete.png"), 15, 15, true, true);
            private final Button deleteBtn = new Button();
            private final HBox container = new HBox(10, editBtn, deleteBtn);

            {
                // edit button onClick
                editBtn.setOnAction(event -> {
                    Log item = getTableView().getItems().get(getIndex());
                    // set Log to edit in Modal
                    logModalViewModel.setLog(item);
                    // set selectedTour in Modal
                    logModalViewModel.setSelectedTour(logViewModel.getSelectedTour());
                    try {
                        showLogModal();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                });

                // delete button onClick
                deleteBtn.setOnAction(event -> {
                    Log item = getTableView().getItems().get(getIndex());
                    logViewModel.deleteLog(item);
                });

                // styling
                editBtn.setGraphic(new ImageView(imgEdit));
                editBtn.setStyle("-fx-background-color: orange;");
                deleteBtn.setGraphic(new ImageView(imgDelete));
                deleteBtn.setStyle("-fx-background-color: darkred;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
        logTable.getColumns().add(actionColumn);
        // set datasource
        logTable.setItems(logViewModel.getLogs());
    }

    public void createNewLog() {
        this.logModalViewModel.clearLog();
        // set selectedTour in Modal
        this.logModalViewModel.setSelectedTour(logViewModel.getSelectedTour());
        try {
            showLogModal();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void showLogModal() throws IOException {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Parent root = FXMLDependencyInjection.load("LogModal.fxml", Locale.ENGLISH);
        dialogStage.setScene(new Scene(root));
        dialogStage.setTitle("Create Log");
        dialogStage.setMinHeight(307.0);
        dialogStage.setMinWidth(742.0);
        dialogStage.showAndWait();
        // refresh log table after closing Modal
        tourListViewModel.reloadTour(logViewModel.getSelectedTour().getId());
    }
}
