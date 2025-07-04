package org.example.tourplanner.view;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.tourplanner.dto.TourDto;
import org.example.tourplanner.service.ImportExportService;
import org.example.tourplanner.service.TourService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExportDataController {
    private final TourService tourService;
    private final ImportExportService importExportService;

    private static final Logger logger = LogManager.getLogger(ExportDataController.class);

    @FXML
    public ListView<String> listViewTours;
    @FXML
    public HBox statusBar;
    @FXML
    public Label statusMessage;
    @FXML
    ResourceBundle resources;

    public ExportDataController(TourService tourService, ImportExportService importExportService) {
        this.tourService = tourService;
        this.importExportService = importExportService;
    }

    public void initialize() {
        listViewTours.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        List<TourDto> tours = tourService.getAllTours();
        List<String> tourNames = new ArrayList<>();
        for (TourDto tour : tours) {
            tourNames.add(tour.getId() + " | " + tour.getName());
        }

        listViewTours.setItems(FXCollections.observableArrayList(tourNames));
    }

    public void exportData(ActionEvent actionEvent) {
        List<String> selectedTours = listViewTours.getSelectionModel().getSelectedItems();
        System.out.println(selectedTours);
        if (selectedTours == null || selectedTours.isEmpty()) {
            showError("ExportSelectOne");
            return;
        }
        List<Long> tourIds = new ArrayList<>();
        for (String tourName : selectedTours) {
            tourIds.add(Long.valueOf(tourName.split(" | ")[0]));
        }

        byte[] exportBytes = this.importExportService.getTourExport(tourIds);
        logger.info("got data export");
        if (exportBytes != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Tourdata");
            fileChooser.setInitialFileName("tourExport.json");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("JSON Files", "*.json")
            );

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    Files.write(file.toPath(), exportBytes);
                } catch (IOException e) {
                    logger.error("Error with saving tourdata {}", e.getMessage());
                }
            }
        }
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void showError(String messageString) {
        statusBar.setStyle("-fx-background-color: red;");
        statusMessage.setText(this.resources.getString(messageString));
    }
}
